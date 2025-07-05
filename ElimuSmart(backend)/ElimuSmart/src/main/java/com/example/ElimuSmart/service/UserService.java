package com.example.ElimuSmart.service;

import aj.org.objectweb.asm.commons.Remapper;
import com.example.ElimuSmart.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    Optional<User> login(String username, String password);
    Optional<User> findByUsername(String username);

    User saveUser(User user);

    User saveUser(User user, String password);

    List<User> getAllUsers();

    Remapper getUserById(Long id);

    void deleteUser(Long id);

    User updateUser(Long id, User user);
}
