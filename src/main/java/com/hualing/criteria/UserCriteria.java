package com.hualing.criteria;

import com.hualing.common.PageableCriteria;
import com.hualing.domain.Org;
import com.hualing.domain.Org2User;
import com.hualing.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Will on 21/06/2019.
 */
public class UserCriteria  extends PageableCriteria {
    private Boolean isDeleted;

    private String account;

    private String name;

    private Long orgId;

    private String orgName;

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Override
    public <User> Specification<User> buildSpecification() {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new LinkedList<>();

            if(StringUtils.nonNull(isDeleted)){
                predicates.add(cb.equal(root.get("isDeleted").as(Boolean.class), isDeleted));
            }

            if(StringUtils.nonNull(name)){
                predicates.add(cb.like(root.get("name").as(String.class), "%" + name + "%"));
            }

            if(StringUtils.nonNull(account)){
                predicates.add(cb.like(root.get("account").as(String.class), "%" + account + "%"));
            }

            if(StringUtils.nonNull(orgId)){
                Join<User, Org> orgUserJoin = root.join("org", JoinType.INNER);
                predicates.add(cb.equal(orgUserJoin.get("id").as(Long.class), orgId));
            }

            if(StringUtils.nonNull(orgName)){
                Join<User, Org> orgUserJoin = root.join("org", JoinType.INNER);
                predicates.add(cb.like(orgUserJoin.get("companyName").as(String.class), "%" + orgName + "%"));
            }

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
}
