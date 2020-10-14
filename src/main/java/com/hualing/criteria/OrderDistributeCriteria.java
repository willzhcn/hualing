package com.hualing.criteria;

import com.hualing.common.PageableCriteria;
import com.hualing.domain.Order;
import com.hualing.domain.Org;
import com.hualing.domain.Store;
import com.hualing.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Will on 01/08/2019.
 */
public class OrderDistributeCriteria  extends PageableCriteria {
    private Long orderId;
    private String orderNo;
    private Long storeId;
    private String status;
    private Boolean expressRequired;
    private String date;


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getExpressRequired() {
        return expressRequired;
    }

    public void setExpressRequired(Boolean expressRequired) {
        this.expressRequired = expressRequired;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public <OrderDistribute> Specification<OrderDistribute> buildSpecification() {
        return (Root<OrderDistribute> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new LinkedList<>();
            Join<OrderDistribute, Order> orderJoin = root.join(root.getModel().getSingularAttribute("order", Order.class), JoinType.INNER);
            if(StringUtils.nonNull(orderId)){
                predicates.add(cb.equal(orderJoin.get("id").as(Long.class), orderId));
            }

            if(StringUtils.nonNull(orderNo)){
                predicates.add(cb.like(orderJoin.get("orderNo").as(String.class), "%" + orderNo + "%"));
            }

            if(StringUtils.nonNull(storeId)){
                Join<OrderDistribute, Store> storeJoin = root.join(root.getModel().getSingularAttribute("store", Store.class), JoinType.INNER);
                predicates.add(cb.equal(storeJoin.get("id").as(Long.class), storeId));
            }

            if(StringUtils.nonNull(expressRequired)){
                Join<OrderDistribute, Store> storeJoin = root.join(root.getModel().getSingularAttribute("store", Store.class), JoinType.INNER);
                predicates.add(cb.equal(storeJoin.get("expressRequired").as(Boolean.class), expressRequired));
            }

            if(StringUtils.nonNull(status)){
                predicates.add(cb.equal(root.get("status").as(String.class), status));
            }

            if(StringUtils.nonNull(date)){

                predicates.add(cb.greaterThanOrEqualTo(orderJoin.get("orderDate").as(String.class), date));
                predicates.add(cb.lessThanOrEqualTo(orderJoin.get("orderDate").as(String.class), date + " 23:59:59"));
            }

            List list = new ArrayList();
            list.add(cb.asc(orderJoin.get("org").as(Org.class)));
            list.add(cb.desc(root.get("id").as(Long.class)));
            query.orderBy(list);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
}
