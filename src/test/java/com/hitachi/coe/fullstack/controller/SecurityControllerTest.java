package com.hitachi.coe.fullstack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.model.LoginRequest;
import com.hitachi.coe.fullstack.model.LoginResponse;
import com.hitachi.coe.fullstack.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityControllerTest {

    @MockBean
    AuthenticationService authenticationService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void testLogin_whenCredentialsAreValid_thenReturnSuccessBaseResponse() throws Exception {
        final LoginRequest loginRequest = LoginRequest.builder()
                .username("john")
                .password("1221")
                .build();
        final LoginResponse loginResponse = LoginResponse.builder()
                .refreshToken("test_refresh_token")
                .accessToken("test_access_token")
                .username("guest")
                .build();
        Mockito.when(authenticationService.login(loginRequest)).thenReturn(loginResponse);
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.accessToken").value(loginResponse.getAccessToken()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.refreshToken").value(loginResponse.getRefreshToken()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testLogin_whenUsernameIsMissing_thenReturnBadRequest() throws Exception {
        final LoginRequest loginRequest = LoginRequest.builder()
                .password("1221")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorConstant.MESSAGE_BAD_CREDENTIALS))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testLogin_whenPasswordIsMissing_thenReturnBadRequest() throws Exception {
        final LoginRequest loginRequest = LoginRequest.builder()
                .username("john")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorConstant.MESSAGE_BAD_CREDENTIALS))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testLogin_whenCredentialsAreInvalid_thenReturnUnauthorizedResponse() throws Exception {
        final LoginRequest loginRequest = LoginRequest.builder()
                .username("john")
                .password("invalid_password")
                .build();
        Mockito.when(authenticationService.login(loginRequest)).thenThrow(new BadCredentialsException("Invalid username or password"));
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("401"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorConstant.MESSAGE_BAD_CREDENTIALS))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testLogin_whenAuthenticationServiceThrowsException_thenReturnInternalServerError() throws Exception {
        final LoginRequest loginRequest = LoginRequest.builder()
                .username("john")
                .password("1221")
                .build();
        Mockito.when(authenticationService.login(loginRequest)).thenThrow(new RuntimeException("Internal server error"));
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("500"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Internal server error"))
                .andDo(MockMvcResultHandlers.print());
    }
}
