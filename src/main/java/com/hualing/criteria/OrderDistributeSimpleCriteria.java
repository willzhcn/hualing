package com.hualing.criteria;

import com.hualing.common.PageableCriteria;
import com.hualing.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Will on 03/09/2019.
 */
public class OrderDistributeSimpleCriteria  extends PageableCriteria {
    private Long orderId;


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }


    @Override
    public <OrderDistributeSimple> Specification<OrderDistributeSimple> buildSpecification() {
        return (Root<OrderDistributeSimple> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new LinkedList<>();

            if(StringUtils.nonNull(orderId)){
                predicates.add(cb.equal(root.get("orderId").as(Long.class), orderId));
            }

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
}
