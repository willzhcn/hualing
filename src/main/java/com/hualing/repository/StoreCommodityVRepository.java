package com.hualing.repository;

import com.hualing.domain.StoreCommodityV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Will on 28/08/2019.
 */
public interface StoreCommodityVRepository extends JpaRepository<StoreCommodityV, Long>, JpaSpecificationExecutor {
}
