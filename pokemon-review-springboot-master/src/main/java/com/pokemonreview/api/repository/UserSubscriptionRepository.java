package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.SubscriptionPlan;
import com.pokemonreview.api.models.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription,Integer> {
    List<UserSubscription> findByUser_Id(Integer id);
    List<UserSubscription> findByUser_IdAndAccountStatus(Integer userId, String accountStatus);


}
