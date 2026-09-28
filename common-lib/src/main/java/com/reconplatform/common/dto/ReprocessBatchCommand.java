package com.reconplatform.common.dto;

import com.reconplatform.common.enums.CommandType;

import java.time.Instant;

public class ReprocessBatchCommand {
    private CommandType commandType = CommandType.REPROCESS_BATCH;
    private String batchId;
    private String requestedBy;
    private Instant requestedAt;

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Instant requestedAt) {
        this.requestedAt = requestedAt;
    }
}
