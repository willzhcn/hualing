package com.hualing.criteria;

import com.hualing.common.PageableCriteria;
import com.hualing.domain.Brand;
import com.hualing.domain.Org;
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
public class StoreImportCriteria  extends PageableCriteria {
    Long orgId;
    Long brandId;
    Boolean isActive;


    @Override
    public <StoreImport> Specification<StoreImport> buildSpecification() {
        return (Root<StoreImport> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new LinkedList<>();

            if(StringUtils.nonNull(isActive)){
                predicates.add(cb.equal(root.get("isActive").as(Boolean.class), isActive));
            }

            if(StringUtils.nonNull(orgId)){
                Join<StoreImport, Org> orgJoin = root.join(root.getModel().getSingularAttribute("org", Org.class), JoinType.INNER);
                predicates.add(cb.equal(orgJoin.get("id").as(Long.class), orgId));
            }

            if(StringUtils.nonNull(brandId)){
                Join<StoreImport, Brand> brandJoin = root.join(root.getModel().getSingularAttribute("brand", Brand.class), JoinType.INNER);
                predicates.add(cb.equal(brandJoin.get("id").as(Long.class), brandId));
            }

            query.orderBy(cb.desc(root.get("loadTime").as(Date.class)));

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
