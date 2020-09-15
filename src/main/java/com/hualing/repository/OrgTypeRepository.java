package com.hualing.repository;

import com.hualing.domain.OrgType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Will on 21/06/2019.
 */
public interface OrgTypeRepository extends JpaRepository<OrgType, Long>, JpaSpecificationExecutor {
}
