package com.reconplatform.review_service.repository;

import com.reconplatform.review_service.model.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUserEntity, String> {
    Optional<AppUserEntity> findByUsername(String username);
}