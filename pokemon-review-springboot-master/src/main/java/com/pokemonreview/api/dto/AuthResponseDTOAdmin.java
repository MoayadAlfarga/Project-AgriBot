package com.pokemonreview.api.dto;

import lombok.Data;

@Data
public class AuthResponseDTOAdmin {
    private String accessToken;
    private String tokenType = "Bearer ";

    public AuthResponseDTOAdmin(String accessToken) {
        this.accessToken = accessToken;

    }
}
