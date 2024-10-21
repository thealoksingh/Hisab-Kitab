package com.hisabKitab.springProject.authentication;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService authenticationService;

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String email, @RequestParam String password) {
        boolean isAuthenticated = authenticationService.login(email, password);
        
        Map<String, String> response = new HashMap<>();
        
        if (isAuthenticated) {
            response.put("message", "Login successful!");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid email or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }


    // Signup endpoint
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserEntity newUser) {
        String result = authenticationService.signup(newUser);
        if (result.equals("User registered successfully!")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(400).body(result);  // If user already exists
        }
    }
}
