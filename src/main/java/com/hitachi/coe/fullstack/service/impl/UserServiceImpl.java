package com.hitachi.coe.fullstack.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.entity.User;
import com.hitachi.coe.fullstack.exceptions.CoEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitachi.coe.fullstack.model.UserModel;
import com.hitachi.coe.fullstack.repository.UserRepository;
import com.hitachi.coe.fullstack.service.UserService;
import com.hitachi.coe.fullstack.transformation.UserTransformer;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTransformer userTransformer;

    @Override
    public List<UserModel> getAllUsers() {
        return userTransformer.applyList(userRepository.findAll());
    }

    @Override
    public List<UserModel> getUsersByGroupId(Integer groupId) {
        return userRepository.findAllByGroupId(groupId).stream().map(userTransformer).collect(Collectors.toList());
    }

    @Override
    public UserModel getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CoEException(ErrorConstant.CODE_USER_NOT_FOUND, ErrorConstant.MESSAGE_USER_NOT_FOUND));
        return userTransformer.apply(user);
    }
}
