package com.example.springboot_3_practices.controller;

import com.example.springboot_3_practices.entity.User;
import com.example.springboot_3_practices.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    // Create a new user
    @PostMapping
    public User createUser(@RequestBody User user) {
        userRepository.insert(user);
        return user; // Return the user with the generated ID
    }

    // Update an existing user
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        // Update the fields
        existingUser.setName(updatedUser.getName());
        existingUser.setAge(updatedUser.getAge());
        existingUser.setEmail(updatedUser.getEmail());

        userRepository.update(existingUser);
        return existingUser;
    }

    // Delete a user by ID
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Integer id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
        return "User with ID " + id + " deleted successfully.";
    }
}
