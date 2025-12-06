package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) { this.userService = userService; }

    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest req) {
        try {
            User created = userService.register(req.getUsername(), req.getPassword(), null);
            return ResponseEntity.ok(new AuthResponse(true, "Registered", created.getId(), created.getUsername()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new AuthResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new AuthResponse(false, "Server error"));
        }
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        boolean ok = userService.verifyCredentials(req.getUsername(), req.getPassword());
        if (!ok) {
            return ResponseEntity.status(401).body(new AuthResponse(false, "Invalid credentials"));
        }
        User user = userService.findByUsername(req.getUsername()).get();
        // For now we return basic info. In real apps, return JWT token.
        return ResponseEntity.ok(new AuthResponse(true, "Login successful", user.getId(), user.getUsername()));
    }
}
