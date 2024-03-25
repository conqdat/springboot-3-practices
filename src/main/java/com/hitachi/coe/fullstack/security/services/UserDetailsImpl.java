package com.hitachi.coe.fullstack.security.services;

import com.hitachi.coe.fullstack.model.GroupRightModel;
import com.hitachi.coe.fullstack.model.UserModel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * UserDetailsImpl is a class that implements UserDetails interface.
 *
 * @see org.springframework.security.core.userdetails.UserDetails
 * @author tminhto
 */
@Getter
@Slf4j
public class UserDetailsImpl implements UserDetails {
    private UserModel userModel;
    private List<GroupRightModel> groupRightModels;

    /**
     * Constructs a new UserDetailsImpl object with the given UserModel and List of GroupRightModel objects.
     * Throws a NullPointerException if either parameter is null.
     *
     * @param userModel the UserModel object representing the user's details
     * @param groupRights the List of GroupRightModel objects representing the permissions granted to the user's group
     * @throws NullPointerException if either userModel or groupRights is null
     * @see UserModel
     * @see GroupRightModel
     */
    public UserDetailsImpl(UserModel userModel, List<GroupRightModel> groupRights) {
        this.userModel = Objects.requireNonNull(userModel, "userModel cannot be null");
        this.groupRightModels = Objects.requireNonNull(groupRights, "groupRights cannot be null");
    }

    /**
     * Returns a collection of SimpleGrantedAuthority objects representing the permissions granted to the user's group.
     * Filters out any group rights that have null values for rightCode or rightModule.
     * Maps the remaining group rights to SimpleGrantedAuthority objects with authority strings in the format "RIGHTCODE_RIGHTMODULE".
     * Converts the authority strings to uppercase.
     * Logs the username and authorities before returning the collection.
     *
     * @return a collection of SimpleGrantedAuthority objects representing the permissions granted to the user's group
     * @see GroupRightModel#getRightCode()
     * @see GroupRightModel#getRightModule()
     * @see SimpleGrantedAuthority
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = this.groupRightModels.stream()
                .filter(model -> model.getRightCode() != null && model.getRightModule() != null)
                .map(model -> new SimpleGrantedAuthority((model.getRightCode() + "_" + model.getRightModule()).toUpperCase()))
                .collect(Collectors.toList());
        log.info("username = {}, authorities = {}", this.userModel.getName(), authorities);
        return authorities;
    }

    @Override
    public String getPassword() {
        return Optional.ofNullable(userModel).map(UserModel::getPassword).orElse(null);
    }

    @Override
    public String getUsername() {
        return Optional.ofNullable(userModel).map(UserModel::getName).orElse(null);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
