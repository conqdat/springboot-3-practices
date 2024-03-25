package com.hitachi.coe.fullstack.model;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmployeeOnBenchDetailModelTest {
    EmployeeOnBenchDetailModel employeeOnBenchDetailModel;
    private Long testId;
    private Integer benchDays;
    private Date dateOfJoin;
    private Date statusChangeDate;
    private Integer categoryCode;
    private Integer employeeId;
    private Integer employeeOnBenchId;
    private EmployeeOnBenchModel employeeOnBenchModel;

    @BeforeEach
    void setupEach() {
        employeeOnBenchDetailModel = new EmployeeOnBenchDetailModel();
        testId = 1L;
        benchDays = 10;
        dateOfJoin = new Date();
        statusChangeDate = new Date();
        categoryCode = 100;
        employeeId = 123;
        employeeOnBenchId = 456;
        employeeOnBenchModel = new EmployeeOnBenchModel();
        employeeOnBenchModel.setId(1);
        employeeOnBenchModel.setName("testName");
    }

    @Test
    void testEmployeeOnBenchDetailModel_GetterSetter() {
        // prepare
        employeeOnBenchDetailModel.setId(testId);
        employeeOnBenchDetailModel.setBenchDays(benchDays);
        employeeOnBenchDetailModel.setDateOfJoin(dateOfJoin);
        employeeOnBenchDetailModel.setStatusChangeDate(statusChangeDate);
        employeeOnBenchDetailModel.setCategoryCode(categoryCode);
        employeeOnBenchDetailModel.setEmployeeId(employeeId);
        employeeOnBenchDetailModel.setEmployeeOnBenchId(employeeOnBenchId);
        employeeOnBenchDetailModel.setEmployeeOnBenchModel(employeeOnBenchModel);
        // assert
        Assertions.assertEquals(testId, employeeOnBenchDetailModel.getId());
        Assertions.assertEquals(benchDays, employeeOnBenchDetailModel.getBenchDays());
        Assertions.assertEquals(dateOfJoin, employeeOnBenchDetailModel.getDateOfJoin());
        Assertions.assertEquals(statusChangeDate, employeeOnBenchDetailModel.getStatusChangeDate());
        Assertions.assertEquals(categoryCode, employeeOnBenchDetailModel.getCategoryCode());
        Assertions.assertEquals(employeeId, employeeOnBenchDetailModel.getEmployeeId());
        Assertions.assertEquals(employeeOnBenchId, employeeOnBenchDetailModel.getEmployeeOnBenchId());
        Assertions.assertEquals(employeeOnBenchModel, employeeOnBenchDetailModel.getEmployeeOnBenchModel());
    }

    @Test
    void testEmployeeOnBenchDetailModel_Builder() {
        // prepare
        employeeOnBenchDetailModel = EmployeeOnBenchDetailModel.builder()
                .id(testId)
                .benchDays(benchDays)
                .dateOfJoin(dateOfJoin)
                .statusChangeDate(statusChangeDate)
                .categoryCode(categoryCode)
                .employeeId(employeeId)
                .employeeOnBenchId(employeeOnBenchId)
                .employeeOnBenchModel(employeeOnBenchModel)
                .build();
        // assert
        Assertions.assertEquals(testId, employeeOnBenchDetailModel.getId());
        Assertions.assertEquals(benchDays, employeeOnBenchDetailModel.getBenchDays());
        Assertions.assertEquals(dateOfJoin, employeeOnBenchDetailModel.getDateOfJoin());
        Assertions.assertEquals(statusChangeDate, employeeOnBenchDetailModel.getStatusChangeDate());
        Assertions.assertEquals(categoryCode, employeeOnBenchDetailModel.getCategoryCode());
        Assertions.assertEquals(employeeId, employeeOnBenchDetailModel.getEmployeeId());
        Assertions.assertEquals(employeeOnBenchId, employeeOnBenchDetailModel.getEmployeeOnBenchId());
        Assertions.assertEquals(employeeOnBenchModel, employeeOnBenchDetailModel.getEmployeeOnBenchModel());
    }

    @Test
    void testEmployeeOnBenchDetailModel_AllArgsConstructor() {
        // prepare
        employeeOnBenchDetailModel = new EmployeeOnBenchDetailModel(testId, benchDays, dateOfJoin, statusChangeDate,
                categoryCode, employeeId, employeeOnBenchId, employeeOnBenchModel);
        // assert
        Assertions.assertEquals(testId, employeeOnBenchDetailModel.getId());
        Assertions.assertEquals(benchDays, employeeOnBenchDetailModel.getBenchDays());
        Assertions.assertEquals(dateOfJoin, employeeOnBenchDetailModel.getDateOfJoin());
        Assertions.assertEquals(statusChangeDate, employeeOnBenchDetailModel.getStatusChangeDate());
        Assertions.assertEquals(categoryCode, employeeOnBenchDetailModel.getCategoryCode());
        Assertions.assertEquals(employeeId, employeeOnBenchDetailModel.getEmployeeId());
        Assertions.assertEquals(employeeOnBenchId, employeeOnBenchDetailModel.getEmployeeOnBenchId());
        Assertions.assertEquals(employeeOnBenchModel, employeeOnBenchDetailModel.getEmployeeOnBenchModel());
    }
}
