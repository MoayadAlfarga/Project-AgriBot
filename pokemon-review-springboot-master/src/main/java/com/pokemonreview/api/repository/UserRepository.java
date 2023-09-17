package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.User;
import com.pokemonreview.api.models.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);






}
