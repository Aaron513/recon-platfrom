package com.reconplatform.ingestion_service.service;

import com.reconplatform.common.dto.TransactionDTO;

@Service
public class BatchValidationService {
    private static final Set<String> VALID_CURRENCIES = Set.of("USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", "SEK", "NZD");

    public void validate(TransactionDTO dto) {
        if (isBlank(dto.getTransactionId()) || isBlank(dto.getAccountNumber())) {
            throw new AppException(ErrorCode.INGESTION_VALIDATION_FAILED);
        }
        if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppException(ErrorCode.INGESTION_VALIDATION_FAILED);
        }
        if (dto.getCurrency() == null || !VALID_CURRENCIES.contains(dto.getCurrency())) {
            throw new AppException(ErrorCode.INGESTION_VALIDATION_FAILED);
        }
        if (dto.getTransactionDate() == null || dto.getTransactionDate().isAfter(Instant.now())) {
            throw new AppException(ErrorCode.INGESTION_VALIDATION_FAILED);
        }
    }

    private boolean isBlank(String s) { return s == null || s.isBlank(); }
}

