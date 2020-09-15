package com.hualing.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Will on 22/07/2019.
 */
@Entity
@Table(name = "t_discount")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "parta_id")
    private Org partA;

    @OneToOne
    @JoinColumn(name = "partb_id")
    private Org partB;

    @OneToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column
    private Integer year;
    @Column
    private String quarter;

    @Column
    private Double discount;

    @Column
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column
    @Temporal(TemporalType.DATE)
    private Date endDate;

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

    public Org getPartA() {
        return partA;
    }

    public void setPartA(Org partA) {
        this.partA = partA;
    }

    public Org getPartB() {
        return partB;
    }

    public void setPartB(Org partB) {
        this.partB = partB;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
