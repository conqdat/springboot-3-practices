package com.hitachi.coe.fullstack.security.jwt;

import com.hitachi.coe.fullstack.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {
    @Mock
    JwtUtils jwtUtils;

    @Mock
    UserDetailsService userDetailsService;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterChain filterChain;

    @InjectMocks
    JwtAuthenticationFilter jwtAuthenticationFilter;


    @Test
    void testDoFilterInternal_whenAuthorizationHeaderMissingOrInvalid_thenClearSecurityContextAndDoFilter() throws ServletException, IOException {
        // when-then
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);
        // invoke
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        // verify
        Mockito.verify(jwtUtils, Mockito.never()).extractClaim("test_token", Claims::getSubject);
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_whenAuthorizationHeaderDoesNotStartWithBearer_thenClearSecurityContextAndDoFilter() throws ServletException, IOException {
        // prepare
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("wrong prefix_test");
        // invoke
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        // verify
        Mockito.verify(jwtUtils, Mockito.never()).extractClaim("test_token", Claims::getSubject);
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_whenAuthorizationHeaderIsEmpty_thenClearSecurityContextAndDoFilter() throws ServletException, IOException {
        // prepare
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer ");
        // invoke
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        // verify
        Mockito.verify(jwtUtils, Mockito.never()).extractUsername("test_token");
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_whenJwtTokenIsInvalid_thenClearSecurityContextAndThrowJwtException() throws ServletException, IOException {
        // prepare
        String token = "invalid_token";
        // when-then
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        Mockito.when(jwtUtils.extractUsername(token)).thenThrow(new SignatureException("Invalid signature"));
        // invoke & assert
        Assertions.assertThrows(JwtException.class, () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));
        // verify
        Mockito.verify(jwtUtils, Mockito.times(1)).extractUsername(token);
        Mockito.verify(filterChain, Mockito.never()).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_whenJwtTokenIsValid_thenSetAuthenticationInSecurityContextAndDoFilter() throws ServletException, IOException {
        // prepare
        String token = "test_token";
        String username = "test_user";
        UserDetails userDetails = new User(username, "test_password", new ArrayList<>());
        // when-then
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        Mockito.when(jwtUtils.extractUsername(token)).thenReturn(username);
        Mockito.when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        Mockito.when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        // invoke
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        // verify
        Mockito.verify(jwtUtils, Mockito.times(1)).extractUsername(token);
        Mockito.verify(userDetailsService, Mockito.times(1)).loadUserByUsername(username);
        Mockito.verify(request).setAttribute(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }
}
