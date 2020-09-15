package com.hualing.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Will on 15/07/2019.
 */
@Entity
@Table(name = "t_barcode")
public class BarCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column
    private String commodityNoSize;
    @Column
    private String barcode;

    @Column
    private String remarks;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedTime;

    @Column
    private Long lastUpdatedBy;

    @Column(name = "isDeleted", columnDefinition = "INT(1)")
    private boolean isDeleted = false;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getCommodityNoSize() {
        return commodityNoSize;
    }

    public void setCommodityNoSize(String commodityNoSize) {
        this.commodityNoSize = commodityNoSize;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
