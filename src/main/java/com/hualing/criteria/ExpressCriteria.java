package com.hualing.criteria;

import com.hualing.common.PageableCriteria;
import com.hualing.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Will on 07/07/2019.
 */
public class ExpressCriteria  extends PageableCriteria {
    String name;
    String category;
    Boolean isDeleted = false;

    @Override
    public <Express> Specification<Express> buildSpecification() {
        return (Root<Express> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new LinkedList<>();

            if(StringUtils.nonNull(isDeleted)){
                predicates.add(cb.equal(root.get("isDeleted").as(Boolean.class), isDeleted));
            }

            if(StringUtils.nonNull(name)){
                predicates.add(cb.like(root.get("name").as(String.class), "%" + name + "%"));
            }

            if(StringUtils.nonNull(category)){
                predicates.add(cb.equal(root.get("category").as(String.class), category));
            }

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
