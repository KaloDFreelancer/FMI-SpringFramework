package fmi.spring.framework.cookingRecipes.web;

import fmi.spring.framework.cookingRecipes.exception.InvalidEntityDataException;
import fmi.spring.framework.cookingRecipes.model.User;
import fmi.spring.framework.cookingRecipes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

import static fmi.spring.framework.cookingRecipes.util.ErrorHandlingUtils.getViolationsAsStringList;


@RestController
@RequestMapping("/api/users")
public class UserResource {
    @Autowired
    private UserService userService;

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
        if(errors.hasErrors()){
            throw new InvalidEntityDataException("Invalid user data",  getViolationsAsStringList(errors));
        }
        User created = userService.addUser(user);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}")
                        .buildAndExpand(created.getId()).toUri()
        ).body(created);

    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id,@Valid @RequestBody User user, Errors errors) {
        if(errors.hasErrors()){
            throw new InvalidEntityDataException("Invalid user data",  getViolationsAsStringList(errors));
        }
        if (!id.equals(user.getId())) {
            throw new InvalidEntityDataException(
                    String.format("User URL ID:%s differs from body entity ID:%s", id, user.getId()));
        }
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable String id) {
        User removed = getUserById(id);
        userService.deleteUser(id);
        return removed;
    }

}
