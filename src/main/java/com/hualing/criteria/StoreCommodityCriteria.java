package com.hualing.criteria;

import com.hualing.common.PageableCriteria;
import com.hualing.domain.Store;
import com.hualing.domain.StoreImport;
import com.hualing.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Will on 19/07/2019.
 */
public class StoreCommodityCriteria  extends PageableCriteria {
    Long storeId;
    String commodityNo;
    String sizeNo;
    String name;
    Integer year;
    String quarter;
    Boolean isActive;

    @Override
    public <StoreCommodity> Specification<StoreCommodity> buildSpecification() {
        return (Root<StoreCommodity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new LinkedList<>();
            Join<StoreCommodity, StoreImport> importJoin = root.join(root.getModel().getSingularAttribute("storeImport", StoreImport.class), JoinType.INNER);
            if(StringUtils.nonNull(isActive)){

                predicates.add(cb.equal(importJoin.get("isActive").as(Boolean.class), isActive));
            }

            if(StringUtils.nonNull(storeId)){
                Join<StoreCommodity, Store> storeJoin = root.join(root.getModel().getSingularAttribute("store", Store.class), JoinType.INNER);
                predicates.add(cb.equal(storeJoin.get("id").as(Long.class), storeId));
            }

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

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSizeNo() {
        return sizeNo;
    }

    public void setSizeNo(String sizeNo) {
        this.sizeNo = sizeNo;
    }
}
