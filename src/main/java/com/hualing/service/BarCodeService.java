package com.hualing.service;

import com.hualing.common.Constants;
import com.hualing.common.UserClaim;
import com.hualing.criteria.BarCodeCriteria;
import com.hualing.domain.BarCode;
import com.hualing.repository.BarCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Created by Will on 15/07/2019.
 */
@Service
public class BarCodeService {
    @Autowired
    private EntityManager em;

    @Autowired
    private BarCodeRepository barCodeRepository;

    @Autowired
    private EntityManager entityManager;

    private Logger logger = LoggerFactory.getLogger(BarCodeService.class);

    public void saveBarCode(BarCode barCode, UserClaim uc){
        barCode.setLastUpdatedTime(new Date());
        barCode.setLastUpdatedBy(uc.getId());

        this.barCodeRepository.save(barCode);
    }

    public Page<BarCode> queryBarCodes(BarCodeCriteria barCodeCriteria){
        return this.barCodeRepository.findAll(barCodeCriteria.buildSpecification(), barCodeCriteria.buildPageRequest());
    }

    @Transactional
    public void insertInBatch(List<BarCode> list, UserClaim uc){
        if(list == null || list.size() == 0)
            return;
        logger.info("Begin to batch insert barcode");
        for(int i = 0; i < list.size(); i++){
            if (i > 0 && i % Constants.BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
                logger.info("insert " + Constants.BATCH_SIZE);
            }
            BarCode barCode = list.get(i);
            barCode.setLastUpdatedTime(new Date());
            barCode.setLastUpdatedBy(uc.getId());
            entityManager.persist(barCode);
        }
        entityManager.flush();
        entityManager.clear();
        logger.info("End to batch insert barcode");

    }

    public BarCode getBarCode(Long id){
        return this.barCodeRepository.findById(id).get();
    }

    @Transactional
    public void saveBarCodeList(List<BarCode> list, UserClaim uc){
        if(list == null || list.size() == 0)
            return;
        for(BarCode barCode: list){
            barCode.setLastUpdatedTime(new Date());
            barCode.setLastUpdatedBy(uc.getId());
        }
        this.barCodeRepository.saveAll(list);
    }

    @Transactional
    public void removeBarCodes(){
        String storeSql = "delete from t_barcode where id > 0";
        Query query1 = em.createNativeQuery(storeSql);
        query1.executeUpdate();
    }
}
