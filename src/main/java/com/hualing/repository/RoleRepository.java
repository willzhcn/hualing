package com.hualing.repository;

import com.hualing.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Will on 04/07/2019.
 */
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor {
}
