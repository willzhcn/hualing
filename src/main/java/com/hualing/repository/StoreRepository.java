package com.hualing.repository;

import com.hualing.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by Will on 07/07/2019.
 */
public interface StoreRepository extends JpaRepository<Store, Long>, JpaSpecificationExecutor {
    public List<Store> findByIsDeleted(Boolean deleted);

    public Store findByName(String name);
}
