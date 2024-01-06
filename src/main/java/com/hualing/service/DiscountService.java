package com.hualing.service;

import com.hualing.common.Constants;
import com.hualing.common.CredentialException;
import com.hualing.common.UserClaim;
import com.hualing.criteria.DiscountCriteria;
import com.hualing.criteria.OrgCriteria;
import com.hualing.domain.Brand;
import com.hualing.domain.Discount;
import com.hualing.domain.DiscountImportItem;
import com.hualing.domain.Org;
import com.hualing.repository.BrandRepository;
import com.hualing.repository.DiscountRepository;
import com.hualing.repository.OrgRepository;
import com.hualing.repository.StoreCommodityRepository;
import com.hualing.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @Autowired
    private OrgRepository orgRepository;
    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    EntityManager em;

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

    public Discount matchDiscount(long orgId, int year, String quarter, String commodityNo, List<Discount> discountList){
        Discount discount = null;
        Date orderDate = new Date();

        //先根据货号查询折扣
        if(StringUtils.nonNull(commodityNo)){
            for(Discount dis: discountList){
                if((dis.getPartB() != null && dis.getPartB().getId() == orgId) && commodityNo.equals(dis.getCommodityNo()) && year == dis.getYear().intValue() && quarter.equals(dis.getQuarter())
                        && (dis.getStartDate() == null || dis.getStartDate().getTime() <= orderDate.getTime()) && ((dis.getEndDate() == null || dis.getEndDate().getTime() >= orderDate.getTime())))
                    return dis;
            }
            //如果没有匹配折扣，去除机构条件进行匹配
            for(Discount dis: discountList){
                if(dis.getPartB() == null && commodityNo.equals(dis.getCommodityNo()) && year == dis.getYear().intValue() && quarter.equals(dis.getQuarter())
                        && (dis.getStartDate() == null || dis.getStartDate().getTime() <= orderDate.getTime()) && ((dis.getEndDate() == null || dis.getEndDate().getTime() >= orderDate.getTime())))
                    return dis;

            }
        }

        //如果根据货号无法匹配，则去除货号条件进行匹配
        for(Discount dis: discountList){
            if((dis.getPartB() != null && dis.getPartB().getId() == orgId) && dis.getCommodityNo() == null && year == dis.getYear().intValue() && quarter.equals(dis.getQuarter())
                    && (dis.getStartDate() == null || dis.getStartDate().getTime() <= orderDate.getTime()) && ((dis.getEndDate() == null || dis.getEndDate().getTime() >= orderDate.getTime())))
                discount = dis;
        }

        if(discount == null){
            for(Discount dis: discountList){
                if((dis.getCommodityNo() == null) && dis.getPartB() == null && year == dis.getYear().intValue() && quarter.equals(dis.getQuarter())
                        && (dis.getStartDate() == null || dis.getStartDate().getTime() <= orderDate.getTime()) && ((dis.getEndDate() == null || dis.getEndDate().getTime() >= orderDate.getTime())) && dis.getPartB() == null) {
                    discount = dis;
                    break;
                }
            }
        }

        return discount;
    }

    public List<Discount> export(){
        return discountRepository.findAllByIsDeletedOrderByPartBAsc(false);
    }

    @Transactional
    public void importExcel(List<DiscountImportItem> list, UserClaim uc){
        StringBuffer errMsg = new StringBuffer();
        int i = 2;
        Date lastUptTime = new Date();
        List<Discount> result = new ArrayList<>();
        for(DiscountImportItem item: list){
            Discount discount = new Discount();
            if(!StringUtils.nonNull(item.getPartAName()) || "全部".equals(item.getPartAName()))
                errMsg.append(i).append("行数据错误，甲方名称不能为空或‘全部’!").append("<br>");
            else {
                Org partA = orgRepository.findByCompanyName(item.getPartAName());
                if(partA != null){
                    discount.setPartA(partA);
                } else
                    errMsg.append(i).append("行数据错误，甲方名称不正确!").append("<br>");
            }

            if(!StringUtils.nonNull(item.getPartBName()))
                errMsg.append(i).append("行数据错误，乙方名称不能为空!").append("<br>");
            else if( "全部".equals(item.getPartBName()))
                discount.setPartB(null);
            else {
                Org partB = orgRepository.findByCompanyName(item.getPartBName());
                if(partB != null){
                    discount.setPartB(partB);
                } else
                    errMsg.append(i).append("行数据错误，乙方名称不正确!").append("<br>");
            }

            if(!StringUtils.nonNull(item.getBrandName()))
                errMsg.append(i).append("行数据错误，品牌名称不能为空!").append("<br>");
            else {
                Brand brand = brandRepository.findByName(item.getBrandName());
                if(brand != null)
                    discount.setBrand(brand);
                else
                    errMsg.append(i).append("行数据错误，品牌名称不正确!").append("<br>");
            }

            if(item.getYear() != null && item.getYear().intValue() != 0){
                discount.setYear(item.getYear());
            } else
                errMsg.append(i).append("行数据错误，年份不正确!").append("<br>");

            if("Q1".equals(item.getQuarter()) || ("Q2").equals(item.getQuarter()) || ("Q3").equals(item.getQuarter()) || ("Q4").equals(item.getQuarter()))
                discount.setQuarter(item.getQuarter());
            else
                errMsg.append(i).append("行数据错误，季节不正确!").append("<br>");

            if(StringUtils.nonNull(item.getCommodityNo())) {
                if(storeCommodityRepository.countAllByCommodityNo(item.getCommodityNo()) == 0)
                    errMsg.append(i).append("行数据错误，货号不存在!").append("<br>");
                else
                    discount.setCommodityNo(item.getCommodityNo());
            }

            if(item.getStartDate() != null)
                discount.setStartDate(item.getStartDate());
            else
                errMsg.append(i).append("行数据错误，起始日期不能为空!").append("<br>");
            if(item.getEndDate() != null)
                discount.setEndDate(item.getEndDate());
            else
                errMsg.append(i).append("行数据错误，结束日期不能为空!").append("<br>");
            if(item.getDiscount() != null)
                discount.setDiscount(item.getDiscount());
            else
                errMsg.append(i).append("行数据错误，折扣不能为空!").append("<br>");
            discount.setRemarks(item.getRemarks());
            discount.setLastUpdatedBy(uc.getId());
            discount.setLastUpdatedTime(lastUptTime);
            result.add(discount);
            i++;
        }

        if(errMsg.length() > 0){
            throw new CredentialException(50001, errMsg.toString());
        } else {
            //将原来的折扣都设置为失效
            String sql = "update t_discount set is_deleted = true, last_updated_by = ?, last_updated_time = ?";
            Query query =  em.createNativeQuery(sql);
            query.setParameter(1, uc.getId());
            query.setParameter(2, lastUptTime);
            query.executeUpdate();
            discountRepository.saveAll(result);
        }
    }
}
