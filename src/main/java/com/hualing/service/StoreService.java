package com.hualing.service;

import com.hualing.common.UserClaim;
import com.hualing.criteria.StoreCriteria;
import com.hualing.domain.Store;
import com.hualing.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Will on 07/07/2019.
 */
@Service
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;

    public List<Store> getAll(){
        return this.storeRepository.findByIsDeleted(false);
    }

    public List<Store> allExpressNotRequired(){
        StoreCriteria storeCriteria = new StoreCriteria();
        storeCriteria.setExpressRequired(false);
        return this.storeRepository.findAll(storeCriteria.buildSpecification());
    }

    public void saveStore(Store store, UserClaim uc){
        store.setLastUpdatedTime(new Date());
        store.setLastUpdatedBy(uc.getId());

        this.storeRepository.save(store);
    }

    public Page<Store> queryStores(StoreCriteria storeCriteria){
        return this.storeRepository.findAll(storeCriteria.buildSpecification(), storeCriteria.buildPageRequest());
    }

    public Store getStore(Long id){
        return this.storeRepository.findById(id).get();
    }

    public Boolean exist(String name){
        Store store = this.storeRepository.findByName(name);

        return store != null;
    }
}
