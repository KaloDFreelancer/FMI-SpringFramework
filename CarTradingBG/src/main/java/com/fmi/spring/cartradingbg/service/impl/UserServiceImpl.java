package com.fmi.spring.cartradingbg.service.impl;

import com.fmi.spring.cartradingbg.exception.AlreadyExistsException;
import com.fmi.spring.cartradingbg.exception.InvalidEntityDataException;
import com.fmi.spring.cartradingbg.exception.NoExistingEntityException;
import com.fmi.spring.cartradingbg.model.User;
import com.fmi.spring.cartradingbg.repository.CarSalePostsRepository;
import com.fmi.spring.cartradingbg.repository.UserRepository;
import com.fmi.spring.cartradingbg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final CarSalePostsRepository carSalePostsRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepo, CarSalePostsRepository carSalePostsRepository) {
        this.userRepo = userRepo;
        this.carSalePostsRepository = carSalePostsRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserById(String id) {
        return userRepo.findById(id).orElseThrow(() ->
                new NoExistingEntityException(String.format("User with ID:%s does not exist.", id)));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(() ->
                new InvalidEntityDataException("Invalid username or password."));
    }

    @Override
    public User addUser(User user) {
        if (userRepo.findByUsername(user.getUsername()).isEmpty()) {
            user.setId(null);
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            return userRepo.insert(user);
        } else {
            throw new AlreadyExistsException(
                    String.format("Username %s already exists", user.getUsername()));
        }
    }

    @Override
    public User updateUser(User user) {
        if (userRepo.findById(user.getId()).isEmpty()) {
            throw new NoExistingEntityException(String.format("User with ID:%s does not exist.", user.getId()));
        }
        if (!userRepo.findById(user.getId()).get().getUsername().equals(user.getUsername())) {
            if (userRepo.findByUsername(user.getUsername()).isPresent()) {
                throw new AlreadyExistsException(
                        String.format("Username %s already exists", user.getUsername()));
            }
        }
        return userRepo.save(user);
    }

    @Override
    public User deleteUser(String id) {
        User removed = getUserById(id);
        //remove all of his posts
        carSalePostsRepository.findAll()
                .stream()
                .filter(post -> post.getSellerId().equals(id))
                .forEach(carSalePostsRepository::delete);

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
