package com.hualing.service;

import com.hualing.common.UserClaim;
import com.hualing.criteria.ExpressCriteria;
import com.hualing.domain.Express;
import com.hualing.repository.ExpressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Will on 07/07/2019.
 */
@Service
public class ExpressService {
    @Autowired
    private ExpressRepository expressRepository;

    public List<Express> getAll(){
        return this.expressRepository.findByIsDeleted(false);
    }

    public void saveExpress(Express express, UserClaim uc){
        express.setLastUpdatedTime(new Date());
        express.setLastUpdatedBy(uc.getId());

        this.expressRepository.save(express);
    }

    public Page<Express> queryExpresses(ExpressCriteria expressCriteria){
        return this.expressRepository.findAll(expressCriteria.buildSpecification(), expressCriteria.buildPageRequest());
    }

    public Express getExpress(Long id){
        return this.expressRepository.findById(id).get();
    }

    public Boolean exist(String name){
        Express express = this.expressRepository.findByName(name);
        return express != null;
    }
}
