package com.hualing.repository;

import com.hualing.domain.OrgMoneyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Will on 30/08/2019.
 */
public interface OrgMoneyDetailRepository  extends JpaRepository<OrgMoneyDetail, Long>, JpaSpecificationExecutor {

}
