package com.fmi.spring.cartradingbg.web;


import com.fmi.spring.cartradingbg.exception.InvalidEntityDataException;
import com.fmi.spring.cartradingbg.model.User;
import com.fmi.spring.cartradingbg.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

import static com.fmi.spring.cartradingbg.util.ErrorHandlingUtils.getViolationsAsStringList;

@RestController
@RequestMapping("/api/users")
public class UserResource {
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") String id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user, Errors errors) {
        if (errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid user data", getViolationsAsStringList(errors));
        }
        User created = userService.addUser(user);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}")
                        .buildAndExpand(created.getId()).toUri()
        ).body(created);

    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id, @Valid @RequestBody User user, Errors errors) {
        if (errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid user data", getViolationsAsStringList(errors));
        }
        if (!id.equals(user.getId())) {
            throw new InvalidEntityDataException(
                    String.format("User URL ID:%s differs from body entity ID:%s", id, user.getId()));
        }
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }

}
