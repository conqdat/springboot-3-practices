package com.hitachi.coe.fullstack.model;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmployeeOnBenchModelTest {
    EmployeeOnBenchModel employeeOnBenchModel;
    Integer testId;
    String testName;
    Date testStartDate;
    Date testEndDate;

    @BeforeEach
    void setupEach() {
        employeeOnBenchModel = new EmployeeOnBenchModel();
        testId = 1;
        testName = "testName";
        testStartDate = new Date();
        testEndDate = new Date();
    }

    @Test
    void testEmployeeOnBenchModel_GetterSetter() {
        // prepare
        employeeOnBenchModel.setId(testId);
        employeeOnBenchModel.setName(testName);
        employeeOnBenchModel.setStartDate(testStartDate);
        employeeOnBenchModel.setEndDate(testEndDate);
        // assert
        Assertions.assertEquals(testId, employeeOnBenchModel.getId());
        Assertions.assertEquals(testName, employeeOnBenchModel.getName());
        Assertions.assertEquals(testStartDate, employeeOnBenchModel.getStartDate());
        Assertions.assertEquals(testEndDate, employeeOnBenchModel.getEndDate());
    }

    @Test
    void testEmployeeOnBenchModel_Builder() {
        // prepare
        employeeOnBenchModel = EmployeeOnBenchModel.builder()
                .id(testId)
                .name(testName)
                .startDate(testStartDate)
                .endDate(testEndDate)
                .build();
        // assert
        Assertions.assertEquals(testId, employeeOnBenchModel.getId());
        Assertions.assertEquals(testName, employeeOnBenchModel.getName());
        Assertions.assertEquals(testStartDate, employeeOnBenchModel.getStartDate());
        Assertions.assertEquals(testEndDate, employeeOnBenchModel.getEndDate());
    }

    @Test
    void testEmployeeOnBenchModel_AllArgsConstructor() {
        // prepare
        employeeOnBenchModel = new EmployeeOnBenchModel(testId, testName, testStartDate, testEndDate);
        // assert
        Assertions.assertEquals(testId, employeeOnBenchModel.getId());
        Assertions.assertEquals(testName, employeeOnBenchModel.getName());
        Assertions.assertEquals(testStartDate, employeeOnBenchModel.getStartDate());
        Assertions.assertEquals(testEndDate, employeeOnBenchModel.getEndDate());
    }
}
