package com.hse.course.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loyalty_cards")
public class LoyaltyCard {
    @Id
    private Long globalId;
    private Long userId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @PrePersist
    public void generateGlobalId() {
        if (this.globalId == null) {
            this.globalId = System.currentTimeMillis();
        }
    }
}