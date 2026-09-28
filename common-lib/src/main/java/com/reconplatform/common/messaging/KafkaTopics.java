package com.reconplatform.common.messaging;

public final class KafkaTopics {
    public static final String TRANSACTION_INGESTED = "transaction.ingested";
    public static final String TRANSACTION_ENRICHED  = "transaction.enriched";
    public static final String TRANSACTION_FLAGGED   = "transaction.flagged";
    private KafkaTopics() {}
}
