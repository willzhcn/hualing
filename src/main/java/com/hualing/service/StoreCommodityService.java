package com.hualing.service;

import com.hualing.common.UserClaim;
import com.hualing.criteria.StoreCommodityCriteria;
import com.hualing.criteria.StoreCommodityVCriteria;
import com.hualing.domain.StoreCommodity;
import com.hualing.domain.StoreCommodityV;
import com.hualing.repository.StoreCommodityRepository;
import com.hualing.repository.StoreCommodityVRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Will on 19/07/2019.
 */
@Service
public class StoreCommodityService {
    @Autowired
    private StoreCommodityRepository storeCommodityRepository;

    @Autowired
    private StoreCommodityVRepository storeCommodityVRepository;

    public Page<StoreCommodity> query(StoreCommodityCriteria storeCommodityCriteria){
        return this.storeCommodityRepository.findAll(storeCommodityCriteria.buildSpecification(), storeCommodityCriteria.buildPageRequest());
    }

    public Page<StoreCommodityV> agentQuery(StoreCommodityVCriteria criteria){
        return this.storeCommodityVRepository.findAll(criteria.buildSpecification(), criteria.buildPageRequest());
    }

    public List<StoreCommodityV> agentQueryAll(StoreCommodityVCriteria criteria){
        return this.storeCommodityVRepository.findAll(criteria.buildSpecification());
    }

    public StoreCommodity matchOne(StoreCommodityCriteria criteria){
        List<StoreCommodity> list = this.storeCommodityRepository.findAll(criteria.buildSpecification());
        if(list.size() > 0)
            return list.get(0);
        else return null;
    }

    public List<StoreCommodity> queryAll(StoreCommodityCriteria storeCommodityCriteria){
        return this.storeCommodityRepository.findAll(storeCommodityCriteria.buildSpecification());
    }

    public StoreCommodity getStoreCommodity(Long id){
        return this.storeCommodityRepository.findById(id).get();
    }

    public void saveList(List<StoreCommodity> list){
        if(list != null && list.size() > 0)
            this.storeCommodityRepository.saveAll(list);
    }

    public void save(StoreCommodity storeCommodity, UserClaim uc){

        this.storeCommodityRepository.save(storeCommodity);
    }

    public boolean exist(String commodityNo, String size){
        StoreCommodityCriteria storeCommodityCriteria = new StoreCommodityCriteria();
        storeCommodityCriteria.setCommodityNo(commodityNo);
        storeCommodityCriteria.setActive(true);
        storeCommodityCriteria.setSizeNo(size);
        long count = this.storeCommodityRepository.count(storeCommodityCriteria.buildSpecification());
        return count > 0;
    }

    public int count(String commodityNo, String size, int year, String quarter){
        StoreCommodityCriteria storeCommodityCriteria = new StoreCommodityCriteria();
        storeCommodityCriteria.setActive(true);
        storeCommodityCriteria.setCommodityNo(commodityNo);
        storeCommodityCriteria.setSizeNo(size);
        storeCommodityCriteria.setYear(year);
        storeCommodityCriteria.setQuarter(quarter);
        List<StoreCommodity> list = storeCommodityRepository.findAll(storeCommodityCriteria.buildSpecification());
        int count = 0;
        for(StoreCommodity s: list){
            count += s.getQuantity();
        }
        return count;
    }
}
