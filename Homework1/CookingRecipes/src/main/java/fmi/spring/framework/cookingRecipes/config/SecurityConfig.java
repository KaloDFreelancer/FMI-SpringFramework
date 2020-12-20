package fmi.spring.framework.cookingRecipes.config;

import fmi.spring.framework.cookingRecipes.service.UserService;
import fmi.spring.framework.cookingRecipes.web.FilterChainExceptionHandlerFilter;
import fmi.spring.framework.cookingRecipes.web.JwtAuthenticationEntryPoint;
import fmi.spring.framework.cookingRecipes.web.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import static fmi.spring.framework.cookingRecipes.model.Role.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private FilterChainExceptionHandlerFilter filterChainExceptionHandlerFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers(POST,"/api/login", "/api/register").permitAll()
                .antMatchers(GET,"/api/recipes/**").permitAll()
                .antMatchers(POST, "/api/recipes").hasAnyRole(AUTHOR.toString(), ADMIN.toString())
                .antMatchers(PUT, "/api/recipes/**").hasAnyRole(AUTHOR.toString(), ADMIN.toString())
                .antMatchers(DELETE, "/api/recipes/**").hasAnyRole(AUTHOR.toString(), ADMIN.toString())
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
