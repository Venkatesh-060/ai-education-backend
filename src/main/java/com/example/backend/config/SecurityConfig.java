package com.example.backend.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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

                                .authorizeHttpRequests(auth -> auth

                                                .requestMatchers("/auth/**").permitAll()
                                                .requestMatchers("/api/session/**").permitAll()

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

                                                .requestMatchers("/admin/**")
                                                .hasRole("ADMIN")

                                                .requestMatchers("/teacher/**")
                                                .hasAnyRole("ADMIN", "TRAINER")

                                                .requestMatchers("/student/**")
                                                .hasAnyRole("ADMIN", "STUDENT")

                                                .requestMatchers("/api/chat/**")
                                                .hasAnyRole("ADMIN", "TRAINER", "STUDENT")

                                                .requestMatchers("/api/whiteboard/**")
                                                .hasAnyRole("TRAINER", "STUDENT")

                                                // .requestMatchers("/api/raisehand/**")
                                                // .hasAnyRole("STUDENT", "TRAINER")
                                                .requestMatchers("/api/raisehand/**")
                                                .permitAll()

                                                // .requestMatchers("/api/participants/**")
                                                // .hasAnyRole("TRAINER", "STUDENT")
                                                .requestMatchers("/api/participants/**")
                                                .permitAll()

                                                .requestMatchers("/api/notifications/**").permitAll()

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


                                                .anyRequest()
                                                .authenticated())

                                .exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint))

                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                .addFilterBefore(jwtFilter,
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
                                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

                configuration.setAllowedHeaders(
                                List.of("*"));

                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

                source.registerCorsConfiguration("/**", configuration);

                return source;
        }
}