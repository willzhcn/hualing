package com.hualing.repository;

import com.hualing.domain.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by Will on 22/07/2019.
 */
public interface DiscountRepository extends JpaRepository<Discount, Long>, JpaSpecificationExecutor {
    List<Discount> findAllByIsDeletedOrderByPartBAsc(boolean isDeleted);
}
