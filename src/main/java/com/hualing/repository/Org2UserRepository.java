package com.hualing.repository;

import com.hualing.domain.Org2User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

/**
 * Created by Will on 21/06/2019.
 */
public interface Org2UserRepository extends JpaRepository<Org2User, Long>, JpaSpecificationExecutor {
    Set<Org2User> findAllByUserId(long userId);
}
