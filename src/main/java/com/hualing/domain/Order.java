package com.hualing.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Will on 22/07/2019.
 */
@Entity
@Table(name = "t_order")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "org_id")
    private Org org;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id")
    private Brand brand;
    @Column
    private String orderNo;
    @Column(name = "order_date")
    @Temporal(TemporalType.DATE)
    private Date orderDate;

    @Temporal(TemporalType.DATE)
    private Date createDate;
    @Column
    private String category;
    @Column
    private String commodityNo;
    @Column
    private String size;
    @Column
    private Integer year;
    @Column
    private String quarter;
    @Column
    private Double discount;
    @Column
    private Double specialDiscount;
    @Column
    private Double price;
    @Column
    private Integer quantity;
    @Column
    private String receiver;
    @Column
    private String receiverPhone;
    @Column
    private String receiverAddress;
    @Column
    private String express;
    @Column
    private String expressNo;
    @Column
    private String status;

    private Integer backCount = 0;

    private String comments;
    @Transient
    private int currentBackCount = 0;

    @Column(name = "last_updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedTime;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Transient
    private List<OrderDistribute> distributeList;

    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name = "orderNo", referencedColumnName = "orderNo")
    private Set<ExpressDistribute2> expressDistributeList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
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

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<OrderDistribute> getDistributeList() {
        return distributeList;
    }

    public void setDistributeList(List<OrderDistribute> distributeList) {
        this.distributeList = distributeList;
    }

    public Integer getBackCount() {
        return backCount;
    }

    public void setBackCount(int backCount) {
        this.backCount = backCount;
    }

    public int getCurrentBackCount() {
        return currentBackCount;
    }

    public void setCurrentBackCount(int currentBackCount) {
        this.currentBackCount = currentBackCount;
    }

    public void setBackCount(Integer backCount) {
        this.backCount = backCount;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Double getSpecialDiscount() {
        return specialDiscount;
    }

    public void setSpecialDiscount(Double specialDiscount) {
        this.specialDiscount = specialDiscount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Set<ExpressDistribute2> getExpressDistributeList() {
        return expressDistributeList;
    }

    public void setExpressDistributeList(Set<ExpressDistribute2> expressDistributeList) {
        this.expressDistributeList = expressDistributeList;
    }
}
