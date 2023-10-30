package com.example.springbootpratices.service;

import com.example.springbootpratices.model.User;

import java.util.List;

public interface IUserService {
    List<User> getUsers();
    User getUser(Long id);
    User createUser(User user);
    void deleteUser(Long id);
    User updateUser(Long id, User user);
}
