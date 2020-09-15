package com.hualing.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * Created by dipengfei on 8/9/16.
 */
public class UserClaim {

    private static final String JWT_KEY = "JWT-TOKEN-SECRET";

    private String account;

    private long id;
    
    private String role="unknown";
    
    private long roleId = 0;  
    
    private String org = "unknown";
    
    private long orgId = 0;

    private String orgType = "unknown";
    
    private long storeId = 0;

    private Date issuedAt;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }   
    

    public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }


    public String toToken() {

        String token = Jwts.builder().setSubject(this.account)
                .claim("role", this.role).claim("id", this.id).claim("roleId", this.roleId).claim("org", this.org).claim("orgId", this.orgId).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, JWT_KEY).compact();

        return token;

    }

    public static UserClaim fromToken(String token) {

        Claims claims = Jwts.parser().setSigningKey(JWT_KEY)
                .parseClaimsJws(token).getBody();

        UserClaim uc = new UserClaim();
        uc.setAccount(claims.getSubject());
        uc.setId(Long.valueOf(claims.get("id").toString()));
        uc.setRole(claims.get("role").toString());
        uc.setRoleId(Long.valueOf(claims.get("roleId").toString()));
        uc.setOrg(claims.get("org").toString());
        uc.setOrgId(Long.valueOf(claims.get("orgId").toString()));
        uc.setIssuedAt(claims.getIssuedAt());
        return uc;
    }


}
