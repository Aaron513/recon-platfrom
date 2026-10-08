package com.reconplatform.ingestion_service.repository;

import com.reconplatform.ingestion_service.model.RawBatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RawBatchRepository extends JpaRepository<RawBatchEntity, String> {
    List<RawBatchEntity> findByStatus(String status);
}