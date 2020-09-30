package com.hualing.criteria;

import com.hualing.common.PageableCriteria;
import com.hualing.domain.*;
import com.hualing.domain.Order;
import com.hualing.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Will on 12/08/2019.
 */
public class OrderRequestCriteria  extends PageableCriteria {
    private String orderNo;
    private String orgId;
    private String requester;
    private String status;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public <OrderRequest> Specification<OrderRequest> buildSpecification() {
        return (Root<OrderRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new LinkedList<>();

            if(StringUtils.nonNull(orderNo)){
                Join<OrderRequest, com.hualing.domain.Order> orderJoin = root.join(root.getModel().getSingularAttribute("order", Order.class), JoinType.INNER);
                predicates.add(cb.like(orderJoin.get("orderNo").as(String.class), "%" + orderNo + "%"));
            }

            if(StringUtils.nonNull(orgId)){
                Join<OrderRequest, Org> orgJoin = root.join(root.getModel().getSingularAttribute("org", Org.class), JoinType.INNER);
                predicates.add(cb.equal(orgJoin.get("id").as(Long.class), orgId));
            }

            if(StringUtils.nonNull(status)){
                predicates.add(cb.equal(root.get("status").as(String.class), status));
            }

            if(StringUtils.nonNull(requester)){
                Join<OrderRequest, User> requestJoin = root.join(root.getModel().getSingularAttribute("requestedBy", User.class), JoinType.INNER);
                predicates.add(cb.like(requestJoin.get("name").as(String.class), "%" + requester + "%"));
            }

            List<javax.persistence.criteria.Order> list = new ArrayList<>();
            list.add(cb.desc(root.get("requestedTime").as(Date.class)));
            list.add(cb.desc(root.get("id").as(Long.class)));
            query.orderBy(list);

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
}
