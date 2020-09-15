package com.hualing.repository;

import com.hualing.domain.BarCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by Will on 15/07/2019.
 */
public interface BarCodeRepository extends JpaRepository<BarCode, Long>, JpaSpecificationExecutor {
    public List<BarCode> findByIsDeleted(Boolean deleted);
}
