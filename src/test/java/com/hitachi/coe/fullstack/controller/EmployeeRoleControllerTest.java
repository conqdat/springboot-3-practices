package com.hitachi.coe.fullstack.controller;

import com.hitachi.coe.fullstack.model.EmployeeRoleModel;
import com.hitachi.coe.fullstack.service.EmployeeRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeRoleControllerTest {

    @MockBean
    EmployeeRoleService employeeRoleService;

    @Autowired
    MockMvc mockMvc;

    EmployeeRoleModel employeeRoleModel;

    List<EmployeeRoleModel> employeeRoleModelList;

    @BeforeEach
    void setUpEach() {
        employeeRoleModel = new EmployeeRoleModel();
        employeeRoleModel.setId(1);
        employeeRoleModel.setCode("code");
        employeeRoleModel.setName("name");
        employeeRoleModel.setDescription("desc");

        employeeRoleModelList = new ArrayList<>();
        employeeRoleModelList.add(employeeRoleModel);
    }

    @Test
    void testGetAllEmployeeRoles_thenReturnListOfEmployeeRoleModel() throws Exception {
        // prepare
        List<EmployeeRoleModel> expectedList = employeeRoleModelList;
        // when-then
        when(employeeRoleService.getAll()).thenReturn(employeeRoleModelList);
        // invoke
        mockMvc.perform(get("/api/v1/employee-roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("List of Employee Roles"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").value(hasSize(1)))
                .andExpect(jsonPath("$.data[0].id").value(expectedList.get(0).getId()))
                .andExpect(jsonPath("$.data[0].code").value(expectedList.get(0).getCode()))
                .andExpect(jsonPath("$.data[0].name").value(expectedList.get(0).getName()))
                .andExpect(jsonPath("$.data[0].description").value(expectedList.get(0).getDescription()))
                .andDo(print());
        // verify
        verify(employeeRoleService).getAll();
    }

    @Test
    void testGetAllEmployeeRoles_thenReturnEmptyListOfEmployeeRoleModel() throws Exception {
        // when-then
        when(employeeRoleService.getAll()).thenReturn(Collections.emptyList());
        // invoke
        mockMvc.perform(get("/api/v1/employee-roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("List of Employee Roles"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").value(hasSize(0)))
                .andDo(print());
        // verify
        verify(employeeRoleService).getAll();
    }
}
