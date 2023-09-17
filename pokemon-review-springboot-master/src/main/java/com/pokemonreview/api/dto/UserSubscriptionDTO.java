package com.pokemonreview.api.dto;


import com.pokemonreview.api.models.SubscriptionPlan;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class UserSubscriptionDTO {
    private SubscriptionPlan subscriptionPlan;
    private LocalDateTime subscriptionStartDate;
    private LocalDateTime subscriptionEndDate;
    private String accountStatus;

    // Constructors, getters, and setters
}

