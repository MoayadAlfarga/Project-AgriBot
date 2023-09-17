package com.pokemonreview.api.controllers;

import com.pokemonreview.api.dto.ChooseSubscriptionRequest;
import com.pokemonreview.api.dto.UserDataDTO;
import com.pokemonreview.api.dto.UserSubscriptionDTO;
import com.pokemonreview.api.models.SubscriptionPlan;
import com.pokemonreview.api.models.User;
import com.pokemonreview.api.models.UserSubscription;
import com.pokemonreview.api.repository.SubscriptionPlanRepository;
import com.pokemonreview.api.repository.UserRepository;
import com.pokemonreview.api.repository.UserSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    private UserRepository userRepository;
    private SubscriptionPlanRepository subscriptionPlanRepository;
    private UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    public UserController(UserRepository userRepository,
                          SubscriptionPlanRepository subscriptionPlanRepository,
                          UserSubscriptionRepository userSubscriptionRepository) {
        this.userRepository = userRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.userSubscriptionRepository = userSubscriptionRepository;
    }

    @GetMapping("/getUserSubscriptions/{userId}")
    public ResponseEntity<?> getUserSubscriptions(@PathVariable Integer userId) {
        // Retrieve all user subscriptions for the given user ID
        List<UserSubscription> userSubscriptions = userSubscriptionRepository.findByUser_Id(userId);

        if (!userSubscriptions.isEmpty()) {
            // Create a list of UserSubscriptionDTO objects to hold the desired data
            List<UserSubscriptionDTO> userSubscriptionDTOs = new ArrayList<>();

            for (UserSubscription userSubscription : userSubscriptions) {
                UserSubscriptionDTO dto = new UserSubscriptionDTO();
                dto.setSubscriptionPlan(userSubscription.getSubscriptionPlan());
                dto.setSubscriptionStartDate(userSubscription.getSubscriptionStartDate());
                dto.setSubscriptionEndDate(userSubscription.getSubscriptionEndDate());
                dto.setAccountStatus(userSubscription.getAccountStatus());

                userSubscriptionDTOs.add(dto);
            }

            return new ResponseEntity<>(userSubscriptionDTOs, HttpStatus.OK);
        } else {
            // Handle the case where no user subscriptions are found
            return new ResponseEntity<>("User not active", HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/getUserData/{userId}")
    public ResponseEntity<UserDataDTO> getUserData(@PathVariable Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            UserDataDTO userDataDTO = new UserDataDTO();
            userDataDTO.setFirstName(user.getFirstName());
            userDataDTO.setLastName(user.getLastName());
            userDataDTO.setUsername(user.getUsername());
            userDataDTO.setLocation(user.getLocation());
            userDataDTO.setPhoneNumber(user.getPhoneNumber());

            return new ResponseEntity<>(userDataDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateUserData/{userId}")
    public ResponseEntity<UserDataDTO> updateUserData(@PathVariable Integer userId, @RequestBody UserDataDTO userDataDTO) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Update the user data with values from the UserDataDTO
            user.setFirstName(userDataDTO.getFirstName());
            user.setLastName(userDataDTO.getLastName());
//            user.setUsername(userDataDTO.getUsername());
            user.setLocation(userDataDTO.getLocation());
            user.setPhoneNumber(userDataDTO.getPhoneNumber());

            // Save the updated user
            userRepository.save(user);

            return new ResponseEntity<>(userDataDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/choose-subscription") /////      choose-subscription Step 2  ///   Enter username and Select package
    public ResponseEntity<String> chooseSubscription(@RequestBody ChooseSubscriptionRequest request) {
        // Find the user by their ID (you can adjust this based on your authentication system)
        User user = userRepository.findByUsername(request.getUsername())
                  .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Find the selected subscription plan by its name
        SubscriptionPlan selectedPlan = subscriptionPlanRepository.findByPlanName(request.getSelectedPackage());
        if (selectedPlan == null) {
            return new ResponseEntity<>("Invalid subscription package.", HttpStatus.BAD_REQUEST);
        }

        // Calculate the subscription end date based on the selected package's duration
        LocalDateTime subscriptionEndDate = LocalDateTime.now().plusDays(selectedPlan.getDurationDays());

        // Create a UserSubscription entry
        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUser(user);
        userSubscription.setSubscriptionPlan(selectedPlan);
        userSubscription.setSubscriptionStartDate(LocalDateTime.now());
        userSubscription.setSubscriptionEndDate(subscriptionEndDate);
        userSubscription.setAccountStatus("active"); // Set to "active" upon selection
        // Save the UserSubscription
        userSubscriptionRepository.save(userSubscription);
        return new ResponseEntity<>("Subscription selected successfully.", HttpStatus.OK);
    }

}

