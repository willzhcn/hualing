package com.hualing.service;

import com.hualing.common.CredentialException;
import com.hualing.criteria.OrgCriteria;
import com.hualing.domain.Org;
import com.hualing.domain.OrgType;
import com.hualing.repository.OrgRepository;
import com.hualing.repository.OrgTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by Will on 21/06/2019.
 */
@Service
public class OrgService {
    @Autowired
    private OrgRepository orgRepository;
    @Autowired
    private OrgTypeRepository orgTypeRepository;
    @Autowired
    EntityManager em;

    public List<OrgType> getAllTypes(){
        return this.orgTypeRepository.findAll();
    }

    public List<Org> getAllOrgs(){
        return this.orgRepository.findByIsDeleted(false);
    }

    public Org getOrgById(Long id){
        return orgRepository.findById(id).get();
    }

    public Page<Org> queryOrgs(OrgCriteria orgCriteria){
        return this.orgRepository.findAll(orgCriteria.buildSpecification(), orgCriteria.buildPageRequest());
    }

    public void saveOrg(Org org) throws CredentialException {
        if(org.isDeleted()){
            //检查是否有关联用户
            String sql = "select count(*) from t_user u inner join t_org2user t on u.id = t.user_id where org_id=?";
            Query query =  em.createNativeQuery(sql);
            query.setParameter(1, org.getId());
            List<BigInteger> list = query.getResultList();
            int count = list.get(0).intValue();
            if(count > 0)
                throw new CredentialException(50001, "请先删除机构下的用户！");
        }
        this.orgRepository.save(org);
    }

    public Boolean exist(String name){
        Org org = this.orgRepository.findByCompanyName(name);
        return org != null;
    }
}
