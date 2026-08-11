package com.example.backend.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.backend.jwt.JwtUtil;
import com.example.backend.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final CustomUserDetailsService service;

        public JwtAuthenticationFilter(CustomUserDetailsService service) {
                this.service = service;
        }

        @Override
        protected void doFilterInternal(
                        @NonNull HttpServletRequest request,
                        @NonNull HttpServletResponse response,
                        @NonNull FilterChain filterChain)
                        throws ServletException, IOException {

                System.out.println("======================================");
                System.out.println("URI = " + request.getRequestURI());

                String header = request.getHeader("Authorization");

                System.out.println("Authorization Header = " + header);

                try {

                        // No token
                        if (header == null || !header.startsWith("Bearer ")) {

                                System.out.println("No Bearer token found");

                                filterChain.doFilter(request, response);
                                return;
                        }

                        // Extract token
                        String token = header.substring(7);

                        // Validate token
                        boolean valid = JwtUtil.validateToken(token);

                        System.out.println("Token Valid = " + valid);

                        if (!valid) {

                                System.out.println("Invalid JWT token");

                                filterChain.doFilter(request, response);
                                return;
                        }

                        // Get email from token
                        String email = JwtUtil.getEmail(token);

                        System.out.println("JWT Email = " + email);

                        // Only authenticate if no authentication already exists
                        if (SecurityContextHolder.getContext().getAuthentication() == null) {

                                var userDetails = service.loadUserByUsername(email);

                                System.out.println(
                                                "Authorities = "
                                                                + userDetails.getAuthorities());

                                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                                userDetails,
                                                null,
                                                userDetails.getAuthorities());

                                authentication.setDetails(
                                                new WebAuthenticationDetailsSource()
                                                                .buildDetails(request));

                                SecurityContextHolder
                                                .getContext()
                                                .setAuthentication(authentication);

                                System.out.println(
                                                "Authentication set successfully");

                                System.out.println(
                                                "Authenticated = "
                                                                + SecurityContextHolder
                                                                                .getContext()
                                                                                .getAuthentication()
                                                                                .isAuthenticated());

                                System.out.println(
                                                "Final Authorities = "
                                                                + SecurityContextHolder
                                                                                .getContext()
                                                                                .getAuthentication()
                                                                                .getAuthorities());
                        }

                } catch (Exception e) {

                        System.out.println("========== JWT ERROR ==========");
                        System.out.println("Request URI: " + request.getRequestURI());
                        System.out.println("Error: " + e.getMessage());
                        e.printStackTrace();

                        // Clear invalid authentication
                        SecurityContextHolder.clearContext();
                }

                System.out.println("Continuing filter chain...");
                System.out.println("======================================");

                filterChain.doFilter(request, response);
        }
}