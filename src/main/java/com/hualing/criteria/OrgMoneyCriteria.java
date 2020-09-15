package com.hualing.criteria;

import com.hualing.common.PageableCriteria;
import com.hualing.domain.Org;
import com.hualing.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Will on 25/07/2019.
 */
public class OrgMoneyCriteria extends PageableCriteria {
    private String companyName;

    private Long orgId;

    private Boolean isDeleted;

    @Override
    public <OrgMoney> Specification<OrgMoney> buildSpecification() {
        return (Root<OrgMoney> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new LinkedList<>();

            if(StringUtils.nonNull(isDeleted)){
                predicates.add(cb.equal(root.get("isDeleted").as(Boolean.class), isDeleted));
            }

            if(StringUtils.nonNull(companyName)){
                Join<OrgMoney, Org> orgJoin = root.join(root.getModel().getSingularAttribute("org", Org.class), JoinType.INNER);

                predicates.add(cb.like(orgJoin.get("companyName").as(String.class), "%" + companyName + "%"));
            }

            if(StringUtils.nonNull(orgId)){
                Join<OrgMoney, Org> orgJoin = root.join(root.getModel().getSingularAttribute("org", Org.class), JoinType.INNER);

                predicates.add(cb.equal(orgJoin.get("id").as(Long.class), orgId));
            }

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
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

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}
