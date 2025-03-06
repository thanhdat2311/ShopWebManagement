package com.project.shopapp.configurations;

import com.project.shopapp.filter.JwtTokenFilter;
import com.project.shopapp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
@EnableWebMvc
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers("api/v1/users/register",
                                    "api/v1/users/login"

                            )
                            .permitAll()
                            //User
                            .requestMatchers(HttpMethod.GET, "api/v1/users/details")
                            .hasAnyRole(Role.ADMIN, Role.USER)
                            .requestMatchers(HttpMethod.PUT, "api/v1/users/update")
                            .hasAnyRole(Role.ADMIN, Role.USER)
                            .requestMatchers(HttpMethod.GET, "api/v1/users/valid-token")
                            .hasAnyRole(Role.ADMIN, Role.USER)
                            // Roles
                            .requestMatchers(HttpMethod.GET, "api/v1/roles/**").permitAll()
                            // categories
                            .requestMatchers(HttpMethod.GET, "api/v1/categories/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "api/v1/categories/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT, "api/v1/categories/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE, "api/v1/categories/**").hasRole(Role.ADMIN)
                            // products
                            .requestMatchers(HttpMethod.GET, "api/v1/products/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "api/v1/products/Ids").permitAll()
                            .requestMatchers(HttpMethod.POST, "api/v1/products/**").hasAnyRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT, "api/v1/products/**").hasAnyRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE, "api/v1/products/**").hasAnyRole(Role.ADMIN)
                            //order-details
                            .requestMatchers(HttpMethod.POST, "api/v1/orderdetail/**").hasRole(Role.USER)
                            .requestMatchers(HttpMethod.PUT, "api/v1/orderdetail/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE, "api/v1/orderdetail/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.GET, "api/v1/orderdetail/**").hasAnyRole(Role.ADMIN, Role.USER)
                            // orders
                            .requestMatchers(HttpMethod.POST, "api/v1/order").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT, "api/v1/order/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE, "api/v1/order/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.GET, "api/v1/order/*").permitAll()
                            .anyRequest().authenticated();
                });
        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*")); // URL nguồn gốc được phép
                configuration.setAllowedMethods(Arrays.asList( "DELETE", "OPTIONS", "GET", "POST", "PUT")); // Phương thức được phép
                configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "x-auth-token")); // Header được phép
                configuration.setExposedHeaders(List.of("x-auth-token")); // Header được hiển thị trong response
                //configuration.setAllowCredentials(true); // Cho phép cookie

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration); // Áp dụng cấu hình cho tất cả endpoint
                httpSecurityCorsConfigurer.configurationSource(source); // Đăng ký cấu hình
            }
        });
        return http.build();

    }
}
