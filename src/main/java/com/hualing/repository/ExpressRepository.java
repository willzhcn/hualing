package com.hualing.repository;

import com.hualing.domain.Express;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by Will on 07/07/2019.
 */
public interface ExpressRepository  extends JpaRepository<Express, Long>, JpaSpecificationExecutor {
    public List<Express> findByIsDeleted(Boolean deleted);

    public Express findByName(String name);
}
