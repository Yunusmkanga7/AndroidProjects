package com.example.ElimuSmart.controller;

import com.example.ElimuSmart.model.User;
import com.example.ElimuSmart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    // Register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User savedUser = userService.registerUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        Optional<User> userOptional = userService.login(loginRequest.getUsername(), loginRequest.getPassword());

        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get()); // ✅ returns User object
        } else {
            return ResponseEntity.status(401).body("Invalid username or password"); // ✅ returns String
        }
    }

}
