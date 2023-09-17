package com.pokemonreview.api.dto;

import lombok.Data;

@Data
public class UpdatePasswordDTO {
    private String username;
    private String newPassword;

    // Constructors, getters, and setters
}

