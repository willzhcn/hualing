package com.hualing.criteria;

import com.hualing.common.PageableCriteria;
import com.hualing.domain.Org;
import com.hualing.util.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Will on 30/08/2019.
 */
public class OrgMoneyDetailCriteria  extends PageableCriteria {
    private Long orgId;

    private String dealType;

    private Boolean isDeleted;

    private String startDate;

    private String endDate;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    @Override
    public <OrgMoneyDetail> Specification<OrgMoneyDetail> buildSpecification() {
        return (Root<OrgMoneyDetail> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new LinkedList<>();

            if(StringUtils.nonNull(isDeleted)){
                predicates.add(cb.equal(root.get("isDeleted").as(Boolean.class), isDeleted));
            }


            if(StringUtils.nonNull(orgId)){
                Join<OrgMoneyDetail, Org> orgJoin = root.join(root.getModel().getSingularAttribute("org", Org.class), JoinType.INNER);

                predicates.add(cb.equal(orgJoin.get("id").as(Long.class), orgId));
            }

            if(StringUtils.nonNull(startDate)){
                predicates.add(cb.greaterThanOrEqualTo(root.get("dealTime").as(String.class), startDate + " 00:00:00"));
            }

            if(StringUtils.nonNull(endDate)){
                predicates.add(cb.lessThanOrEqualTo(root.get("dealTime").as(String.class), endDate + " 23:59:59"));
            }

            if(StringUtils.nonNull(dealType)){
                predicates.add(cb.equal(root.get("dealType").as(String.class), dealType));
            }
            List<Order> list = new ArrayList<Order>();
            list.add(cb.desc(root.get("dealTime").as(Date.class)));
            list.add(cb.desc(root.get("id").as(Long.class)));
            query.orderBy(list);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
}
