package com.fmi.spring.cartradingbg.web;


import com.fmi.spring.cartradingbg.model.User;
import com.fmi.spring.cartradingbg.service.UserService;
import com.fmi.spring.cartradingbg.util.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@Order
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        if(authorizationHeader != null) {
            if (authorizationHeader.startsWith("Bearer ")) {
                jwtToken = authorizationHeader.substring(7);
                try {
                    username = jwtUtils.getUsernameFromToken(jwtToken);
                } catch (IllegalArgumentException ex) {
                    log.error("Unable to get JWT token.");
                    throw new BadCredentialsException("Unable to get JWT token.");
                } catch (ExpiredJwtException ex) {
                    log.error("JWT token has expired.");
                    throw new BadCredentialsException("JWT token has expired.");
                }
            } else {
                log.error("JWT token does not begin with 'Bearer ' prefix.");
                throw new BadCredentialsException("WT token does not begin with 'Bearer ' prefix.");
            }
        }

        if(username != null) {
            User user = userService.getUserByUsername(username);
            if(jwtUtils.validateToken(jwtToken, user)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
