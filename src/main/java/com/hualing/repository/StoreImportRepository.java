package com.hualing.repository;

import com.hualing.domain.StoreImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Will on 19/07/2019.
 */
public interface StoreImportRepository extends JpaRepository<StoreImport, Long>, JpaSpecificationExecutor {

}
