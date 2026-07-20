package com.example.backend.config;

import com.example.backend.jwt.JwtUtil;
import com.example.backend.service.CustomUserDetailsService;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    CustomUserDetailsService service;

    @Override
protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException, IOException {
                System.out.println("JWT FILTER CALLED");
        HttpServletRequest req = (HttpServletRequest) request;

        String header = req.getHeader("Authorization");
System.out.println("Header = " + header);
        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            System.out.println("Token = " + token);

            if (JwtUtil.validateToken(token)) {

                System.out.println("Token Valid");

                String email = JwtUtil.getEmail(token);

                System.out.println("Email = " + email);

                var userDetails = service.loadUserByUsername(email);

                System.out.println("Authorities = " + userDetails.getAuthorities());

                UsernamePasswordAuthenticationToken auth =

                        new UsernamePasswordAuthenticationToken(

                                userDetails,

                                null,

                                userDetails.getAuthorities()

                        );

                auth.setDetails(

                        new WebAuthenticationDetailsSource()

                                .buildDetails(req)

                );

                SecurityContextHolder

                        .getContext()

                        .setAuthentication(auth);

            }

        }

        filterChain.doFilter(request, response);

    }

}