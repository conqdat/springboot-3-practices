package com.hitachi.coe.fullstack.security.jwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AuthorizationFilterTest {
    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterChain filterChain;

    @InjectMocks
    AuthorizationFilter authorizationFilter;

    @Test
    void testDoFilterInternal_whenAuthorizationHeaderIsInvalid() throws ServletException, IOException {
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);
        authorizationFilter.doFilterInternal(request, response, filterChain);
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_whenAuthorizationHeaderHasWrongPrefix() throws ServletException, IOException {
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("wrong-prefix test_token");
        authorizationFilter.doFilterInternal(request, response, filterChain);
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_whenAuthorizationHeaderBearerIsEmpty() throws ServletException, IOException {
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer ");
        authorizationFilter.doFilterInternal(request, response, filterChain);
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_whenUrlHasPermission() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn("http://localhost:8099/fullstack-coe/api/v1/location");
        Mockito.when(request.getMethod()).thenReturn("GET");
        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("GET_LOCATION"), new SimpleGrantedAuthority("POST_LOCATION"));

        String module = request.getRequestURI().split("/")[request.getRequestURI().split("/").length - 1].toUpperCase();
        String fullModule = request.getMethod() + "_" + module;
        GrantedAuthority currentGrantedAuthority = new SimpleGrantedAuthority(fullModule);

        assertTrue(authorities.contains(currentGrantedAuthority));
    }

    @Test
    void testDoFilterInternal_whenUrlHasNotPermission() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn("http://localhost:8099/fullstack-coe/api/v1/location");
        Mockito.when(request.getMethod()).thenReturn("GET");
        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("PUT_LOCATION"), new SimpleGrantedAuthority("POST_LOCATION"));

        String module = request.getRequestURI().split("/")[request.getRequestURI().split("/").length - 1].toUpperCase();
        String fullModule = request.getMethod() + "_" + module;
        GrantedAuthority currentGrantedAuthority = new SimpleGrantedAuthority(fullModule);

        assertFalse(authorities.contains(currentGrantedAuthority));
    }


}
