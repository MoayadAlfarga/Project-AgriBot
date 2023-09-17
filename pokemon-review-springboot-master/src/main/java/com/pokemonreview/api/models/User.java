package com.pokemonreview.api.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Email
    @Column(nullable = false, unique = true)
    private String username;
    @Pattern(
              regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
              message = "Invalid password. It must contain at least 8 characters, including at least one digit, one lowercase letter, one uppercase letter, and one special character."
    )
    @Column
    private String password;
    @NotNull(message = "Name Cannot not null!")
    @NotBlank(message = "Name Cannot not Blank!")
    @Column(name = "first_name")
    private String firstName;
    @NotNull(message = "Name Cannot not null!")
    @NotBlank(message = "Name Cannot not Blank!")
    @Column(name = "last_name")
    private String lastName;
    @NotNull(message = "Phone Cannot not null!")
    @NotBlank(message = "Phone Cannot not Blank!")
    @Column(name = "phone_number")
    private String phoneNumber;
    @NotNull(message = "Location Cannot not null!")
    @NotBlank(message = "Location Cannot not Blank!")
    @Column
    private String location;
    @Column
    private String role;
}
