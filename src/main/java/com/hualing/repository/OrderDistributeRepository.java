package com.hualing.repository;

import com.hualing.domain.OrderDistribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Will on 01/08/2019.
 */
public interface OrderDistributeRepository extends JpaRepository<OrderDistribute, Long>, JpaSpecificationExecutor {
}
