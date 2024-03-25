package com.hitachi.coe.fullstack.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmployeeImportModelTest {
    EmployeeImportModel employeeImportModel;
    private Integer id;
    private String name;
    private ImportOperationType type;
    private ImportOperationStatus status;
    private Object message;

    @BeforeEach
    void setupEach() {
        employeeImportModel = new EmployeeImportModel();
        id = 1;
        name = "testName";
        type = ImportOperationType.EMPLOYEE_ADD;
        status = ImportOperationStatus.SUCCESS;
        message = new Object();
    }

    @Test
    void testEmployeeOnBenchModel_GetterSetter() {
        // prepare
        employeeImportModel.setId(id);
        employeeImportModel.setName(name);
        employeeImportModel.setType(type);
        employeeImportModel.setStatus(status);
        employeeImportModel.setMessage(message);
        // assert
        Assertions.assertEquals(id, employeeImportModel.getId());
        Assertions.assertEquals(name, employeeImportModel.getName());
        Assertions.assertEquals(type, employeeImportModel.getType());
        Assertions.assertEquals(status, employeeImportModel.getStatus());
        Assertions.assertEquals(message, employeeImportModel.getMessage());
    }

    @Test
    void testEmployeeOnBenchModel_Builder() {
        // prepare
        employeeImportModel = EmployeeImportModel.builder()
                .id(id)
                .name(name)
                .type(type)
                .status(status)
                .message(message)
                .build();
        // assert
        Assertions.assertEquals(id, employeeImportModel.getId());
        Assertions.assertEquals(name, employeeImportModel.getName());
        Assertions.assertEquals(type, employeeImportModel.getType());
        Assertions.assertEquals(status, employeeImportModel.getStatus());
        Assertions.assertEquals(message, employeeImportModel.getMessage());
    }

    @Test
    void testEmployeeOnBenchModel_AllArgsConstructor() {
        // prepare
        employeeImportModel = new EmployeeImportModel(id, name, type, status, message);
        // assert
        Assertions.assertEquals(id, employeeImportModel.getId());
        Assertions.assertEquals(name, employeeImportModel.getName());
        Assertions.assertEquals(type, employeeImportModel.getType());
        Assertions.assertEquals(status, employeeImportModel.getStatus());
        Assertions.assertEquals(message, employeeImportModel.getMessage());
    }
}
