package com.reconplatform.ingestion_service.repository;

public interface RawBatchRepository extends JpaRepository<RawBatch, String> {
    List<RawBatch> findByStatus(String status);
}