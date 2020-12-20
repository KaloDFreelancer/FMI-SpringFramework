package fmi.spring.framework.cookingRecipes.web;

import fmi.spring.framework.cookingRecipes.exception.InvalidEntityDataException;
import fmi.spring.framework.cookingRecipes.model.Credentials;
import fmi.spring.framework.cookingRecipes.model.JwtResponse;
import fmi.spring.framework.cookingRecipes.model.User;
import fmi.spring.framework.cookingRecipes.service.UserService;
import fmi.spring.framework.cookingRecipes.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static fmi.spring.framework.cookingRecipes.util.ErrorHandlingUtils.getViolationsAsStringList;


@RestController
@Slf4j
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/api/login")
    public JwtResponse login(@Valid @RequestBody Credentials credentials, Errors errors) {
        if(errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid username or password");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                credentials.getUsername(), credentials.getPassword()));
        final User user = userService.getUserByUsername(credentials.getUsername());
        final String token = jwtUtils.generateToken(user);
        log.info("Login successfull for {}: {}", user.getUsername(), token); //remove it!
        return new JwtResponse(user, token);
    }

    @PostMapping("/api/register")
    public User register(@Valid @RequestBody User user, Errors errors){
        if(errors.hasErrors()){
            throw new InvalidEntityDataException("Invalid user data",  getViolationsAsStringList(errors));
        }
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            throw new InvalidEntityDataException(
                    String.format("Username %s already exists", user.getUsername()));
        }
        return userService.addUser(user);
    }
}
