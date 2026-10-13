package com.reconplatform.reconciliation_service.uow;

public interface UnitOfWork {
    void registerNew(Object entity);
    void registerDirty(Object entity);
    void registerRemoved(Object entity);
    void commit();
    void rollback();
}