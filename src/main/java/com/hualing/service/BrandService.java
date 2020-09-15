package com.hualing.service;

import com.hualing.common.UserClaim;
import com.hualing.domain.Brand;

import java.util.Date;
import java.util.List;
import com.hualing.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Will on 07/07/2019.
 */
@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> getAllBrands(){
        return this.brandRepository.findByIsDeleted(false);
    }

    public void saveBrand(Brand brand, UserClaim uc){
        brand.setLastUpdatedBy(uc.getId());
        brand.setLastUpdatedTime(new Date());
        this.brandRepository.save(brand);
    }

    public Brand getBrand(Long id){
        return this.brandRepository.findById(id).get();
    }

    public Brand getOnly(){
        return  brandRepository.findByIsDeleted(false).get(0);
    }

    public Boolean exist(String name){
        Brand brand = this.brandRepository.findByName(name);
        return brand != null;
    }
}
