package com.hualing.repository;

import com.hualing.domain.ExpressDistribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Will on 04/08/2019.
 */
public interface ExpressDistributeRepository  extends JpaRepository<ExpressDistribute, Long>, JpaSpecificationExecutor {
}
