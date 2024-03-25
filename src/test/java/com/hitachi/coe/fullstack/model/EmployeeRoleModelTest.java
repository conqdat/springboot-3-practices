package com.hitachi.coe.fullstack.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeRoleModelTest {
    EmployeeRoleModel employeeRoleModel;
    Integer testId;
    String testName;
    String testCode;
    String testDescription;
    String testToString;

    @BeforeEach
    void setupEach() {
        employeeRoleModel = new EmployeeRoleModel();
        testId = 1;
        testName = "testName";
        testCode = "testCode";
        testDescription = "testDescription";
        testToString = employeeRoleModel.toString();
    }

    @Test
    void testEmployeeRoleModel_GetterSetter() {
        // prepare
        employeeRoleModel.setId(testId);
        employeeRoleModel.setName(testName);
        employeeRoleModel.setCode(testCode);
        employeeRoleModel.setDescription(testDescription);
        // assert
        Assertions.assertEquals(testId, employeeRoleModel.getId());
        Assertions.assertEquals(testName, employeeRoleModel.getName());
        Assertions.assertEquals(testCode, employeeRoleModel.getCode());
        Assertions.assertEquals(testDescription, employeeRoleModel.getDescription());
    }

    @Test
    void testEmployeeRoleModel_Builder() {
        // prepare
        employeeRoleModel = EmployeeRoleModel.builder()
                .id(testId)
                .name(testName)
                .code(testCode)
                .description(testDescription)
                .build();
        // assert
        Assertions.assertEquals(testId, employeeRoleModel.getId());
        Assertions.assertEquals(testName, employeeRoleModel.getName());
        Assertions.assertEquals(testCode, employeeRoleModel.getCode());
        Assertions.assertEquals(testDescription, employeeRoleModel.getDescription());
    }

    @Test
    void testEmployeeRoleModel_AllArgsConstructor() {
        // prepare
        employeeRoleModel = new EmployeeRoleModel(testId, testCode, testName, testDescription);
        // assert
        Assertions.assertEquals(testId, employeeRoleModel.getId());
        Assertions.assertEquals(testName, employeeRoleModel.getName());
        Assertions.assertEquals(testCode, employeeRoleModel.getCode());
        Assertions.assertEquals(testDescription, employeeRoleModel.getDescription());
    }

    @Test
    void testEmployeeRoleModel_ToString() {
        // assert
        Assertions.assertEquals(testToString, employeeRoleModel.toString());
    }
}
