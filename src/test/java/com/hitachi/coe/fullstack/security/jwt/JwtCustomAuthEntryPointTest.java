package com.hitachi.coe.fullstack.security.jwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@ExtendWith(MockitoExtension.class)
class JwtCustomAuthEntryPointTest {
    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    AuthenticationException authException;

    @Mock
    ServletOutputStream servletOutputStream;

    @InjectMocks
    JwtCustomAuthEntryPoint jwtCustomAuthEntryPoint;

    @Test
    void testCommence_shouldReturnCustomErrorResponse() throws IOException, ServletException {
        // when-then
        Mockito.when((OutputStream) response.getOutputStream()).thenReturn(servletOutputStream);
        // invoke
        jwtCustomAuthEntryPoint.commence(request, response, authException);
        // verify
        Mockito.verify(response, Mockito.times(1)).setContentType(MediaType.APPLICATION_JSON_VALUE);
        Mockito.verify(response, Mockito.times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Mockito.verify(servletOutputStream, Mockito.times(1)).flush();
    }

    @Test
    void testCommence_shouldReturnCustomErrorResponse_whenAuthExceptionIsInstanceOfBadCredentialsException() throws IOException, ServletException {
        // prepare
        AuthenticationException authException = new BadCredentialsException("Invalid credentials");
        // when-then
        Mockito.when((OutputStream) response.getOutputStream()).thenReturn(servletOutputStream);
        // invoke
        jwtCustomAuthEntryPoint.commence(request, response, authException);
        // verify
        Mockito.verify(response, Mockito.times(1)).setContentType(MediaType.APPLICATION_JSON_VALUE);
        Mockito.verify(response, Mockito.times(1)).setStatus(HttpStatus.UNAUTHORIZED.value());
        Mockito.verify(servletOutputStream, Mockito.times(1)).flush();
    }

    @Test
    void testCommence_shouldReturnCustomErrorResponse_whenAuthExceptionIsInstanceOfUsernameNotFoundException() throws IOException, ServletException {
        // prepare
        AuthenticationException authException = new UsernameNotFoundException("Invalid credentials");
        // when-then
        Mockito.when((OutputStream) response.getOutputStream()).thenReturn(servletOutputStream);
        // invoke
        jwtCustomAuthEntryPoint.commence(request, response, authException);
        // verify
        Mockito.verify(response, Mockito.times(1)).setContentType(MediaType.APPLICATION_JSON_VALUE);
        Mockito.verify(response, Mockito.times(1)).setStatus(HttpStatus.UNAUTHORIZED.value());
        Mockito.verify(servletOutputStream, Mockito.times(1)).flush();
    }

}
