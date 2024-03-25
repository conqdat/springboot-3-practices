package com.hitachi.coe.fullstack.security.services;

import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.entity.GroupRight;
import com.hitachi.coe.fullstack.entity.User;
import com.hitachi.coe.fullstack.model.GroupRightModel;
import com.hitachi.coe.fullstack.model.UserModel;
import com.hitachi.coe.fullstack.repository.GroupRightRepository;
import com.hitachi.coe.fullstack.repository.UserRepository;
import com.hitachi.coe.fullstack.transformation.GroupRightTransformer;
import com.hitachi.coe.fullstack.transformation.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class provides an implementation of the {@link UserDetailsService} interface for retrieving user details
 * from a database using the {@link UserRepository}, {@link GroupRightRepository}, {@link UserTransformer},
 * and {@link GroupRightTransformer} dependencies.
 *
 * @author tminhto
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserTransformer userTransformer;

    @Autowired
    GroupRightTransformer groupRightTransformer;

    @Autowired
    GroupRightRepository groupRightRepository;


    /**
     * Retrieves user details for the given username from the database.
     * Throws a {@link UsernameNotFoundException} if no user with the given name is found in the database.
     * Throws a {@link RuntimeException} if the group rights for the user's group cannot be found in the database.
     *
     * @param username the username of the user to retrieve details for
     * @return a {@link UserDetails} object containing the user details and group rights
     * @throws UsernameNotFoundException if no user with the given name is found in the database
     * @throws RuntimeException          if the group rights for the user's group cannot be found in the database
     * @author tminhto
     * @see UserRepository
     * @see GroupRightRepository
     * @see UserTransformer
     * @see GroupRightTransformer
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = Optional.ofNullable(userRepository.findByName(username))
                .orElseThrow(() -> new UsernameNotFoundException(ErrorConstant.MESSAGE_USER_NOT_FOUND));
        List<GroupRight> groupRights = Optional.ofNullable(groupRightRepository.findAllByGroupId(user.getGroup().getId()))
                .orElseThrow(() -> new RuntimeException(ErrorConstant.MESSAGE_GROUP_RIGHTS_NOT_FOUND));
        UserModel userModel = userTransformer.apply(user);
        List<GroupRightModel> groupRightModels = groupRights.stream().map(groupRightTransformer).collect(Collectors.toList());
        return new UserDetailsImpl(userModel, groupRightModels);
    }
}

