package com.hitachi.coe.fullstack.service;

import com.hitachi.coe.fullstack.model.IBarChartOnBenchModel;
import com.hitachi.coe.fullstack.repository.EmployeeOnBenchDetailRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-data-test.properties")
public class EmployeeOnBenchDetailServiceTest {

    @MockBean
    private EmployeeOnBenchDetailRepository employeeOnBenchDetailRepository;

    @Autowired
    private EmployeeOnBenchDetailService employeeOnBenchDetailService;

    @Test
    void testGetQuantityOfEmployeeOnBenchByBusinessUnit_whenNullMonth_thenReturnResult() {
        Integer month = null;
        LocalDate date = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);

        List<IBarChartOnBenchModel> expected = new ArrayList<IBarChartOnBenchModel>();

        Mockito.when(employeeOnBenchDetailRepository.getQuantityOfEmployeeOnBenchByBusinessUnit(date)).thenReturn(expected);

        List<IBarChartOnBenchModel> response = employeeOnBenchDetailService.getQuantityOfEmployeeOnBenchByBusinessUnit(month);

        assertNotNull(response);
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByBusinessUnit_whenValidMonth_thenReturnResult() {
        Integer month = 202301;
        LocalDate date = LocalDate.of(parseInt(month.toString().substring(0, 4)), parseInt(month.toString().substring(month.toString().length() - 2)), 1);

        List<IBarChartOnBenchModel> expected = new ArrayList<IBarChartOnBenchModel>();

        Mockito.when(employeeOnBenchDetailRepository.getQuantityOfEmployeeOnBenchByBusinessUnit(date)).thenReturn(expected);

        List<IBarChartOnBenchModel> response = employeeOnBenchDetailService.getQuantityOfEmployeeOnBenchByBusinessUnit(month);

        assertNotNull(response);
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByBusinessUnit_whenEmptyResponse_thenReturnEmptyResult() {
        Integer month = 202301;
        LocalDate date = LocalDate.of(parseInt(month.toString().substring(0, 4)), parseInt(month.toString().substring(month.toString().length() - 2)), 1);

        List<IBarChartOnBenchModel> expected = new ArrayList<>(Collections.emptyList());

        Mockito.when(employeeOnBenchDetailRepository.getQuantityOfEmployeeOnBenchByBusinessUnit(date)).thenReturn(expected);

        List<IBarChartOnBenchModel> response = employeeOnBenchDetailService.getQuantityOfEmployeeOnBenchByBusinessUnit(month);

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByLocation_whenNullMonth_thenReturnResult() {
        Integer month = null;
        LocalDate date = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);

        List<IBarChartOnBenchModel> expected = new ArrayList<IBarChartOnBenchModel>();

        Mockito.when(employeeOnBenchDetailRepository.getQuantityOfEmployeesOnBenchByLocation(date)).thenReturn(expected);

        List<IBarChartOnBenchModel> response = employeeOnBenchDetailService.getQuantityOfEmployeesOnBenchByLocation(month);

        assertNotNull(response);
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByLocation_whenValidMonth_thenReturnResult() {
        Integer month = 202301;
        LocalDate date = LocalDate.of(parseInt(month.toString().substring(0, 4)), parseInt(month.toString().substring(month.toString().length() - 2)), 1);

        List<IBarChartOnBenchModel> expected = new ArrayList<IBarChartOnBenchModel>();

        Mockito.when(employeeOnBenchDetailRepository.getQuantityOfEmployeesOnBenchByLocation(date)).thenReturn(expected);

        List<IBarChartOnBenchModel> response = employeeOnBenchDetailService.getQuantityOfEmployeesOnBenchByLocation(month);

        assertNotNull(response);
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByLocation_whenEmptyResponse_thenReturnEmptyResult() {
        Integer month = 202301;
        LocalDate date = LocalDate.of(parseInt(month.toString().substring(0, 4)), parseInt(month.toString().substring(month.toString().length() - 2)), 1);

        List<IBarChartOnBenchModel> expected = new ArrayList<>(Collections.emptyList());

        Mockito.when(employeeOnBenchDetailRepository.getQuantityOfEmployeesOnBenchByLocation(date)).thenReturn(expected);

        List<IBarChartOnBenchModel> response = employeeOnBenchDetailService.getQuantityOfEmployeesOnBenchByLocation(month);

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByLevel_whenNullMonth_thenReturnResult() {
        Integer month = null;
        LocalDate date = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);

        List<IBarChartOnBenchModel> expected = new ArrayList<IBarChartOnBenchModel>();

        Mockito.when(employeeOnBenchDetailRepository.getQuantityOfEmployeesOnBenchByLevel(date)).thenReturn(expected);

        List<IBarChartOnBenchModel> response = employeeOnBenchDetailService.getQuantityOfEmployeesOnBenchByLevel(month);

        assertNotNull(response);
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByLevel_whenValidMonth_thenReturnResult() {
        Integer month = 202301;
        LocalDate date = LocalDate.of(parseInt(month.toString().substring(0, 4)), parseInt(month.toString().substring(month.toString().length() - 2)), 1);

        List<IBarChartOnBenchModel> expected = new ArrayList<IBarChartOnBenchModel>();

        Mockito.when(employeeOnBenchDetailRepository.getQuantityOfEmployeesOnBenchByLevel(date)).thenReturn(expected);

        List<IBarChartOnBenchModel> response = employeeOnBenchDetailService.getQuantityOfEmployeesOnBenchByLevel(month);

        assertNotNull(response);
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByLevel_whenEmptyResponse_thenReturnEmptyResult() {
        Integer month = 202301;
        LocalDate date = LocalDate.of(parseInt(month.toString().substring(0, 4)), parseInt(month.toString().substring(month.toString().length() - 2)), 1);

        List<IBarChartOnBenchModel> expected = new ArrayList<>(Collections.emptyList());

        Mockito.when(employeeOnBenchDetailRepository.getQuantityOfEmployeesOnBenchByLevel(date)).thenReturn(expected);

        List<IBarChartOnBenchModel> response = employeeOnBenchDetailService.getQuantityOfEmployeesOnBenchByLevel(month);

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByCoe_whenNullMonth_thenReturnResult() {
        Integer month = null;
        LocalDate date = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);

        List<IBarChartOnBenchModel> expected = new ArrayList<IBarChartOnBenchModel>();

        Mockito.when(employeeOnBenchDetailRepository.getQuantityOfEmployeesOnBenchByCoe(date)).thenReturn(expected);

        List<IBarChartOnBenchModel> response = employeeOnBenchDetailService.getQuantityOfEmployeesOnBenchByCoe(month);

        assertNotNull(response);
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByCoe_whenValidMonth_thenReturnResult() {
        Integer month = 202301;
        LocalDate date = LocalDate.of(parseInt(month.toString().substring(0, 4)), parseInt(month.toString().substring(month.toString().length() - 2)), 1);

        List<IBarChartOnBenchModel> expected = new ArrayList<IBarChartOnBenchModel>();

        Mockito.when(employeeOnBenchDetailRepository.getQuantityOfEmployeesOnBenchByCoe(date)).thenReturn(expected);

        List<IBarChartOnBenchModel> response = employeeOnBenchDetailService.getQuantityOfEmployeesOnBenchByCoe(month);

        assertNotNull(response);
    }

    @Test
    void testGetQuantityOfEmployeeOnBenchByCoe_whenEmptyResponse_thenReturnEmptyResult() {
        Integer month = 202301;
        LocalDate date = LocalDate.of(parseInt(month.toString().substring(0, 4)), parseInt(month.toString().substring(month.toString().length() - 2)), 1);

        List<IBarChartOnBenchModel> expected = new ArrayList<>(Collections.emptyList());

        Mockito.when(employeeOnBenchDetailRepository.getQuantityOfEmployeesOnBenchByCoe(date)).thenReturn(expected);

        List<IBarChartOnBenchModel> response = employeeOnBenchDetailService.getQuantityOfEmployeesOnBenchByCoe(month);

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }
}
