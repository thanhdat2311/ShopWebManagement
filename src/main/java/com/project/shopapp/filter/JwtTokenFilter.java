package com.project.shopapp.filter;

import com.project.shopapp.components.JwtTokenUtil;
import com.project.shopapp.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
//@RequiredArgsConstructor
@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            if (isByPassToken(request)) {
                filterChain.doFilter(request, response);
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if (!authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
                final String token = authHeader.substring(7);
                final String phone = jwtTokenUtil.extractPhoneNumber(token);
                if (phone != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User user = (User) userDetailsService.loadUserByUsername(phone);
                    if (jwtTokenUtil.validateToken(token, user)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            }
            filterChain.doFilter(request, response); //enable bypas cho qua hết
        } catch (
                Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    private boolean isByPassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> byPassTokens = Arrays.asList(
                Pair.of("api/v1/roles", "GET"),
                Pair.of("api/v1/products", "GET"),
//                Pair.of("api/v1/order/", "GET"),
                Pair.of("api/v1/categories", "GET"),
                Pair.of("api/v1/users/register", "POST"),
                Pair.of("api/v1/users/login", "POST")

        );
        for (Pair<String, String> byPassToken : byPassTokens) {
            if (request.getServletPath().contains(byPassToken.getLeft())
                    && request.getMethod().equals(byPassToken.getRight())) {
                return true;
            }
        }
        return false;
    }
}
