package com.hualing.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Will on 07/07/2019.
 */
@Entity
@Table(name = "t_expresses")
public class Express {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private String category;

    @Column
    private Boolean exportSupported;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getExportSupported() {
        return exportSupported;
    }

    public void setExportSupported(Boolean exportSupported) {
        this.exportSupported = exportSupported;
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
