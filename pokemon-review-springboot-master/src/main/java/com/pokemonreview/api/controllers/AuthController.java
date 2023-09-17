package com.pokemonreview.api.controllers;

import com.pokemonreview.api.dto.*;
import com.pokemonreview.api.models.User;
import com.pokemonreview.api.repository.SubscriptionPlanRepository;
import com.pokemonreview.api.repository.UserRepository;
import com.pokemonreview.api.repository.UserSubscriptionRepository;
import com.pokemonreview.api.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;

    private SubscriptionPlanRepository subscriptionPlanRepository;
    private UserSubscriptionRepository userSubscriptionRepository;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator,
                          SubscriptionPlanRepository subscriptionPlanRepository, UserSubscriptionRepository userSubscriptionRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.userSubscriptionRepository = userSubscriptionRepository;

    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                      new UsernamePasswordAuthenticationToken(
                                loginDto.getUsername(),
                                loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);
            return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
        } catch (BadCredentialsException badCredentialsException) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/login/Admin") /// This End Point Login Admin // one Admin
    public ResponseEntity<?> loginAdmin(@RequestBody LoginDto loginDtoAdmin) {
        try {


            Authentication authentication = authenticationManager.authenticate(
                      new UsernamePasswordAuthenticationToken(
                                loginDtoAdmin.getUsername(),
                                loginDtoAdmin.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Check the role of the authenticated user
            String userRole = getUserRole(authentication);

            if ("admin".equals(userRole.toLowerCase())) {
                String token = jwtGenerator.generateToken(authentication);
                return new ResponseEntity<>(new AuthResponseDTOAdmin(token), HttpStatus.OK);
            } else {
                // Handle the case where the user is not an admin
                return new ResponseEntity<>("Invalid username or password or not Admin ", HttpStatus.UNAUTHORIZED);
            }
        } catch (BadCredentialsException badCredentialsException) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }

    private String getUserRole(Authentication authentication) {
        // You need to implement logic to fetch the user's role from your database or user details
        // For example, if using Spring Security, you can access the user's authorities
        // and find their role.
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                return "admin";
            }
        }

        return "user"; // Default to "user" role if the user is not an admin
    }


    @PostMapping("/register")
    ///// signUp Step 1  //// Enter Data username,Password,firstname, lastname,phoneNumber,location
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setPhoneNumber(registerDto.getPhoneNumber());
        user.setLocation(registerDto.getLocation());
        user.setRole("User");
        userRepository.save(user);
        System.out.println("Sing Up data User " + user);
        return new ResponseEntity<>("Created Successfully  User", HttpStatus.OK);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        String username = updatePasswordDTO.getUsername(); // Include username in UpdatePasswordDTO

        User user = userRepository.findByUsername(username).orElse(null);

        if (user != null) {
            // Update the user's password with the new password
            user.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));

            // Save the updated user
            userRepository.save(user);

            return new ResponseEntity<>("update password Successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid Username!", HttpStatus.NOT_FOUND);
        }
    }


}
