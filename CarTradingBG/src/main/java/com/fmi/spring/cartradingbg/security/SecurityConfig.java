package com.fmi.spring.cartradingbg.security;

import com.fmi.spring.cartradingbg.service.UserService;
import com.fmi.spring.cartradingbg.web.FilterChainExceptionHandlerFilter;
import com.fmi.spring.cartradingbg.web.JwtAuthenticationEntryPoint;
import com.fmi.spring.cartradingbg.web.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import static com.fmi.spring.cartradingbg.enums.Role.ADMIN;
import static com.fmi.spring.cartradingbg.enums.Role.SELLER;
import static org.springframework.http.HttpMethod.*;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final FilterChainExceptionHandlerFilter filterChainExceptionHandlerFilter;

    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAuthenticationFilter jwtAuthenticationFilter, FilterChainExceptionHandlerFilter filterChainExceptionHandlerFilter) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.filterChainExceptionHandlerFilter = filterChainExceptionHandlerFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(POST, "/api/login", "/api/register").permitAll()
                .antMatchers(GET, "/api/car-sale-posts/**").permitAll()
                .antMatchers(POST, "/api/car-sale-posts").hasAnyRole(SELLER.toString(), ADMIN.toString())
                .antMatchers(PUT, "/api/car-sale-posts/**").hasAnyRole(SELLER.toString(), ADMIN.toString())
                .antMatchers(DELETE, "/api/car-sale-posts/**").hasAnyRole(SELLER.toString(), ADMIN.toString())
                .antMatchers("/api/users/**").hasRole(ADMIN.toString())
                .antMatchers("/**").permitAll()

                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(filterChainExceptionHandlerFilter, LogoutFilter.class);
    }

    @Bean
    public UserDetailsService getUserDetailsService(UserService userService) {
        return userService::getUserByUsername;
    }

    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
