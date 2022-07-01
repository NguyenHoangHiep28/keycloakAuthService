package com.example.authenticationservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class AuthorizationFilter extends OncePerRequestFilter {
    private static final String[] IGNORE_PATHS = {"/api/v1/login", "/api/v1/register", "/api/v1/token/refresh", "/api/v1/users/{id}"};
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//let login and register pass through
        String requestPath = request.getServletPath();
        if (Arrays.asList(IGNORE_PATHS).contains(requestPath)) {
            filterChain.doFilter(request, response);
        }
    }
}
