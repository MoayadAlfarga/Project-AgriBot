package com.pokemonreview.api.models;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
@Data
@Entity
@Table
public class UserSubscription {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "subscription_plan_id")
    private SubscriptionPlan subscriptionPlan;

    @Column(name = "subscription_start_date", nullable = false)
    private LocalDateTime subscriptionStartDate;

    @Column(name = "subscription_end_date", nullable = false)
    private LocalDateTime subscriptionEndDate;

    @Column(name = "account_status", nullable = false)
    private String accountStatus; // "active," "inactive," etc.



}
