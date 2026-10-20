package com.reconplatform.reconciliation_service.service;

import com.reconplatform.common.dto.ReviewDecisionCommand;
import com.reconplatform.common.enums.ReviewStatus;
import com.reconplatform.common.enums.TransactionStatus;
import com.reconplatform.reconciliation_service.model.AuditLogEntity;
import com.reconplatform.reconciliation_service.model.LedgerBalanceEntity;
import com.reconplatform.reconciliation_service.model.TransactionEntity;
import com.reconplatform.reconciliation_service.repository.LedgerBalanceRepository;
import com.reconplatform.reconciliation_service.repository.TransactionRepository;
import com.reconplatform.reconciliation_service.uow.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewDecisionService {

    private static final Logger log = LoggerFactory.getLogger(ReviewDecisionService.class);

    private final TransactionRepository transactionRepository;
    private final LedgerBalanceRepository ledgerRepository;
    private final ObjectProvider<UnitOfWork> uowProvider;

    public ReviewDecisionService(TransactionRepository transactionRepository,
                                 LedgerBalanceRepository ledgerRepository,
                                 ObjectProvider<UnitOfWork> uowProvider) {
        this.transactionRepository = transactionRepository;
        this.ledgerRepository = ledgerRepository;
        this.uowProvider = uowProvider;
    }

    public void apply(ReviewDecisionCommand cmd) {
        Optional<TransactionEntity> found = transactionRepository.findById(cmd.transactionId());
        if (found.isEmpty()) {
            log.warn("Decision for unknown transaction {}, ignoring", cmd.transactionId());
            return;
        }
        TransactionEntity tx = found.get();
        if (tx.getStatus() != TransactionStatus.FLAGGED) {
            log.warn("Decision for {} ignored, status is already {}", tx.getTransactionId(), tx.getStatus());
            return;
        }
        if (cmd.decision() != ReviewStatus.APPROVED && cmd.decision() != ReviewStatus.REJECTED) {
            log.warn("Unsupported decision {} for {}", cmd.decision(), tx.getTransactionId());
            return;
        }

        UnitOfWork uow = uowProvider.getObject();
        String details;

        if (cmd.decision() == ReviewStatus.APPROVED) {
            tx.setStatus(TransactionStatus.RECONCILED);
            Optional<LedgerBalanceEntity> ledger = ledgerRepository.findById(tx.getAccountNumber());
            if (ledger.isPresent()) {
                LedgerBalanceEntity l = ledger.get();
                l.setExpectedBalance(l.getExpectedBalance().subtract(tx.getConvertedAmount()));
                uow.registerDirty(l);
                details = "approved, applied to ledger";
            } else {
                details = "approved, no ledger account to apply to";
            }
        } else {
            tx.setStatus(TransactionStatus.REJECTED);
            details = "rejected, ledger untouched";
        }

        uow.registerDirty(tx);

        AuditLogEntity audit = new AuditLogEntity();
        audit.setTransactionId(tx.getTransactionId());
        audit.setAction("REVIEW_" + cmd.decision().name());
        audit.setPerformedBy(cmd.reviewedBy());
        audit.setDetails(details);
        uow.registerNew(audit);

        uow.commit();
        log.info("Applied {} decision for {}", cmd.decision(), tx.getTransactionId());
    }
}