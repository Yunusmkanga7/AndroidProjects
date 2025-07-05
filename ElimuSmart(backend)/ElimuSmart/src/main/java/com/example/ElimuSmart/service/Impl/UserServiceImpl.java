package com.example.ElimuSmart.service.Impl;

import aj.org.objectweb.asm.commons.Remapper;
import com.example.ElimuSmart.model.User;
import com.example.ElimuSmart.repository.UserRepository;
import com.example.ElimuSmart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(User user) {
        // Angalia kama username tayari inatumika
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists.");
        }

        // Usihash — weka password kama ilivyo
        return userRepository.save(user);
    }

    @Override
    public Optional<User> login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Linga password moja kwa moja
            if (user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User saveUser(User user) {
        return null;
    }

    @Override
    public User saveUser(User user, String password) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return List.of();
    }

    @Override
    public Remapper getUserById(Long id) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }

    @Override
    public User updateUser(Long id, User user) {
        return null;
    }
}
