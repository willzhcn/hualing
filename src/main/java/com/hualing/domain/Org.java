package com.hualing.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_org")
public class Org {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "org_type")
	private OrgType orgType;

	@Column(name = "company_name")
	private String companyName;

	@Column
	private String contact;

	@Column
	private String remarks;
	@Column
	private String expressSender;
	@Column
	private String expressAddress;

	@Column
	private String expressPhone;

	@Column
	private String shortName;

	@Column(name = "last_updated_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdatedTime;

	@Column(name = "last_updated_by")
	private Long lastUpdatedBy;

	@Column(name = "isDeleted", columnDefinition = "INT(1)")
	private boolean isDeleted = false;

	private String dh;

	private String bh;

	private String thdh;

	private String thbh;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public OrgType getOrgType() {
		return orgType;
	}

	public void setOrgType(OrgType orgType) {
		this.orgType = orgType;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public long getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean deleted) {
		isDeleted = deleted;
	}

	public String getExpressSender() {
		return expressSender;
	}

	public void setExpressSender(String expressSender) {
		this.expressSender = expressSender;
	}

	public String getExpressAddress() {
		return expressAddress;
	}

	public void setExpressAddress(String expressAddress) {
		this.expressAddress = expressAddress;
	}

	public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getExpressPhone() {
		return expressPhone;
	}

	public void setExpressPhone(String expressPhone) {
		this.expressPhone = expressPhone;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getDh() {
		return dh;
	}

	public void setDh(String dh) {
		this.dh = dh;
	}

	public String getBh() {
		return bh;
	}

	public void setBh(String bh) {
		this.bh = bh;
	}

	public String getThdh() {
		return thdh;
	}

	public void setThdh(String thdh) {
		this.thdh = thdh;
	}

	public String getThbh() {
		return thbh;
	}

	public void setThbh(String thbh) {
		this.thbh = thbh;
	}

	@Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
	
}
