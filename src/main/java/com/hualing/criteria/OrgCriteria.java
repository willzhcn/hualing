package com.hualing.criteria;

import com.hualing.common.PageableCriteria;
import com.hualing.domain.OrgType;
import com.hualing.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Will on 21/06/2019.
 */
public class OrgCriteria extends PageableCriteria {

    private Long orgType;

    private String companyName;

    private Boolean isDeleted;

    private String orgTypeStr;

    public Long getOrgType() {
        return orgType;
    }

    public void setOrgType(Long orgType) {
        this.orgType = orgType;
    }

    public String getOrgTypeStr() {
        return orgTypeStr;
    }

    public void setOrgTypeStr(String orgTypeStr) {
        this.orgTypeStr = orgTypeStr;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public <Org> Specification<Org> buildSpecification() {
        return (Root<Org> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new LinkedList<>();

            if(StringUtils.nonNull(isDeleted)){
                predicates.add(cb.equal(root.get("isDeleted").as(Boolean.class), isDeleted));
            }

            if(StringUtils.nonNull(companyName)){
                predicates.add(cb.like(root.get("companyName").as(String.class), "%" + companyName + "%"));
            }

            if(StringUtils.nonNull(orgType)){
                Join<Org, OrgType> orgTypeJoin = root.join(root.getModel().getSingularAttribute("orgType", OrgType.class), JoinType.INNER);
                predicates.add(cb.equal(orgTypeJoin.get("id").as(Long.class), orgType));
            }

            if(StringUtils.nonNull(orgTypeStr)){
                Join<Org, OrgType> orgTypeJoin = root.join(root.getModel().getSingularAttribute("orgType", OrgType.class), JoinType.INNER);
                predicates.add(cb.equal(orgTypeJoin.get("type").as(String.class), orgTypeStr));
            }

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
}
