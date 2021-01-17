package fmi.course.cookingwebapp.config;


import fmi.course.cookingwebapp.constant.Role;
import fmi.course.cookingwebapp.exception.NonExistingEntityException;
import fmi.course.cookingwebapp.service.UsersService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/users/**").hasAuthority(Role.ADMIN.toString())
                .antMatchers(GET, "/").permitAll()
                .antMatchers(GET, "/login", "/registration").permitAll()
                .antMatchers(POST, "/login", "/registration").permitAll()
                .antMatchers(GET, "/recipes/**").permitAll()
                .antMatchers(POST, "/recipes").hasAnyRole(Role.USER.toString(), Role.ADMIN.toString())
                .antMatchers(PUT, "/recipes/**").hasAnyRole(Role.USER.toString(), Role.ADMIN.toString())
                .antMatchers(DELETE, "/recipes/**").hasAnyRole(Role.USER.toString(), Role.ADMIN.toString())
                .antMatchers("/**").authenticated()
                .and()
                .formLogin().defaultSuccessUrl("/recipes").and()
                .logout().deleteCookies("JSESSIONID").clearAuthentication(true).invalidateHttpSession(true).logoutUrl("/logout");
    }

    @Bean
    public UserDetailsService userDetailsService(UsersService usersService) {

        return username -> {
            try {
                return usersService.findByUsername(username);
            } catch (NonExistingEntityException ex) {
                throw new UsernameNotFoundException(ex.getMessage(), ex);
            }
        };

    }
}
