package fmi.course.cookingwebapp.service;

import fmi.course.cookingwebapp.model.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UsersService {
    User findByUsername(String username);
    Iterable<User> getAllUsers();
    User getUserById(Long id);
    User getUserByUsername(String username);
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);
    long getCount();
}
