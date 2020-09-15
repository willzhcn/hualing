package com.hualing.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Will on 09/10/2019.
 */
@Entity
@Table(name = "t_express_distribute")
public class ExpressDistribute2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "orderExpressId")
    private OrderExpress2 orderExpress;

    private String orderNo;

    private long distributeId;

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

    public OrderExpress2 getOrderExpress() {
        return orderExpress;
    }

    public void setOrderExpress(OrderExpress2 orderExpress) {
        this.orderExpress = orderExpress;
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

    public long getDistributeId() {
        return distributeId;
    }

    public void setDistributeId(long distributeId) {
        this.distributeId = distributeId;
    }
}
