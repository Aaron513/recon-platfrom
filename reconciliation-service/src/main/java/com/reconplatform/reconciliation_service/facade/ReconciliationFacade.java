package com.reconplatform.reconciliation_service.facade;

import com.reconplatform.common.dto.TransactionDTO;
import com.reconplatform.common.enums.MismatchReason;
import com.reconplatform.common.enums.TransactionStatus;
import com.reconplatform.common.messaging.KafkaTopics;
import com.reconplatform.reconciliation_service.model.LedgerBalanceEntity;
import com.reconplatform.reconciliation_service.uow.UnitOfWork;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;
import com.reconplatform.common.error.AppException;
import com.reconplatform.reconciliation_service.enrichment.CurrencyEnrichmentClient;
import com.reconplatform.reconciliation_service.enrichment.GeoRiskClient;
import com.reconplatform.reconciliation_service.model.AuditLogEntity;
import com.reconplatform.reconciliation_service.model.MismatchEntity;
import com.reconplatform.reconciliation_service.model.TransactionEntity;
import com.reconplatform.reconciliation_service.repository.LedgerBalanceRepository;
import com.reconplatform.reconciliation_service.repository.TransactionRepository;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.kafka.core.KafkaTemplate;

@Service
public class ReconciliationFacade {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationFacade.class);
    private static final Set<String> HIGH_RISK_COUNTRIES = Set.of("NG", "KP", "IR");

    private final TransactionRepository transactionRepository;
    private final LedgerBalanceRepository ledgerRepository;
    private final CurrencyEnrichmentClient currencyClient;
    private final GeoRiskClient geoRiskClient;
    private final ObjectProvider<UnitOfWork> uowProvider;
    private final KafkaTemplate<String, TransactionDTO> kafkaTemplate;

    public ReconciliationFacade(TransactionRepository transactionRepository,
                                LedgerBalanceRepository ledgerRepository,
                                CurrencyEnrichmentClient currencyClient,
                                GeoRiskClient geoRiskClient,
                                ObjectProvider<UnitOfWork> uowProvider,
                                KafkaTemplate<String, TransactionDTO> kafkaTemplate) {
        this.transactionRepository = transactionRepository;
        this.ledgerRepository = ledgerRepository;
        this.currencyClient = currencyClient;
        this.geoRiskClient = geoRiskClient;
        this.uowProvider = uowProvider;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void process(TransactionDTO dto) {

        if (transactionRepository.existsById(dto.getTransactionId())) {
            log.warn("Duplicate transaction {}, skipping", dto.getTransactionId());
            return;
        }

        BigDecimal converted = currencyClient.convertToInr(dto.getAmount(), dto.getCurrency());
        String country = null;
        boolean geoSkipped = false;
        try {
            country = geoRiskClient.lookupCountry(dto.getOriginIp());
        } catch (AppException e) {
            log.warn("Geo lookup skipped for {}: {}", dto.getTransactionId(), e.getErrorCode().getCode());
            geoSkipped = true;
        }

        Optional<LedgerBalanceEntity> ledger = ledgerRepository.findById(dto.getAccountNumber());

        MismatchReason reason = evaluate(ledger, converted, country);

        dto.setConvertedAmount(converted);
        dto.setTargetCurrency("INR");
        dto.setRiskCountry(country);
        dto.setFlagReason(reason);
        dto.setStatus(reason == null ? TransactionStatus.RECONCILED : TransactionStatus.FLAGGED);

        UnitOfWork uow = uowProvider.getObject();
        uow.registerNew(toTransactionEntity(dto));
        if (reason != null) {
            uow.registerNew(toMismatchEntity(dto, reason, ledger, converted));
        } else {
            LedgerBalanceEntity l = ledger.get();
            l.setExpectedBalance(l.getExpectedBalance().subtract(converted));
            uow.registerDirty(l);
        }
        uow.registerNew(toAuditEntity(dto, reason, geoSkipped));
        uow.commit();

        String topic = (reason == null) ? KafkaTopics.TRANSACTION_ENRICHED : KafkaTopics.TRANSACTION_FLAGGED;
        try {
            kafkaTemplate.send(topic, dto.getAccountNumber(), dto)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Publish of {} to {} failed", dto.getTransactionId(), topic, ex);
                        }
                    });
        } catch (Exception e) {
            log.error("Publish of {} to {} failed", dto.getTransactionId(), topic, e);
        }
    }

    private MismatchReason evaluate(Optional<LedgerBalanceEntity> ledger, BigDecimal converted, String country) {
        if (ledger.isEmpty()) return MismatchReason.UNKNOWN_ACCOUNT;
        if (ledger.get().getExpectedBalance().compareTo(converted) < 0) return MismatchReason.OVERDRAFT;
        if (country != null && HIGH_RISK_COUNTRIES.contains(country)) return MismatchReason.HIGH_RISK_ORIGIN;
        return null;
    }

    private TransactionEntity toTransactionEntity(TransactionDTO dto) {
        TransactionEntity e = new TransactionEntity();
        e.setTransactionId(dto.getTransactionId());
        e.setBatchId(dto.getBatchId());
        e.setAccountNumber(dto.getAccountNumber());
        e.setAmount(dto.getAmount());
        e.setCurrency(dto.getCurrency());
        e.setConvertedAmount(dto.getConvertedAmount());
        e.setTargetCurrency(dto.getTargetCurrency());
        e.setTransactionDate(dto.getTransactionDate());
        e.setOriginIp(dto.getOriginIp());
        e.setRiskCountry(dto.getRiskCountry());
        e.setStatus(dto.getStatus());
        return e;
    }

    private MismatchEntity toMismatchEntity(TransactionDTO dto, MismatchReason reason,
                                            Optional<LedgerBalanceEntity> ledger, BigDecimal converted) {
        MismatchEntity m = new MismatchEntity();
        m.setMismatchId(UUID.randomUUID().toString());
        m.setTransactionId(dto.getTransactionId());
        m.setReason(reason);
        if (reason == MismatchReason.OVERDRAFT) {
            BigDecimal balance = ledger.get().getExpectedBalance();
            m.setExpectedBalance(balance);
            m.setActualBalance(balance.subtract(converted));
            m.setDifference(converted.subtract(balance));
        }
        return m;
    }

    private AuditLogEntity toAuditEntity(TransactionDTO dto, MismatchReason reason, boolean geoSkipped) {
        AuditLogEntity a = new AuditLogEntity();
        a.setTransactionId(dto.getTransactionId());
        a.setPerformedBy("SYSTEM");
        a.setAction(dto.getStatus().name());
        a.setDetails((reason == null ? "applied to ledger" : "flagged: " + reason)
                + (geoSkipped ? "; geo lookup skipped" : ""));
        return a;
    }
}