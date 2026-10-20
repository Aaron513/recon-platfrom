package com.reconplatform.common.dto;

import com.reconplatform.common.enums.ReviewStatus;

import java.util.Map;

public record ReviewDecisionCommand(String transactionId, ReviewStatus decision,
                                    String reviewedBy, long reviewedAtEpochMs) {

    public Map<String, Object> toMap() {
        return Map.of("transactionId", transactionId, "decision", decision.name(),
                "reviewedBy", reviewedBy, "reviewedAt", reviewedAtEpochMs);
    }

    public static ReviewDecisionCommand fromMap(Map<String, Object> m) {
        return new ReviewDecisionCommand((String) m.get("transactionId"),
                ReviewStatus.valueOf((String) m.get("decision")),
                (String) m.get("reviewedBy"),
                (Long) m.get("reviewedAt"));
    }
}