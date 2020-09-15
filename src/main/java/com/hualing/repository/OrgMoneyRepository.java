package com.hualing.repository;

import com.hualing.domain.Org;
import com.hualing.domain.OrgMoney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Will on 25/07/2019.
 */
public interface OrgMoneyRepository  extends JpaRepository<OrgMoney, Long>, JpaSpecificationExecutor {
    public OrgMoney findFirstByOrg(Org org );
}
