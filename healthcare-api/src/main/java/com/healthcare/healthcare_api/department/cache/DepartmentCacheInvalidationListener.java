package com.healthcare.healthcare_api.department.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class DepartmentCacheInvalidationListener {

    private final CacheManager cacheManager;

    public DepartmentCacheInvalidationListener(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void invalidateDepartmentCaches(DepartmentCacheInvalidationEvent event) {

        Cache departmentByIdCache = cacheManager.getCache("departmentById");
        Cache departmentsCache = cacheManager.getCache("departments");

        if (departmentByIdCache != null) {
            departmentByIdCache.evict(event.departmentId());
        }

        if (departmentsCache != null) {
            departmentsCache.clear();
        }
    }
}
