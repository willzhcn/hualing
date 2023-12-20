package com.hualing.criteria;

import com.hualing.common.PageableCriteria;
import com.hualing.domain.Brand;
import com.hualing.domain.Org;
import com.hualing.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Will on 22/07/2019.
 */
public class DiscountCriteria  extends PageableCriteria {
    private Integer year;
    private String quarter;
    private Long partAId;
    private String partAName;
    private String partBName;
    private long partBId;
    private Long brandId;
    private Boolean isDeleted;
    private String commodityNo;

    @Override
    public <Discount> Specification<Discount> buildSpecification() {
        return (Root<Discount> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new LinkedList<>();

            if(StringUtils.nonNull(isDeleted)){
                predicates.add(cb.equal(root.get("isDeleted").as(Boolean.class), isDeleted));
            }

            if(StringUtils.nonNull(year)){
                predicates.add(cb.equal(root.get("year").as(Integer.class), year));
            }

            if(StringUtils.nonNull(quarter)){
                predicates.add(cb.equal(root.get("quarter").as(String.class), quarter));
            }

            if(StringUtils.nonNull(commodityNo)){
                predicates.add(cb.equal(root.get("commodityNo").as(String.class), commodityNo));
            }

            if(StringUtils.nonNull(partAId)){
                Join<Discount, Org> orgJoin = root.join(root.getModel().getSingularAttribute("partA", Org.class), JoinType.INNER);
                predicates.add(cb.equal(orgJoin.get("id").as(String.class), partAId));
            }

            if(StringUtils.nonNull(partAName)){
                Join<Discount, Org> orgJoin = root.join(root.getModel().getSingularAttribute("partA", Org.class), JoinType.INNER);
                predicates.add(cb.like(orgJoin.get("companyName").as(String.class), "%" + partAName + "%"));
            }

            if(StringUtils.nonNull(partBName)){
                Join<Discount, Org> orgJoin = root.join(root.getModel().getSingularAttribute("partB", Org.class), JoinType.INNER);
                predicates.add(cb.like(orgJoin.get("companyName").as(String.class), "%" + partBName + "%"));
            }

            if(StringUtils.nonNull(partBId)){
                Join<Discount, Org> orgJoin = root.join(root.getModel().getSingularAttribute("partB", Org.class), JoinType.INNER);
                predicates.add(cb.equal(orgJoin.get("id").as(String.class), partBId));
            }

            if(StringUtils.nonNull(brandId)){
                Join<Discount, Brand> brandJoin = root.join(root.getModel().getSingularAttribute("brand", Brand.class), JoinType.INNER);
                predicates.add(cb.equal(brandJoin.get("id").as(Long.class), brandId));
            }
            query.orderBy(cb.asc(root.get("isDeleted").as(Boolean.class)));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
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


    public String getPartAName() {
        return partAName;
    }

    public void setPartAName(String partAName) {
        this.partAName = partAName;
    }

    public String getPartBName() {
        return partBName;
    }

    public void setPartBName(String partBName) {
        this.partBName = partBName;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public long getPartBId() {
        return partBId;
    }

    public void setPartBId(long partBId) {
        this.partBId = partBId;
    }

    public Long getPartAId() {
        return partAId;
    }

    public void setPartAId(Long partAId) {
        this.partAId = partAId;
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
    }
}
