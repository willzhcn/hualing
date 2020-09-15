package com.hualing.criteria;

import com.hualing.common.PageableCriteria;
import com.hualing.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Will on 15/07/2019.
 */
public class BarCodeCriteria  extends PageableCriteria {
    String commodityNoSize;
    String barcode;
    String entireCode;
    Boolean isDeleted;

    @Override
    public <BarCode> Specification<BarCode> buildSpecification() {
        return (Root<BarCode> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new LinkedList<>();

            if(StringUtils.nonNull(isDeleted)){
                predicates.add(cb.equal(root.get("isDeleted").as(Boolean.class), isDeleted));
            }

            if(StringUtils.nonNull(commodityNoSize)){
                predicates.add(cb.like(root.get("commodityNoSize").as(String.class), "%" + commodityNoSize + "%"));
            }

            if(StringUtils.nonNull(barcode)){
                predicates.add(cb.like(root.get("barcode").as(String.class), "%" + barcode + "%"));
            }

            if(StringUtils.nonNull(entireCode)){
                predicates.add(cb.equal(root.get("barcode").as(String.class), entireCode));
            }

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }

    public String getCommodityNoSize() {
        return commodityNoSize;
    }

    public void setCommodityNoSize(String commodityNoSize) {
        this.commodityNoSize = commodityNoSize;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getEntireCode() {
        return entireCode;
    }

    public void setEntireCode(String entireCode) {
        this.entireCode = entireCode;
    }
}
