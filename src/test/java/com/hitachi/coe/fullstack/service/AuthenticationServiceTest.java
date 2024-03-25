package com.hitachi.coe.fullstack.service;

import com.hitachi.coe.fullstack.model.LoginRequest;
import com.hitachi.coe.fullstack.model.LoginResponse;
import com.hitachi.coe.fullstack.model.RefreshTokenRequest;
import com.hitachi.coe.fullstack.model.RefreshTokenResponse;
import com.hitachi.coe.fullstack.service.impl.AuthenticationServiceImpl;
import com.hitachi.coe.fullstack.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

/**
 * Unit test for {@link AuthenticationServiceImpl}
 *
 * @author tminhto
 * @see AuthenticationServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtUtils jwtUtils;

    @InjectMocks
    AuthenticationServiceImpl authenticationService;

    @Test
    void testLogin_whenCredentialsValid_thenReturnLoginResponse() {
        // prepare
        String username = "test_user";
        String password = "test_password";
        String accessToken = "test_access_token";
        String refreshToken = "test_refresh_token";
        LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();
        UserDetails userDetails = new User(username, password, new ArrayList<>());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        // when-then
        Mockito.when(authenticationManager.authenticate(authenticationToken)).thenReturn(authentication);
        Mockito.when(jwtUtils.generateJwtAccessToken(userDetails.getUsername())).thenReturn(accessToken);
        Mockito.when(jwtUtils.generateJwtRefreshToken(Mockito.anyMap(), Mockito.eq(userDetails.getUsername()))).thenReturn(refreshToken);
        // evoke
        LoginResponse response = authenticationService.login(loginRequest);
        // assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals(username, response.getUsername());
        Assertions.assertEquals(accessToken, response.getAccessToken());
        Assertions.assertEquals(refreshToken, response.getRefreshToken());
        // verify
        Mockito.verify(authenticationManager, Mockito.times(1)).authenticate(authenticationToken);
        Mockito.verify(jwtUtils, Mockito.times(1)).generateJwtAccessToken(userDetails.getUsername());
        Mockito.verify(jwtUtils, Mockito.times(1)).generateJwtRefreshToken(Mockito.anyMap(), Mockito.eq(userDetails.getUsername()));
    }

    @Test
    void testLogin_whenCredentialsInvalid_thenThrowBadCredentialsException() {
        // prepare
        String username = "test_user";
        String password = "test_password";
        LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        // when-then
        Mockito.when(authenticationManager.authenticate(authenticationToken))
                .thenThrow(new BadCredentialsException("Bad credentials"));
        // evoke & assert
        Assertions.assertThrows(BadCredentialsException.class, () -> authenticationService.login(loginRequest));
        // verify
        Mockito.verify(authenticationManager, Mockito.times(1)).authenticate(authenticationToken);
    }

    @Test
    void testRefreshToken_whenValidRefreshTokenProvided_thenGenerateNewAccessTokenAndRefreshToken() {
        // prepare
        String refreshToken = "valid_refresh_token";
        String username = "test_user";
        String accessToken = "new_access_token";
        String newRefreshToken = "new_refresh_token";
        Claims claims = Mockito.mock(Claims.class);
        claims.setSubject(username);
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(refreshToken);
        Mockito.when(jwtUtils.validateJwtToken(refreshToken)).thenReturn(true);
        Mockito.when(jwtUtils.extractAllClaims(refreshToken)).thenReturn(claims);
        Mockito.when(claims.getSubject()).thenReturn(username);
        Mockito.when(jwtUtils.generateJwtAccessToken(username)).thenReturn(accessToken);
        Mockito.when(jwtUtils.generateJwtRefreshToken(username)).thenReturn(newRefreshToken);
        // evoke
        RefreshTokenResponse response = authenticationService.refreshToken(refreshTokenRequest);
        // assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals(accessToken, response.getAccessToken());
        Assertions.assertEquals(newRefreshToken, response.getRefreshToken());
        // verify
        Mockito.verify(jwtUtils, Mockito.times(1)).validateJwtToken(refreshToken);
        Mockito.verify(jwtUtils, Mockito.times(1)).extractAllClaims(refreshToken);
        Mockito.verify(jwtUtils, Mockito.times(1)).generateJwtAccessToken(username);
        Mockito.verify(jwtUtils, Mockito.times(1)).generateJwtRefreshToken(username);
    }

    @Test
    void testRefreshToken_whenInvalidRefreshTokenProvided_thenThrowJwtException() throws JwtException {
        // prepare
        String refreshToken = "invalid_refresh_token";
        String username = "test_user";
        Claims claims = Mockito.mock(Claims.class);
        claims.setSubject(username);
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(refreshToken);
        // evoke
        Mockito.when(jwtUtils.validateJwtToken(refreshToken)).thenReturn(false);
        Mockito.when(jwtUtils.extractAllClaims(refreshToken)).thenReturn(claims);
        // assert
        Assertions.assertThrows(JwtException.class, () -> authenticationService.refreshToken(refreshTokenRequest));
        // verify
        Mockito.verify(jwtUtils, Mockito.times(1)).validateJwtToken(refreshToken);
        Mockito.verify(jwtUtils, Mockito.times(1)).extractAllClaims(Mockito.anyString());
        Mockito.verify(claims, Mockito.never()).getSubject();
        Mockito.verify(jwtUtils, Mockito.never()).generateJwtAccessToken(Mockito.anyString());
        Mockito.verify(jwtUtils, Mockito.never()).generateJwtRefreshToken(Mockito.anyString());
    }
}
