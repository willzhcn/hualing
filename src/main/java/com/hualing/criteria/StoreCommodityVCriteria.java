package com.hualing.criteria;

import com.hualing.common.PageableCriteria;
import com.hualing.domain.StoreImport;
import com.hualing.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Will on 28/08/2019.
 */
public class StoreCommodityVCriteria   extends PageableCriteria {
    String commodityNo;
    String sizeNo;
    String name;
    Integer year;
    String quarter;

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
    }

    public String getSizeNo() {
        return sizeNo;
    }

    public void setSizeNo(String sizeNo) {
        this.sizeNo = sizeNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    @Override
    public <StoreCommodityV> Specification<StoreCommodityV> buildSpecification() {
        return (Root<StoreCommodityV> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new LinkedList<>();
            Join<StoreCommodityV, StoreImport> importJoin = root.join(root.getModel().getSingularAttribute("storeImport", StoreImport.class), JoinType.INNER);


            if(StringUtils.nonNull(commodityNo)){
                predicates.add(cb.like(root.get("commodityNo").as(String.class), "%" + commodityNo + "%"));
            }

            if(StringUtils.nonNull(sizeNo)){
                predicates.add(cb.equal(root.get("size").as(String.class), sizeNo));
            }

            if(StringUtils.nonNull(name)){
                predicates.add(cb.like(root.get("name").as(String.class), "%" + name + "%"));
            }

            if(StringUtils.nonNull(year)){
                predicates.add(cb.equal(root.get("year").as(Integer.class), year));
            }

            if(StringUtils.nonNull(quarter)){
                predicates.add(cb.like(root.get("quarter").as(String.class), "%" + quarter + "%"));
            }

            query.orderBy(cb.desc(importJoin.get("loadTime").as(Date.class)));

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
}
