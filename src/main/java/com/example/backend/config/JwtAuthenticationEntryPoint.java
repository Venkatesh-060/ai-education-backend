package com.example.backend.config;

import jakarta.servlet.http.*;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    @Override
    public void commence(

            HttpServletRequest request,

            HttpServletResponse response,

            org.springframework.security.core.AuthenticationException authException

    ) throws IOException {

        System.out.println("401 Unauthorized");
System.out.println(request.getRequestURI());

        response.sendError(

                HttpServletResponse.SC_UNAUTHORIZED,

                "Unauthorized"

        );

    }

}