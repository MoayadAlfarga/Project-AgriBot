package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.SubscriptionPlan;
import com.pokemonreview.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
    SubscriptionPlan findByPlanName(String planName);


}
