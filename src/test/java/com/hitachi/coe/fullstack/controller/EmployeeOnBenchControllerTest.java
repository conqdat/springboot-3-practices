package com.hitachi.coe.fullstack.controller;

import com.hitachi.coe.fullstack.model.EmployeeOnBenchModel;
import com.hitachi.coe.fullstack.service.EmployeeOnBenchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-data-test.properties")
class EmployeeOnBenchControllerTest {
    @Mock
    private EmployeeOnBenchService employeeOnBenchService;

    private EmployeeOnBenchModel employeeOnBenchModel;

    @BeforeEach
    void setUp() {
        employeeOnBenchModel = mock(EmployeeOnBenchModel.class);
    }

    @Test
    void testGetAllEmployeeOnBench() {
        List<EmployeeOnBenchModel> employees = Arrays.asList(employeeOnBenchModel, employeeOnBenchModel);
        Page<EmployeeOnBenchModel> mockPage = new PageImpl<>(employees);

        int no = 0;
        int limit = 10;
        String sortBy = "created";
        Boolean desc = true;

        when(employeeOnBenchService.getAllEmployeeOnBench(anyInt(), anyInt(), anyString(), anyBoolean())).thenReturn(mockPage);

        Page<EmployeeOnBenchModel> result = employeeOnBenchService.getAllEmployeeOnBench(no, limit, sortBy, desc);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.getTotalElements());
        assertEquals(employees, result.getContent());

        verify(employeeOnBenchService).getAllEmployeeOnBench(no, limit, sortBy, desc);
    }
}
