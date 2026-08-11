package com.example.backend.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtFilter;
        private final JwtAuthenticationEntryPoint entryPoint;

        public SecurityConfig(
                        JwtAuthenticationFilter jwtFilter,
                        JwtAuthenticationEntryPoint entryPoint) {

                this.jwtFilter = jwtFilter;
                this.entryPoint = entryPoint;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http
                                .cors(cors -> {
                                })
                                .csrf(csrf -> csrf.disable())

                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))

                                .exceptionHandling(exception -> exception.authenticationEntryPoint(entryPoint))

                                .authorizeHttpRequests(auth -> auth

                                                // =========================
                                                // PUBLIC APIs
                                                // =========================

                                                .requestMatchers("/auth/**").permitAll()

                                                .requestMatchers("/api/recovery/**").permitAll()
                                                .requestMatchers("/error").permitAll()

                                                // .requestMatchers("/api/session/**").permitAll()

                                                // =========================
                                                // BATCH MANAGEMENT
                                                // ADMIN + TRAINER
                                                // =========================

                                                .requestMatchers("/api/admin/batches/**")
                                                .hasRole("ADMIN")

                                                .requestMatchers("/api/trainer/**")
                                                .hasRole("TRAINER")
                                                .requestMatchers("/api/batch/**")
                                                .hasAnyRole("ADMIN", "TRAINER")

                                                // =========================
                                                // SESSION MANAGEMENT
                                                // =========================

                                                .requestMatchers("/api/session/**")
                                                .hasAnyRole("ADMIN", "TRAINER")

                                                // =========================
                                                // ADMIN APIs
                                                // =========================

                                                .requestMatchers("/api/admin/**")
                                                .hasRole("ADMIN")
                                                // =========================
                                                // ATTENDANCE
                                                // =========================

                                                .requestMatchers("/api/attendance/mark")
                                                .hasAnyRole("ADMIN", "TRAINER", "STUDENT")

                                                .requestMatchers("/api/attendance/update")
                                                .hasAnyRole("ADMIN", "TRAINER", "STUDENT")

                                                .requestMatchers("/api/attendance/report")
                                                .hasAnyRole("ADMIN", "TRAINER")

                                                .requestMatchers("/api/attendance/stats")
                                                .hasAnyRole("ADMIN", "TRAINER")

                                                .requestMatchers("/api/attendance/all")
                                                .hasAnyRole("ADMIN", "TRAINER")

                                                .requestMatchers("/api/attendance/session/**")
                                                .hasAnyRole("ADMIN", "TRAINER")

                                                .requestMatchers("/api/attendance/student/**")
                                                .hasAnyRole("ADMIN", "TRAINER", "STUDENT")

                                                // =========================
                                                // CHAT
                                                // =========================

                                                .requestMatchers("/api/chat/**")
                                                .hasAnyRole("ADMIN", "TRAINER", "STUDENT")

                                                // =========================
                                                // WHITEBOARD
                                                // =========================

                                                .requestMatchers("/api/whiteboard/**")
                                                .hasAnyRole("TRAINER", "STUDENT")

                                                // =========================
                                                // PARTICIPANTS
                                                // =========================

                                                .requestMatchers("/api/participants/**")
                                                .permitAll()

                                                // =========================
                                                // RAISE HAND
                                                // =========================

                                                .requestMatchers("/api/raisehand/**")
                                                .permitAll()

                                                // =========================
                                                // NOTIFICATIONS
                                                // =========================

                                                .requestMatchers(
                                                                HttpMethod.POST,
                                                                "/api/notifications/**")
                                                .hasAnyRole("ADMIN", "TRAINER")

                                                .requestMatchers(
                                                                HttpMethod.PUT,
                                                                "/api/notifications/*")
                                                .hasAnyRole("ADMIN", "TRAINER")

                                                .requestMatchers(
                                                                HttpMethod.PUT,
                                                                "/api/notifications/*/read")
                                                .hasRole("STUDENT")

                                                .requestMatchers(
                                                                HttpMethod.GET,
                                                                "/api/notifications/**")
                                                .hasAnyRole("ADMIN", "TRAINER", "STUDENT")

                                                .requestMatchers(
                                                                HttpMethod.DELETE,
                                                                "/api/notifications/**")
                                                .hasAnyRole("ADMIN", "TRAINER")

                                                // =========================
                                                // FEEDBACK
                                                // =========================

                                                .requestMatchers("/api/feedback")
                                                .hasRole("STUDENT")

                                                .requestMatchers("/api/feedback/session/**")
                                                .hasAnyRole("ADMIN", "TRAINER")

                                                .requestMatchers("/api/feedback/trainer/**")
                                                .hasAnyRole("ADMIN", "TRAINER")

                                                .requestMatchers("/api/feedback/average/**")
                                                .hasAnyRole("ADMIN", "TRAINER")

                                                .requestMatchers("/api/feedback/distribution/**")
                                                .hasAnyRole("ADMIN", "TRAINER")

                                                .requestMatchers("/api/feedback/all")
                                                .hasAnyRole("ADMIN", "TRAINER")

                                                .requestMatchers("/api/feedback/search")
                                                .hasAnyRole("ADMIN", "TRAINER")

                                                // =========================
                                                // RECORDINGS
                                                // =========================

                                                .requestMatchers(
                                                                HttpMethod.POST,
                                                                "/api/recordings/**")
                                                .hasAnyRole("ADMIN", "TRAINER")

                                                .requestMatchers(
                                                                HttpMethod.PUT,
                                                                "/api/recordings/**")
                                                .hasAnyRole("ADMIN", "TRAINER")

                                                .requestMatchers(
                                                                HttpMethod.DELETE,
                                                                "/api/recordings/**")
                                                .hasAnyRole("ADMIN", "TRAINER")

                                                .requestMatchers(
                                                                HttpMethod.GET,
                                                                "/api/recordings/**")
                                                .hasAnyRole("ADMIN", "TRAINER", "STUDENT")

                                                // =========================
                                                // ROLE BASED PAGES
                                                // =========================

                                                .requestMatchers("/teacher/**")
                                                .hasAnyRole("ADMIN", "TRAINER")

                                                .requestMatchers("/student/**")
                                                .hasAnyRole("ADMIN", "STUDENT")

                                                .requestMatchers("/admin/**")
                                                .hasRole("ADMIN")

                                                // =========================
                                                // EVERYTHING ELSE
                                                // =========================

                                                .anyRequest()
                                                .authenticated())

                                .addFilterBefore(
                                                jwtFilter,
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(
                        AuthenticationConfiguration config) throws Exception {

                return config.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {

                return new BCryptPasswordEncoder();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(
                                List.of("http://localhost:5173"));

                configuration.setAllowedMethods(
                                List.of(
                                                "GET",
                                                "POST",
                                                "PUT",
                                                "DELETE",
                                                "OPTIONS"));

                configuration.setAllowedHeaders(
                                List.of("*"));

                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

                source.registerCorsConfiguration(
                                "/**",
                                configuration);

                return source;
        }
}