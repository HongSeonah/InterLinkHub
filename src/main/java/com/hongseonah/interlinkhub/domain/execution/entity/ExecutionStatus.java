package com.hongseonah.interlinkhub.domain.execution.entity;

public enum ExecutionStatus {
    PENDING,
    RUNNING,
    SUCCESS,
    FAILED,
    TIMEOUT,
    CANCELLED,
    RETRYING
}