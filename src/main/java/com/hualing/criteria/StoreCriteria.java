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
 * Created by Will on 07/07/2019.
 */
public class StoreCriteria extends PageableCriteria {
    String name;
    Boolean expressRequired;
    Boolean isDeleted = false;

    @Override
    public <Store> Specification<Store> buildSpecification() {
        return (Root<Store> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new LinkedList<>();

            if(StringUtils.nonNull(isDeleted)){
                predicates.add(cb.equal(root.get("isDeleted").as(Boolean.class), isDeleted));
            }

            if(StringUtils.nonNull(name)){
                predicates.add(cb.like(root.get("name").as(String.class), "%" + name + "%"));
            }

            if(StringUtils.nonNull(expressRequired)){
                predicates.add(cb.equal(root.get("expressRequired").as(Boolean.class), expressRequired));
            }

            query.orderBy(cb.asc(root.get("orderNo")));

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getExpressRequired() {
        return expressRequired;
    }

    public void setExpressRequired(Boolean expressRequired) {
        this.expressRequired = expressRequired;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
