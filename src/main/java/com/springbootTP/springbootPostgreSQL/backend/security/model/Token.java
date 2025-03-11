package com.springbootTP.springbootPostgreSQL.backend.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class Token {

    private String refreshToken;
    private String accessToken;


}
