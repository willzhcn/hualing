package com.hualing.repository;

import com.hualing.domain.OrderWithDistribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Will on 13/09/2019.
 */
public interface OrderWithDistributeRepository   extends JpaRepository<OrderWithDistribute, Long>, JpaSpecificationExecutor {

}
