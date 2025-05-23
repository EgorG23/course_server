package com.hse.course.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@Entity
@Data
@Table(name = "gift_collections")
public class GiftCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "global_id", unique = true, nullable = false)
    private Long globalId;
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    private List<Long> gifts;

    @PrePersist
    public void generateGlobalId() {
        if (this.globalId == null) {
            this.globalId = System.currentTimeMillis();
        }
    }
}