package com.reconplatform.reconciliation_service.uow;

import com.reconplatform.common.error.AppException;
import com.reconplatform.common.error.ErrorCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HibernateUnitOfWork implements UnitOfWork {

    @PersistenceContext
    private EntityManager entityManager;

    private final List<Object> newEntities = new ArrayList<>();
    private final List<Object> dirtyEntities = new ArrayList<>();
    private final List<Object> removedEntities = new ArrayList<>();

    @Override
    public void registerNew(Object entity) {
        newEntities.add(entity);
    }

    @Override
    public void registerDirty(Object entity) {
        dirtyEntities.add(entity);
    }

    @Override
    public void registerRemoved(Object entity) {
        removedEntities.add(entity);
    }

    @Override
    @Transactional
    public void commit() {
        try {
            newEntities.forEach(entityManager::persist);

            for (Object entity : dirtyEntities) {
                if (!entityManager.contains(entity)) {
                    entityManager.merge(entity);
                }
            }
            for (Object entity : removedEntities) {
                Object managed = entityManager.contains(entity) ? entity : entityManager.merge(entity);
                entityManager.remove(managed);
            }
            entityManager.flush();
        } catch (PersistenceException e) {
            throw new AppException(ErrorCode.RECON_UOW_COMMIT_FAILURE, e);
        } finally {
            clear();
        }
    }

    @Override
    public void rollback() {
        clear();
    }

    private void clear() {
        newEntities.clear();
        dirtyEntities.clear();
        removedEntities.clear();
    }
}