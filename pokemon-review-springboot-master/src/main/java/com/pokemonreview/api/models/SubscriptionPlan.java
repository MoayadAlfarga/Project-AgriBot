package com.pokemonreview.api.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "plan_name")
    private String planName;

    @Column(name = "plan_description")
    private String planDescription;
    @Column(name = "duration_days", nullable = false)

    private int durationDays;
    @Column(name = "price", nullable = false)
    private double price;


}
