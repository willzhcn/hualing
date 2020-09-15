package com.hualing.service;

import com.hualing.common.Constants;
import com.hualing.common.UserClaim;
import com.hualing.criteria.DiscountCriteria;
import com.hualing.criteria.OrgCriteria;
import com.hualing.domain.Discount;
import com.hualing.domain.Org;
import com.hualing.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Will on 22/07/2019.
 */
@Service
public class DiscountService {
    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private OrgService orgService;

    public void save(Discount discount, UserClaim uc){
        discount.setLastUpdatedTime(new Date());
        discount.setLastUpdatedBy(uc.getId());

        this.discountRepository.save(discount);
    }

    public Page<Discount> query(DiscountCriteria discountCriteria){
        return this.discountRepository.findAll(discountCriteria.buildSpecification(), discountCriteria.buildPageRequest());
    }

    public Discount get(Long id){
        return this.discountRepository.findById(id).get();
    }

    public Discount matchDiscount(long orgId, String orderDateStr, int year, String quarter) throws ParseException {
        DiscountCriteria criteria = new DiscountCriteria();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date orderDate = sf.parse(orderDateStr);
        criteria.setDeleted(false);
        criteria.setPartBId(orgId);
        criteria.setYear(year);
        criteria.setQuarter(quarter);
        //华凌电商机构
        OrgCriteria orgCriteria = new OrgCriteria();
        orgCriteria.setOrgTypeStr(Constants.ORG_TYPE_HUALING);
        Org hualing = orgService.queryOrgs(orgCriteria).iterator().next();
        criteria.setPartAId(hualing.getId());
        List<Discount> list = discountRepository.findAll(criteria.buildSpecification());
        Discount discount = null;
        for(Discount dis: list){
            if((dis.getStartDate() == null || dis.getStartDate().getTime() <= orderDate.getTime()) && ((dis.getEndDate() == null || dis.getEndDate().getTime() >= orderDate.getTime())))
                discount = dis;
        }

        if(discount == null){
            criteria.setPartBId(0);
            list = discountRepository.findAll(criteria.buildSpecification());
            for(Discount dis: list){
                if((dis.getStartDate() == null || dis.getStartDate().getTime() <= orderDate.getTime()) && ((dis.getEndDate() == null || dis.getEndDate().getTime() >= orderDate.getTime())) && dis.getPartB() == null) {
                    discount = dis;
                    break;
                }
            }
        }
        return discount;
    }
}
