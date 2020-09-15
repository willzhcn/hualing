package com.hualing.repository;

import com.hualing.domain.Org;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * Created by Will on 21/06/2019.
 */
public interface OrgRepository extends JpaRepository<Org, Long>, JpaSpecificationExecutor {
    public List<Org> findByIsDeleted(Boolean deleted);

    public Org findByCompanyName(String companyName);
}
