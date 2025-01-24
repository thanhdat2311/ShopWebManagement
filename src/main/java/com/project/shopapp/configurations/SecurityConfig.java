package com.project.shopapp.configurations;

import com.project.shopapp.models.User;
import com.project.shopapp.repositories.UserRepo;
import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//@RequiredArgsConstructor
@AllArgsConstructor
@Configuration
public class SecurityConfig {
    // user detail object
    private final UserRepo userRepo;
    @Bean
    public UserDetailsService userDetailsService(){
        return phoneNumber -> {
          User user = userRepo
                    .findByPhone(phoneNumber)
                    .orElseThrow(() -> new IllegalArgumentException("Cannot find user with this phone number"));


            return user;
        };

    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
