package com.springbootTP.springbootPostgreSQL.backend.security.service;

import com.springbootTP.springbootPostgreSQL.backend.security.model.Role;
import com.springbootTP.springbootPostgreSQL.backend.security.model.Users;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface JwtServiceInterface {
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
    Users registerUser(String email, String username, String password, Role role);
}
