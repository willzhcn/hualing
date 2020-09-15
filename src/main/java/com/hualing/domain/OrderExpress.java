package com.hualing.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Will on 04/08/2019.
 */
@Entity
@Table(name = "t_order_express")
public class OrderExpress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String expressNo;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "express_id")
    private Express express;

    private String receiver;

    private String receiverPhone;

    private String receiverAddress;

    private String status;

    private long storeId;

    @Column(name = "last_updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedTime;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name = "orderExpressId")
    private List<ExpressDistribute> distributeList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<ExpressDistribute> getDistributeList() {
        return distributeList;
    }

    public void setDistributeList(List<ExpressDistribute> distributeList) {
        this.distributeList = distributeList;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public Express getExpress() {
        return express;
    }

    public void setExpress(Express express) {
        this.express = express;
    }
}
