package com.hualing.repository;

import com.hualing.domain.OrderDistributeSimple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Will on 03/09/2019.
 */
public interface OrderDistributeSimpleRepository  extends JpaRepository<OrderDistributeSimple, Long>, JpaSpecificationExecutor {
}
