package com.hualing.repository;

import com.hualing.domain.StoreCommodity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Will on 19/07/2019.
 */
public interface StoreCommodityRepository extends JpaRepository<StoreCommodity, Long>, JpaSpecificationExecutor {
    int countAllByCommodityNo(String commodityNo);
}
