package com.hualing.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Will on 19/07/2019.
 */
@Entity
@Table(name = "t_store_import")
public class StoreImport {
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
    private String fileName;

    @Column
    private String remarks;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date loadTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "load_by")
    private User loadBy;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedTime;

    @Column
    private Long lastUpdatedBy;

    @Transient
    private List<StoreCommodity> dataList;

    @Column(name = "is_active", columnDefinition = "INT(1)")
    private boolean isActive = false;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(Date loadTime) {
        this.loadTime = loadTime;
    }

    public User getLoadBy() {
        return loadBy;
    }

    public void setLoadBy(User loadBy) {
        this.loadBy = loadBy;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<StoreCommodity> getDataList() {
        return dataList;
    }

    public void setDataList(List<StoreCommodity> dataList) {
        this.dataList = dataList;
    }
}
