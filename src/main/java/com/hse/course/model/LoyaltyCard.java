package com.hse.course.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "loyalty_cards")
public class LoyaltyCard {
    @Id
    private Long globalId;
    @Enumerated(EnumType.STRING)
    private LoyaltyLevel loyaltyLevel;

    public Long getGlobalId() {
        return globalId;
    }

    public void setGlobalId(Long globalId) {
        this.globalId = globalId;
    }

    public LoyaltyLevel getLoyaltyLevel() {
        return loyaltyLevel;
    }

    public void setLoyaltyLevel(LoyaltyLevel loyaltyLevel) {
        this.loyaltyLevel = loyaltyLevel;
    }
}