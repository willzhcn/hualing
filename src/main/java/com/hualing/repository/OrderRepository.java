package com.hualing.repository;

import com.hualing.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Will on 22/07/2019.
 */
public interface OrderRepository  extends JpaRepository<Order, Long>, JpaSpecificationExecutor {
    @Query(value = "select order_no from t_order where org_id = ?1 order by id desc limit 1" , nativeQuery = true)
    List<Object[]> getLatestOrderNo(Long orgId);
}
