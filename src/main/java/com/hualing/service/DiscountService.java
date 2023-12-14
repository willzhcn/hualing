package com.hualing.service;

import com.hualing.common.Constants;
import com.hualing.common.CredentialException;
import com.hualing.common.UserClaim;
import com.hualing.criteria.DiscountCriteria;
import com.hualing.criteria.OrgCriteria;
import com.hualing.domain.Discount;
import com.hualing.domain.Org;
import com.hualing.repository.DiscountRepository;
import com.hualing.repository.StoreCommodityRepository;
import com.hualing.util.StringUtils;
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

    @Autowired
    private StoreCommodityRepository storeCommodityRepository;

    public void save(Discount discount, UserClaim uc){
        if(StringUtils.nonNull(discount.getCommodityNo()) && storeCommodityRepository.countAllByCommodityNo(discount.getCommodityNo()) == 0)
            throw new CredentialException(30001, "货号不存在！");
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

    public Discount matchDiscount(long orgId, String orderDateStr, int year, String quarter, String commodityNo) throws ParseException {
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

        Discount discount = null;

        //先根据货号查询折扣
        if(StringUtils.nonNull(commodityNo)){
            criteria.setCommodityNo(commodityNo);
            List<Discount> list = discountRepository.findAll(criteria.buildSpecification());
            for(Discount dis: list){
                if((dis.getStartDate() == null || dis.getStartDate().getTime() <= orderDate.getTime()) && ((dis.getEndDate() == null || dis.getEndDate().getTime() >= orderDate.getTime())))
                    return dis;
            }
            //如果没有匹配折扣，去除机构条件进行匹配
            criteria.setPartBId(0);
            list = discountRepository.findAll(criteria.buildSpecification());
            for(Discount dis: list){
                if((dis.getStartDate() == null || dis.getStartDate().getTime() <= orderDate.getTime()) && ((dis.getEndDate() == null || dis.getEndDate().getTime() >= orderDate.getTime())) && dis.getPartB() == null) {
                    return dis;
                }
            }
        }

        //如果根据货号无法匹配，则去除货号条件进行匹配
        criteria.setCommodityNo(null);
        criteria.setPartBId(orgId);
        List<Discount> list = discountRepository.findAll(criteria.buildSpecification());
        for(Discount dis: list){
            if((dis.getCommodityNo() == null) && (dis.getStartDate() == null || dis.getStartDate().getTime() <= orderDate.getTime()) && ((dis.getEndDate() == null || dis.getEndDate().getTime() >= orderDate.getTime())))
                discount = dis;
        }

        if(discount == null){
            criteria.setPartBId(0);
            list = discountRepository.findAll(criteria.buildSpecification());
            for(Discount dis: list){
                if((dis.getCommodityNo() == null) && (dis.getStartDate() == null || dis.getStartDate().getTime() <= orderDate.getTime()) && ((dis.getEndDate() == null || dis.getEndDate().getTime() >= orderDate.getTime())) && dis.getPartB() == null) {
                    discount = dis;
                    break;
                }
            }
        }
        return discount;
    }
}
