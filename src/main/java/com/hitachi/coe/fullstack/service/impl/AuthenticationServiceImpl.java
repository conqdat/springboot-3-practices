package com.hitachi.coe.fullstack.service.impl;

import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.model.LoginRequest;
import com.hitachi.coe.fullstack.model.LoginResponse;
import com.hitachi.coe.fullstack.model.RefreshTokenRequest;
import com.hitachi.coe.fullstack.model.RefreshTokenResponse;
import com.hitachi.coe.fullstack.service.AuthenticationService;
import com.hitachi.coe.fullstack.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This service is an implementation of AuthenticationService to handle authentication and refresh token
 * See the interface for more detail
 *
 * @author tminhto
 * @see AuthenticationService
 */
@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    AuthenticationManager authenticationManager;


    @Autowired
    JwtUtils jwtUtils;

    /**
     * An implementation of login method from AuthenticationService interface.
     * Authenticates a user with the provided username and password and generates a JWT access token and refresh token.
     *
     * @param request the login request containing the username and password
     * @return the login response containing the access token, refresh token, and username
     * @throws BadCredentialsException if the provided credentials are invalid
     * @author tminhto
     * @see AuthenticationService
     * @see LoginRequest
     * @see LoginResponse
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        // AuthenticationManager's provider  will handle username/password authentication for us
        // See the config in AuthConfig.java for more detail
        final Authentication authentication;
        try {
            // authenticate method will return a fully populated Authentication object
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            log.error("Bad credentials for username: {}", request.getUsername() + " " + e.getMessage());
            // We catch the exception here to throw back the exception for global exception handler to handle
            throw new BadCredentialsException(ErrorConstant.MESSAGE_BAD_CREDENTIALS);
        }
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("username", userDetails.getUsername());
        final String accessToken = jwtUtils.generateJwtAccessToken(userDetails.getUsername());
        final String refreshToken = jwtUtils.generateJwtRefreshToken(extraClaims, userDetails.getUsername());
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(userDetails.getUsername())
                .build();
    }

    /**
     * An implementation of refreshToken method from AuthenticationService interface.
     * Refreshes a JWT access token and refresh token using the provided refresh token.
     *
     * @param request the refresh token request containing the refresh token
     * @return the refresh token response containing the new access token and refresh token
     * @throws JwtException if the provided refresh token is invalid
     * @author tminhto
     * @see AuthenticationService
     * @see RefreshTokenRequest
     * @see RefreshTokenResponse
     */
    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) throws JwtException {
        final String requestRefreshToken = request.getRefreshToken();
        // Check for malformed, expired, unsupported, illegal argument jwt exception
        final boolean isValidRefreshToken = jwtUtils.validateJwtToken(requestRefreshToken);
        // if the refresh token can not be extract, we throw JwtException here for global exception handler to handle
        final Claims claims = jwtUtils.extractAllClaims(requestRefreshToken);
        // throw exception for the exception handler to handle
        if (!isValidRefreshToken) {
            log.error("Invalid refresh token");
            // throw JwtException for global exception handler to handle
            throw new JwtException(ErrorConstant.MESSAGE_INVALID_TOKEN);
        }
        final String username = claims.getSubject();
        final String accessToken = jwtUtils.generateJwtAccessToken(username);
        final String refreshToken = jwtUtils.generateJwtRefreshToken(username);
        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
