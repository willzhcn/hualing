package com.hualing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hualing.common.ActionResult;
import com.hualing.common.Constants;
import com.hualing.common.UserClaim;
import com.hualing.criteria.StoreImportCriteria;
import com.hualing.domain.Store;
import com.hualing.domain.StoreCommodity;
import com.hualing.domain.StoreImport;
import com.hualing.domain.User;
import com.hualing.repository.BrandRepository;
import com.hualing.repository.OrgRepository;
import com.hualing.repository.StoreImportRepository;
import com.hualing.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Will on 19/07/2019.
 */
@Service
public class StoreImportService {
    @Value("${api.file.store.server}")
    private String fileStoreServer;
    @Autowired
    private StoreImportRepository storeImportRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private StoreCommodityService storeCommodityService;

    @Autowired
    private OrgRepository orgRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private EntityManager em;

    @Transactional
    public void importStore(String jsonStr, MultipartFile file, UserClaim uc) throws Exception {

        try {
            StoreImport storeImport = new ObjectMapper().readValue(jsonStr, StoreImport.class);
            String fileName = fileService.uploadFile(file, Constants.STORE_IMPORT_DIR);
            if(fileName == null){
                throw new Exception("上传文件失败!");
            } else {
                HashMap<String, Store> storeMap = new HashMap<String, Store>();
                StoreImportCriteria storeImportCriteria = new StoreImportCriteria();
                storeImportCriteria.setBrandId(storeImport.getBrand().getId());
//                storeImportCriteria.setOrgId(storeImport.getOrg().getId());
                storeImportCriteria.setActive(true);
                List<StoreImport> activeList = storeImportRepository.findAll(storeImportCriteria.buildSpecification());
                if(activeList != null && activeList.size() > 0) {
                    for (StoreImport activeStore : activeList) {
                        activeStore.setActive(false);
                    }
                    this.storeImportRepository.saveAll(activeList);
                }
                storeImport.setFileName(fileName);
                Date date = new Date();
                storeImport.setLastUpdatedTime(date);
                storeImport.setLastUpdatedBy(uc.getId());
                User user = new User();
                user.setId(uc.getId());
                storeImport.setLoadBy(user);
                storeImport.setLoadTime(date);
                storeImport.setActive(true);
                StoreImport temp = this.storeImportRepository.save(storeImport);
                storeImport.setId(temp.getId());
                List<StoreCommodity> list = storeImport.getDataList();
                for(StoreCommodity storeCommodity: list){
                    storeCommodity.setStoreImport(storeImport);
                    Store store = storeMap.get(storeCommodity.getStoreName());
                    if(store == null) {
                        store = storeRepository.findByName(storeCommodity.getStoreName());
                        if(store != null)
                            storeMap.put(storeCommodity.getStoreName(), store);
                    }
                    if(store == null){
                        throw new Exception("店铺名称不存在：" + storeCommodity.getStoreName());
                    } else {
                        storeCommodity.setStore(store);
                    }
                }
                storeCommodityService.saveList(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("IO 操作失败!");
        }
    }

    public Page<StoreCommodity> query(StoreImportCriteria storeImportCriteria){
        return this.storeImportRepository.findAll(storeImportCriteria.buildSpecification(), storeImportCriteria.buildPageRequest());
    }

    public String getFilePathById(Long id){
        StoreImport storeImport = this.storeImportRepository.findById(id).get();
        if(storeImport != null)
            return fileStoreServer + storeImport.getFileName();
        else return null;
    }

    @Transactional
    public void removeUselessData(Date date){
        String storeSql = "delete from t_store_import where load_time < ? and is_active = 0 and id > 0";
        String storeCommoditySql = "delete from t_store_commodity where store_id in " +
                                    "(select id from t_store_import  where load_time < ?  and is_active = 0 ) and id > 0;";
        Query query1 = em.createNativeQuery(storeCommoditySql);
        query1.setParameter(1, date, TemporalType.DATE);
        query1.executeUpdate();
        Query query2 = em.createNativeQuery(storeSql);
        query2.setParameter(1, date, TemporalType.DATE);
        query2.executeUpdate();


    }
}
