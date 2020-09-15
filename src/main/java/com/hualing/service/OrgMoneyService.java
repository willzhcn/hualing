package com.hualing.service;

import com.hualing.common.Constants;
import com.hualing.common.UserClaim;
import com.hualing.criteria.OrgMoneyCriteria;
import com.hualing.domain.Org;
import com.hualing.domain.OrgMoney;
import com.hualing.domain.OrgMoneyDetail;
import com.hualing.domain.User;
import com.hualing.repository.OrgMoneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Will on 25/07/2019.
 */
@Service
public class OrgMoneyService {
    @Autowired
    private OrgMoneyRepository orgMoneyRepository;

    @Autowired
    private OrgMoneyDetailService orgMoneyDetailService;

    public void save(OrgMoney orgMoney, UserClaim uc){
        orgMoney.setLastUpdatedTime(new Date());
        orgMoney.setLastUpdatedBy(uc.getId());

        this.orgMoneyRepository.save(orgMoney);
    }

    public Page<OrgMoney> query(OrgMoneyCriteria orgMoneyCriteria){
        return this.orgMoneyRepository.findAll(orgMoneyCriteria.buildSpecification(), orgMoneyCriteria.buildPageRequest());
    }

    public OrgMoney get(Long id){
        return this.orgMoneyRepository.findById(id).get();
    }

    public Boolean exist(Long orgId){
        OrgMoneyCriteria orgMoneyCriteria = new OrgMoneyCriteria();
        orgMoneyCriteria.setDeleted(false);
        orgMoneyCriteria.setOrgId(orgId);
        long count = this.orgMoneyRepository.count(orgMoneyCriteria.buildSpecification());

        return count > 0;
    }

    @Transactional
    public void recharge(Long orgId, Double sum, String remarks,UserClaim uc){
        Org org = new Org();
        org.setId(orgId);
        Date date = new Date();
        OrgMoney orgMoney = orgMoneyRepository.findFirstByOrg(org);
        BigDecimal balance = new BigDecimal(orgMoney.getBalance() + sum);
        orgMoney.setBalance(balance.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
        orgMoney.setLastUpdatedBy(uc.getId());
        orgMoney.setLastUpdatedTime(date);
        OrgMoneyDetail detail = new OrgMoneyDetail();
        detail.setAmount(sum);
        detail.setBalance(orgMoney.getBalance());
        detail.setDealType(Constants.DEAL_TYPE_RECHARGE);
        detail.setOrg(org);
        detail.setComments(remarks);
        orgMoneyDetailService.save(detail, uc);
        orgMoneyRepository.save(orgMoney);
    }
}
