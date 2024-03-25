package com.hitachi.coe.fullstack.controller;

import com.hitachi.coe.fullstack.model.IBarChartOnBenchModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import com.hitachi.coe.fullstack.service.EmployeeOnBenchDetailService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-data-test.properties")
class EmployeeOnBenchDetailControllerTest {
    @MockBean
    EmployeeOnBenchDetailService employeeOnBenchDetailService;

    @Autowired
    EmployeeOnBenchDetailController employeeOnBenchDetailController;

    @Test
    void testGetQuantityOfEmployeeOnBenchByBusinessUnit_whenNullMonth_thenReturnResult() {
        Integer month = null;
        List<IBarChartOnBenchModel> expected = new ArrayList<IBarChartOnBenchModel>();
        Mockito.when(employeeOnBenchDetailService.getQuantityOfEmployeeOnBenchByBusinessUnit(month)).thenReturn(expected);

        ResponseEntity<List<IBarChartOnBenchModel>> response = employeeOnBenchDetailController.getQuantityOfEmployeeOnBenchByBusinessUnit(month);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expected, response.getBody());
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByBusinessUnit_whenValidMonth_thenReturnResult() {
        Integer month = 1;
        List<IBarChartOnBenchModel> expected = new ArrayList<IBarChartOnBenchModel>();
        Mockito.when(employeeOnBenchDetailService.getQuantityOfEmployeeOnBenchByBusinessUnit(month)).thenReturn(expected);

        ResponseEntity<List<IBarChartOnBenchModel>> response = employeeOnBenchDetailController.getQuantityOfEmployeeOnBenchByBusinessUnit(month);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expected, response.getBody());
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByBusinessUnit_whenEmptyResponse_thenReturnEmptyResult() {
        Integer month = 1;
        List<IBarChartOnBenchModel> expected = new ArrayList<>(Collections.emptyList());

        Mockito.when(employeeOnBenchDetailService.getQuantityOfEmployeeOnBenchByBusinessUnit(month)).thenReturn(expected);

        ResponseEntity<List<IBarChartOnBenchModel>> response = employeeOnBenchDetailController.getQuantityOfEmployeeOnBenchByBusinessUnit(month);

        assertNotNull(response);
        assertTrue(response.getBody().isEmpty());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByLocation_whenNullMonth_thenReturnResult() {
        Integer month = null;
        List<IBarChartOnBenchModel> expected = new ArrayList<IBarChartOnBenchModel>();
        Mockito.when(employeeOnBenchDetailService.getQuantityOfEmployeesOnBenchByLocation(month)).thenReturn(expected);

        ResponseEntity<List<IBarChartOnBenchModel>> response = employeeOnBenchDetailController.getQuantityOfEmployeesOnBenchByLocation(month);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expected, response.getBody());
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByLocation_whenValidMonth_thenReturnResult() {
        Integer month = 1;
        List<IBarChartOnBenchModel> expected = new ArrayList<IBarChartOnBenchModel>();
        Mockito.when(employeeOnBenchDetailService.getQuantityOfEmployeesOnBenchByLocation(month)).thenReturn(expected);

        ResponseEntity<List<IBarChartOnBenchModel>> response = employeeOnBenchDetailController.getQuantityOfEmployeesOnBenchByLocation(month);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expected, response.getBody());
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByLocation_whenEmptyResponse_thenReturnEmptyResult() {
        Integer month = 1;
        List<IBarChartOnBenchModel> expected = new ArrayList<>(Collections.emptyList());

        Mockito.when(employeeOnBenchDetailService.getQuantityOfEmployeesOnBenchByLocation(month)).thenReturn(expected);

        ResponseEntity<List<IBarChartOnBenchModel>> response = employeeOnBenchDetailController.getQuantityOfEmployeesOnBenchByLocation(month);

        assertNotNull(response);
        assertTrue(response.getBody().isEmpty());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByLevel_whenNullMonth_thenReturnResult() {
        Integer month = null;
        List<IBarChartOnBenchModel> expected = new ArrayList<IBarChartOnBenchModel>();
        Mockito.when(employeeOnBenchDetailService.getQuantityOfEmployeesOnBenchByLevel(month)).thenReturn(expected);

        ResponseEntity<List<IBarChartOnBenchModel>> response = employeeOnBenchDetailController.getQuantityOfEmployeesOnBenchByLevel(month);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expected, response.getBody());
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByLevel_whenValidMonth_thenReturnResult() {
        Integer month = 1;
        List<IBarChartOnBenchModel> expected = new ArrayList<IBarChartOnBenchModel>();
        Mockito.when(employeeOnBenchDetailService.getQuantityOfEmployeesOnBenchByLevel(month)).thenReturn(expected);

        ResponseEntity<List<IBarChartOnBenchModel>> response = employeeOnBenchDetailController.getQuantityOfEmployeesOnBenchByLevel(month);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expected, response.getBody());
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByLevel_whenEmptyResponse_thenReturnEmptyResult() {
        Integer month = 1;
        List<IBarChartOnBenchModel> expected = new ArrayList<>(Collections.emptyList());

        Mockito.when(employeeOnBenchDetailService.getQuantityOfEmployeesOnBenchByLevel(month)).thenReturn(expected);

        ResponseEntity<List<IBarChartOnBenchModel>> response = employeeOnBenchDetailController.getQuantityOfEmployeesOnBenchByLevel(month);

        assertNotNull(response);
        assertTrue(response.getBody().isEmpty());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByCoe_whenNullMonth_thenReturnResult() {
        Integer month = null;
        List<IBarChartOnBenchModel> expected = new ArrayList<IBarChartOnBenchModel>();
        Mockito.when(employeeOnBenchDetailService.getQuantityOfEmployeesOnBenchByCoe(month)).thenReturn(expected);

        ResponseEntity<List<IBarChartOnBenchModel>> response = employeeOnBenchDetailController.getQuantityOfEmployeesOnBenchByCoe(month);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expected, response.getBody());
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByCoe_whenValidMonth_thenReturnResult() {
        Integer month = 1;
        List<IBarChartOnBenchModel> expected = new ArrayList<IBarChartOnBenchModel>();
        Mockito.when(employeeOnBenchDetailService.getQuantityOfEmployeesOnBenchByCoe(month)).thenReturn(expected);

        ResponseEntity<List<IBarChartOnBenchModel>> response = employeeOnBenchDetailController.getQuantityOfEmployeesOnBenchByCoe(month);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expected, response.getBody());
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByCoe_whenEmptyResponse_thenReturnEmptyResult() {
        Integer month = 1;
        List<IBarChartOnBenchModel> expected = new ArrayList<>(Collections.emptyList());

        Mockito.when(employeeOnBenchDetailService.getQuantityOfEmployeesOnBenchByCoe(month)).thenReturn(expected);

        ResponseEntity<List<IBarChartOnBenchModel>> response = employeeOnBenchDetailController.getQuantityOfEmployeesOnBenchByCoe(month);

        assertNotNull(response);
        assertTrue(response.getBody().isEmpty());
        assertEquals(0, response.getBody().size());
    }
}
