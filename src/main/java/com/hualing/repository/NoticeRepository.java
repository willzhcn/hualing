package com.hualing.repository;

import com.hualing.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by Will on 10/10/2019.
 */
public interface NoticeRepository extends JpaRepository<Notice, Long>, JpaSpecificationExecutor {
    public List<Notice> findByIsDeletedOrderByIdDesc(boolean deleted);
}
