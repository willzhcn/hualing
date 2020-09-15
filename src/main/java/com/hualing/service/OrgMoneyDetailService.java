package com.hualing.service;

import com.hualing.common.UserClaim;
import com.hualing.criteria.OrgMoneyDetailCriteria;
import com.hualing.domain.OrgMoneyDetail;
import com.hualing.domain.User;
import com.hualing.repository.OrgMoneyDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Will on 30/08/2019.
 */
@Service
public class OrgMoneyDetailService {
    @Autowired
    private OrgMoneyDetailRepository orgMoneyDetailRepository;

    public void save(OrgMoneyDetail detail, UserClaim uc){
        detail.setDealTime(new Date());
        User user = new User();
        user.setId(uc.getId());
        detail.setTrader(user);

        this.orgMoneyDetailRepository.save(detail);
    }

    public Page<OrgMoneyDetail> query(OrgMoneyDetailCriteria orgMoneyCriteria){
        return this.orgMoneyDetailRepository.findAll(orgMoneyCriteria.buildSpecification(), orgMoneyCriteria.buildPageRequest());
    }

    public List<OrgMoneyDetail> queryAll(OrgMoneyDetailCriteria orgMoneyCriteria){
        return this.orgMoneyDetailRepository.findAll(orgMoneyCriteria.buildSpecification());
    }
}
