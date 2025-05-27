package com.hse.course.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    private Long globalId;
    private Long userId;
    private Long idAdvertisement;
    private Integer amount;
    private String address;
    private Boolean isByCash;


    public Long getGlobalId() {
        return globalId;
    }

    public void setGlobalId(Long globalId) {
        this.globalId = globalId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getIdAdvertisement() {
        return idAdvertisement;
    }

    public void setIdAdvertisement(Long idAdvertisement) {
        this.idAdvertisement = idAdvertisement;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getByCash() {
        return isByCash;
    }

    public void setByCash(Boolean byCash) {
        isByCash = byCash;
    }
}