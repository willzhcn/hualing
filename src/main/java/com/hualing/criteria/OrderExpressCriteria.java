package com.hualing.criteria;

import com.hualing.common.PageableCriteria;
import com.hualing.domain.Express;
import com.hualing.domain.ExpressDistribute;
import com.hualing.domain.OrderDistribute;
import com.hualing.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Will on 05/08/2019.
 */

public class OrderExpressCriteria extends PageableCriteria {
    private String orderNo;
    private String expressNo;
    private Long expressId;
    private String receiver;
    private Long storeId;
    private String status;
    private Long orgId;
    private String commodityNo;
    private String sizeNo;
    private boolean expressNotRequired;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public Long getExpressId() {
        return expressId;
    }

    public void setExpressId(Long expressId) {
        this.expressId = expressId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public boolean isExpressNotRequired() {
        return expressNotRequired;
    }

    public void setExpressNotRequired(boolean expressNotRequired) {
        this.expressNotRequired = expressNotRequired;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
    }

    public String getSizeNo() {
        return sizeNo;
    }

    public void setSizeNo(String sizeNo) {
        this.sizeNo = sizeNo;
    }

    @Override
    public <OrderExpress> Specification<OrderExpress> buildSpecification() {
        return (Root<OrderExpress> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new LinkedList<>();

            if(StringUtils.nonNull(orderNo)){
                ListJoin<OrderExpress, ExpressDistribute> distributeJoin = root.joinList("distributeList", JoinType.LEFT);
                predicates.add(cb.like(distributeJoin.get("orderNo").as(String.class), "%" + orderNo + "%"));
            }

            if(StringUtils.nonNull(expressNo)){
                predicates.add(cb.equal(root.get("expressNo").as(String.class), expressNo));
            }

            if(StringUtils.nonNull(status)){
                predicates.add(cb.equal(root.get("status").as(String.class), status));
            }

            if(StringUtils.nonNull(expressId)){
                Join<OrderExpress, Express> expressJoin = root.join(root.getModel().getSingularAttribute("express", Express.class), JoinType.INNER);
                predicates.add(cb.equal(expressJoin.get("id").as(Long.class), expressId));
            }

            if(StringUtils.nonNull(storeId)){
                Join<OrderExpress, ExpressDistribute> expressJoin = root.join("distributeList", JoinType.INNER);
                predicates.add(cb.equal(expressJoin.get("orderDistribute").get("store").get("id").as(Long.class), storeId));
            }

            if(StringUtils.nonNull(orgId)){
                Join<OrderExpress, ExpressDistribute> expressJoin = root.join("distributeList", JoinType.INNER);
                predicates.add(cb.equal(expressJoin.get("orderDistribute").get("order").get("org").get("id").as(Long.class), orgId));
            }

            if(expressNotRequired){
                predicates.add(cb.isNotNull(root.get("storeId").as(Long.class)));
            }

            if(StringUtils.nonNull(receiver)){
                predicates.add(cb.like(root.get("receiver").as(String.class), "%" + receiver + "%"));
            }

            if(StringUtils.nonNull(commodityNo)){
                Join<OrderExpress, ExpressDistribute> expressJoin = root.join("distributeList", JoinType.INNER);
                predicates.add(cb.equal(expressJoin.get("orderDistribute").get("order").get("commodityNo").as(String.class), commodityNo));
            }

            if(StringUtils.nonNull(sizeNo)){
                Join<OrderExpress, ExpressDistribute> expressJoin = root.join("distributeList", JoinType.INNER);
                predicates.add(cb.equal(expressJoin.get("orderDistribute").get("order").get("size").as(String.class), sizeNo));
            }

            query.distinct(true);

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
}
