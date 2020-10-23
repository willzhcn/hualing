package com.hualing.repository;

import com.hualing.domain.OrderRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by Will on 12/08/2019.
 */
public interface OrderRequestRepository  extends JpaRepository<OrderRequest, Long>, JpaSpecificationExecutor {
    List<OrderRequest> findAllByStatus(String status);
}
