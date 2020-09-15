package com.hualing.repository;

import com.hualing.domain.Brand;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Will on 07/07/2019.
 */
public interface BrandRepository extends JpaRepository<Brand, Long>, JpaSpecificationExecutor {
    public List<Brand> findByIsDeleted(Boolean deleted);

    public Brand findByName(String name);
}
