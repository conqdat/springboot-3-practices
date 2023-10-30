package com.example.springbootpratices.service;

import com.example.springbootpratices.model.User;
import com.example.springbootpratices.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User getUser(Long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override
    public User createUser(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public User updateUser(Long id, User user) {

        User currentUser = this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
        currentUser.setUserName(user.getUserName());
        currentUser.setAge(user.getAge());
        currentUser.setFullName(user.getFullName());

        return userRepository.save(currentUser);
    }
}
