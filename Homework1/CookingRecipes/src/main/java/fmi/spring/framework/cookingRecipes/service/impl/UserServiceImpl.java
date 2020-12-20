package fmi.spring.framework.cookingRecipes.service.impl;

import fmi.spring.framework.cookingRecipes.dao.UserRepository;
import fmi.spring.framework.cookingRecipes.exception.InvalidEntityDataException;
import fmi.spring.framework.cookingRecipes.exception.NonexistingEntityException;
import fmi.spring.framework.cookingRecipes.model.User;
import fmi.spring.framework.cookingRecipes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepo;

    @Autowired
    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserById(String id) {
        return userRepo.findById(id).orElseThrow(() ->
            new NonexistingEntityException(String.format("User with ID:%s does not exist.", id)));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(() ->
                new InvalidEntityDataException("Invalid username or password."));
    }

    @Override
    public User addUser(User user) {
        user.setId(null);
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.insert(user);
    }

    @Override
    public User updateUser(User user) {
        getUserById(user.getId());
        user.setModified(LocalDateTime.now());
        return userRepo.save(user);
    }

    @Override
    public User deleteUser(String id) {
        User removed = getUserById(id);
        //check all recipes if there are posts with this authorId
        userRepo.deleteById(id);
        return removed;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public long getCount() {
        return userRepo.count();
    }
}
