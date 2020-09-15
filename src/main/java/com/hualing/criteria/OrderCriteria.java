package com.hualing.criteria;

import com.hualing.common.PageableCriteria;
import com.hualing.domain.Brand;
import com.hualing.domain.Org;
import com.hualing.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.TemporalType;
import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Will on 22/07/2019.
 */
public class OrderCriteria  extends PageableCriteria {
    private Long orgId;
    private Long brandId;
    private String orderNo;
    private String expressNo;
    private String commodityNo;
    private String sizeNo;
    private String receiver;
    private String receiverPhone;
    private String status;
    private String[] statuses;
    private Date orderDate;


    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String[] getStatuses() {
        return statuses;
    }

    public void setStatuses(String[] statuses) {
        this.statuses = statuses;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getSizeNo() {
        return sizeNo;
    }

    public void setSizeNo(String sizeNo) {
        this.sizeNo = sizeNo;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    @Override
    public <Order> Specification<Order> buildSpecification() {
        return (Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new LinkedList<>();

            if(StringUtils.nonNull(orgId)){
                Join<Order, Org> orgJoin = root.join(root.getModel().getSingularAttribute("org", Org.class), JoinType.INNER);
                predicates.add(cb.equal(orgJoin.get("id").as(Long.class), orgId));
            }

            if(StringUtils.nonNull(brandId)){
                Join<Order, Brand> brandJoin = root.join(root.getModel().getSingularAttribute("brand", Brand.class), JoinType.INNER);
                predicates.add(cb.equal(brandJoin.get("id").as(Long.class), brandId));
            }

            if(StringUtils.nonNull(orderNo)){
                predicates.add(cb.like(root.get("orderNo").as(String.class), "%" + orderNo + "%"));
            }

            if(StringUtils.nonNull(expressNo)){
                predicates.add(cb.like(root.get("expressNo").as(String.class), "%" + expressNo + "%"));
            }

            if(StringUtils.nonNull(receiver)){
                predicates.add(cb.like(root.get("receiver").as(String.class), "%" + receiver + "%"));
            }

            if(StringUtils.nonNull(receiverPhone)){
                predicates.add(cb.like(root.get("receiverPhone").as(String.class), "%" + receiverPhone + "%"));
            }

            if(StringUtils.nonNull(commodityNo)){
                predicates.add(cb.like(root.get("commodityNo").as(String.class), "%" + commodityNo + "%"));
            }

            if(StringUtils.nonNull(sizeNo)){
                predicates.add(cb.equal(root.get("size").as(String.class), sizeNo));
            }

            if(StringUtils.nonNull(status)){
                predicates.add(cb.equal(root.get("status").as(String.class), status));
            }

            if(StringUtils.nonNull(orderDate)){

                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                predicates.add(cb.equal(root.get("orderDate").as(String.class), sf.format(orderDate) + " 00:00:00"));
            }

            if(statuses != null && statuses.length > 0){
                Predicate[] array = new Predicate[statuses.length];
                for(int i = 0; i < array.length; i++){
                    array[i] = cb.equal(root.get("status").as(String.class), statuses[i]);
                }

                predicates.add(cb.or(array));
            }
            List list = new ArrayList();
            list.add(cb.desc(root.get("orderDate").as(Date.class)));
            list.add(cb.desc(root.get("id").as(Long.class)));
            query.orderBy(list);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
}
