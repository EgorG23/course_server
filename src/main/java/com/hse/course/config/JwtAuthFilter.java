package com.hse.course.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//import org.apache.catalina.core.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.util.Collections;
import org.springframework.context.ApplicationContext;



@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final ApplicationContext context; // вместо UserDetailsService

    public JwtAuthFilter(JwtService jwtService, ApplicationContext context) {
        this.jwtService = jwtService;
        this.context = context;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = extractToken(request);
            if (token != null && jwtService.validateToken(token)) {
                String email = jwtService.extractUsername(token);

                UserDetailsService userDetailsService = context.getBean(UserDetailsService.class);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("JWT Filter Error", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        }
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//        try {
//            System.out.println("JWT Filter: Processing request URL = " + request.getRequestURI());
//            String token = extractToken(request);
//            System.out.println("JWT Filter: Extracted token = " + token);
//
//            if (token != null && jwtService.validateToken(token)) {
//                String email = jwtService.extractUsername(token);
//                System.out.println("JWT Filter: Token valid for user = " + email);
//                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
//                        email, null, Collections.emptyList()
//                );
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            } else {
//                System.out.println("JWT Filter: Token is invalid or missing");
//            }
//
//            filterChain.doFilter(request, response);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("JWT Filter Error", e);
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
//        }
//    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}