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

                String header = request.getHeader("Authorization");

                System.out.println("URI = " + request.getRequestURI());
                System.out.println("Authorization Header = " + header);

                if (header != null && header.startsWith("Bearer ")) {

                        String token = header.substring(7);
                                                         System.out.println("Token Valid = " + JwtUtil.validateToken(token));


                        if (JwtUtil.validateToken(token)) {


                                String email = JwtUtil.getEmail(token);
                                System.out.println("JWT Email = " + email);


                                var userDetails = service.loadUserByUsername(email);
                                 System.out.println("Authorities = " + userDetails.getAuthorities());

                                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                                userDetails,
                                                null,
                                                userDetails.getAuthorities());

                                authentication.setDetails(
                                                new WebAuthenticationDetailsSource()
                                                                .buildDetails(request));

                                SecurityContextHolder.getContext()
                                                .setAuthentication(authentication);

                                                System.out.println("Authentication = "
                + SecurityContextHolder.getContext().getAuthentication());
                        }
                }

                filterChain.doFilter(request, response);
        }
}