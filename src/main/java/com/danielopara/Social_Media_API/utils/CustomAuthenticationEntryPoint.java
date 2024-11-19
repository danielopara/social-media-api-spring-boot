package com.danielopara.Social_Media_API.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        String requestURI = request.getRequestURI();


        Map<String, Object > responseBody = new HashMap<>();
        responseBody.put("status", (HttpStatus.FORBIDDEN.value()));
        responseBody.put("error", "Unauthorized");
        responseBody.put("message", authException.getMessage());
        responseBody.put("url", requestURI);
        responseBody.put("path", request.getServletPath());


        String jsonResponse = new ObjectMapper().writeValueAsString(responseBody);

        response.getWriter().write(jsonResponse);
    }
}
