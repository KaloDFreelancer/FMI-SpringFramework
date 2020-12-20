package com.fmi.spring.cartradingbg.web;

import com.fmi.spring.cartradingbg.exception.AlreadyExistsException;
import com.fmi.spring.cartradingbg.exception.InvalidEntityDataException;
import com.fmi.spring.cartradingbg.exception.NotLoggedInException;
import com.fmi.spring.cartradingbg.model.Credentials;
import com.fmi.spring.cartradingbg.model.JwtResponse;
import com.fmi.spring.cartradingbg.model.User;
import com.fmi.spring.cartradingbg.service.UserService;
import com.fmi.spring.cartradingbg.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.security.Principal;

import static com.fmi.spring.cartradingbg.util.ErrorHandlingUtils.getViolationsAsStringList;


@RestController
@Slf4j
public class LoginController {
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public LoginController(UserService userService, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/api/login")
    public JwtResponse login(@Valid @RequestBody Credentials credentials, Errors errors) {
        if(errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid username or password");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                credentials.getUsername(), credentials.getPassword()));
        final User user = userService.getUserByUsername(credentials.getUsername());
        final String token = jwtUtils.generateToken(user);
        return new JwtResponse(user, token);
    }

    @PostMapping("/api/register")
    public User register(@Valid @RequestBody User user, Errors errors){
        if(errors.hasErrors()){
            throw new InvalidEntityDataException("Invalid user data",  getViolationsAsStringList(errors));
        }
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            throw new AlreadyExistsException(
                    String.format("Username %s already exists", user.getUsername()));
        }
        return userService.addUser(user);
    }
}
