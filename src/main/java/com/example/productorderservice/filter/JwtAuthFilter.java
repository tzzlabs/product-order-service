package com.example.productorderservice.filter;

import com.example.productorderservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            try {
                String username = jwtUtil.extractUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtUtil.validateToken(token)) {
                        // Extract roles and authorities from token
                        List<String> roles = jwtUtil.extractRoles(token);
                        List<String> authoritiesList = jwtUtil.extractAuthorities(token);
                        
                        // Combine roles and authorities
                        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                        
                        // Add roles as authorities (with ROLE_ prefix)
                        if (roles != null) {
                            authorities.addAll(roles.stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList()));
                        }
                        
                        // Add specific authorities
                        if (authoritiesList != null) {
                            authorities.addAll(authoritiesList.stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList()));
                        }
                        
                        // If no authorities found, provide default
                        if (authorities.isEmpty()) {
                            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                        }
                        
                        // Debug logging
                        System.out.println("Authenticating user: " + username);
                        System.out.println("Authorities: " + authorities);

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(username, null, authorities);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception e) {
                System.err.println("JWT processing error: " + e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}