package com.springbootTP.springbootPostgreSQL.backend.security.service;

import com.springbootTP.springbootPostgreSQL.backend.security.model.Role;
import com.springbootTP.springbootPostgreSQL.backend.security.model.Users;
import com.springbootTP.springbootPostgreSQL.backend.security.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class JwtService implements JwtServiceInterface, UserDetailsService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public JwtService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Chargement des détails de l'utilisateur pour l'email : {}", email);

        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Utilisateur non trouvé pour l'email : {}", email);
                    return new UsernameNotFoundException("Utilisateur non trouvé");
                });

        log.debug("Détails de l'utilisateur chargés : {}", user);
        return new User(user.getEmail(), user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
    }

    @Override
    public Users registerUser(String email, String username, String password, Role role) {
        log.info("Tentative d'inscription pour l'utilisateur avec l'email : {}", email);

        if (usersRepository.findByEmail(email).isPresent()) {
            log.warn("Email déjà utilisé pour l'utilisateur : {}", email);
            throw new RuntimeException("Email déjà utilisé !");
        }

        Users newUser = new Users();
        newUser.setEmail(email);
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole(role);

        Users savedUser = usersRepository.save(newUser);
        log.info("Inscription réussie pour l'utilisateur avec l'email : {}", email);
        return savedUser;
    }
}