package fmi.course.cookingwebapp.service.implementation;

import fmi.course.cookingwebapp.constant.Gender;
import fmi.course.cookingwebapp.exception.InvalidEntityDataException;
import fmi.course.cookingwebapp.exception.NonExistingEntityException;
import fmi.course.cookingwebapp.model.User;
import fmi.course.cookingwebapp.repo.UserRepository;
import fmi.course.cookingwebapp.service.UsersService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UsersService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new NonExistingEntityException("Invalid username"));
    }

    @Override
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NonExistingEntityException(String.format("User with ID: %s does not exist.", id)));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
        new NonExistingEntityException(String.format("User with username: %s does not exist.", username)));
    }

    @Override
    public void addUser(User user) {
        user.setId(null);
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        if (Objects.isNull(user.getImageUrl()) || user.getImageUrl().isBlank()) {
            if (user.getGender().equals(Gender.MALE)) {
                user.setImageUrl("maleAvatar.jpg");
            }
            else {
                user.setImageUrl("femaleAvatar.jpg");
            }
        }
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        User removed = getUserById(id);
        userRepository.deleteById(id);
    }

    @Override
    public long getCount() {
        return userRepository.count();
    }
}
