package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UserController {

    @Autowired // Enables dependency injection for Java classes
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user){
        String hashedPassword = Hashing.sha256()
                .hashString(user.getPassword(), StandardCharsets.UTF_8)
                .toString();
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userRepository.findById(id).orElseThrow()); // Could define a custom exception handling.
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User newUser){
        User user = userRepository.findById(id).orElseThrow();

        user.setFname(newUser.getFname());
        user.setLname(newUser.getLname());
        user.setPhoneNumber(newUser.getPhoneNumber());
        user.setEmail(newUser.getEmail());
        user.setUsername(newUser.getUsername());
        String hashedPassword = Hashing.sha256()
                .hashString(newUser.getPassword(), StandardCharsets.UTF_8)
                .toString();
        user.setPassword(hashedPassword);

        return ResponseEntity.ok(userRepository.save(user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id){
        userRepository.delete(userRepository.findById(id).orElseThrow()); // Could define a custom exception handling.
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
