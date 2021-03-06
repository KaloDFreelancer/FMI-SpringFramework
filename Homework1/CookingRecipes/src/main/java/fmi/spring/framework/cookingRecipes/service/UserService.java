package fmi.spring.framework.cookingRecipes.service;


import fmi.spring.framework.cookingRecipes.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(String id);
    User getUserByUsername(String username);
    User addUser(User user);
    User updateUser(User user);
    User deleteUser(String id);
    Optional<User> findByUsername(String username);
    long getCount();
}
