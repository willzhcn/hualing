package com.hualing.repository;

import com.hualing.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor {

    User findFirstByAccount(String account);

    User findFirstByAccountAndPassword(String account, String password);

    List<User> findByIsDeleted(Boolean deleted);

}

