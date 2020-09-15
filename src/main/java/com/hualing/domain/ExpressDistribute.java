package com.hualing.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Will on 04/08/2019.
 */
@Entity
@Table(name = "t_express_distribute")
public class ExpressDistribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long  orderExpressId;

    private String orderNo;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "distributeId")
    private OrderDistribute orderDistribute;

    private int quantity;

    @Column(name = "last_updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedTime;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderExpressId() {
        return orderExpressId;
    }

    public void setOrderExpressId(long orderExpressId) {
        this.orderExpressId = orderExpressId;
    }

    public OrderDistribute getOrderDistribute() {
        return orderDistribute;
    }

    public void setOrderDistribute(OrderDistribute orderDistribute) {
        this.orderDistribute = orderDistribute;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
