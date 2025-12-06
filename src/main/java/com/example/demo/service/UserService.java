package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String username, String rawPassword, String fullName) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken");
        }
        String hash = passwordEncoder.encode(rawPassword);
        User u = new User(username, hash, fullName);
        return userRepository.save(u);
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public boolean verifyCredentials(String username, String rawPassword){
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) return false;
        String storedHash = userOpt.get().getPasswordHash();
        return passwordEncoder.matches(rawPassword, storedHash);
    }
}
