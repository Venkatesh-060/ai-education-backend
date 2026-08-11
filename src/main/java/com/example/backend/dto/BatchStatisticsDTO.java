package com.example.backend.dto;

public class BatchStatisticsDTO {

    private long totalBatches;

    private long activeBatches;

    private long completedBatches;

    private long inactiveBatches;

    private long totalStudentsAllocated;

    private long batchesWithTrainer;

    public BatchStatisticsDTO() {
    }

    public long getTotalBatches() {
        return totalBatches;
    }

    public void setTotalBatches(long totalBatches) {
        this.totalBatches = totalBatches;
    }

    public long getActiveBatches() {
        return activeBatches;
    }

    public void setActiveBatches(long activeBatches) {
        this.activeBatches = activeBatches;
    }

    public long getCompletedBatches() {
        return completedBatches;
    }

    public void setCompletedBatches(long completedBatches) {
        this.completedBatches = completedBatches;
    }

    public long getInactiveBatches() {
        return inactiveBatches;
    }

    public void setInactiveBatches(long inactiveBatches) {
        this.inactiveBatches = inactiveBatches;
    }

    public long getTotalStudentsAllocated() {
        return totalStudentsAllocated;
    }

    public void setTotalStudentsAllocated(long totalStudentsAllocated) {
        this.totalStudentsAllocated = totalStudentsAllocated;
    }

    public long getBatchesWithTrainer() {
        return batchesWithTrainer;
    }

    public void setBatchesWithTrainer(long batchesWithTrainer) {
        this.batchesWithTrainer = batchesWithTrainer;
    }
}