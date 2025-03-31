package com.springbootTP.springbootPostgreSQL.backend.security.service;

import com.springbootTP.springbootPostgreSQL.backend.security.model.Role;
import com.springbootTP.springbootPostgreSQL.backend.security.model.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface JwtServiceInterface extends UserDetailsService {

    Users registerUser(String email, String username, String password, Role role);
}