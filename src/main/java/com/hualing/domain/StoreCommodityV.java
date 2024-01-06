package com.hualing.domain;

import javax.persistence.*;

/**
 * Created by Will on 28/08/2019.
 */
@Entity
@Table(name = "v_store_commodity")
public class StoreCommodityV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "store_import_id")
    private StoreImport storeImport;

    @Column
    private String commodityNo;

    @Column
    private String category;

    @Column
    private String name;

    @Column
    private int year;

    @Column
    private String quarter;

    @Column
    private int listPrice;

    @Column
    private String gender;

    @Column
    private String size;

    @Column
    private int quantity;

    @Transient
    private Double discount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StoreImport getStoreImport() {
        return storeImport;
    }

    public void setStoreImport(StoreImport storeImport) {
        this.storeImport = storeImport;
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public int getListPrice() {
        return listPrice;
    }

    public void setListPrice(int listPrice) {
        this.listPrice = listPrice;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
