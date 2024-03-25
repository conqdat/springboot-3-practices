package com.hitachi.coe.fullstack.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmployeeImportDetailModelTest {
    EmployeeImportDetailModel employeeImportDetailModel;
    private Long id;
    private Object body;
    private ImportOperationStatus status;
    private Integer lineNum;
    private Object messsageLineList;
    private Integer employeeImportId;
    private EmployeeImportModel employeeImportModel;

    @BeforeEach
    void setupEach() {
        employeeImportDetailModel = new EmployeeImportDetailModel();
        id = 1L;
        body = new Object();
        status = ImportOperationStatus.SUCCESS;
        lineNum = 1;
        messsageLineList = new Object();
        employeeImportId = 1;
        employeeImportModel = new EmployeeImportModel();
    }

    @Test
    void testEmployeeOnBenchModel_GetterSetter() {
        // prepare
        employeeImportDetailModel.setId(id);
        employeeImportDetailModel.setBody(body);
        employeeImportDetailModel.setStatus(status);
        employeeImportDetailModel.setLineNum(lineNum);
        employeeImportDetailModel.setMesssageLineList(messsageLineList);
        employeeImportDetailModel.setEmployeeImportId(employeeImportId);
        employeeImportDetailModel.setEmployeeImportModel(employeeImportModel);
        // assert
        Assertions.assertEquals(id, employeeImportDetailModel.getId());
        Assertions.assertEquals(body, employeeImportDetailModel.getBody());
        Assertions.assertEquals(status, employeeImportDetailModel.getStatus());
        Assertions.assertEquals(lineNum, employeeImportDetailModel.getLineNum());
        Assertions.assertEquals(messsageLineList, employeeImportDetailModel.getMesssageLineList());
        Assertions.assertEquals(employeeImportId, employeeImportDetailModel.getEmployeeImportId());
        Assertions.assertEquals(employeeImportModel, employeeImportDetailModel.getEmployeeImportModel());
    }

    @Test
    void testEmployeeOnBenchModel_Builder() {
        // prepare
        employeeImportDetailModel = EmployeeImportDetailModel.builder()
                .id(id)
                .body(body)
                .status(status)
                .lineNum(lineNum)
                .messsageLineList(messsageLineList)
                .employeeImportId(employeeImportId)
                .employeeImportModel(employeeImportModel)
                .build();
        // assert
        Assertions.assertEquals(id, employeeImportDetailModel.getId());
        Assertions.assertEquals(body, employeeImportDetailModel.getBody());
        Assertions.assertEquals(status, employeeImportDetailModel.getStatus());
        Assertions.assertEquals(lineNum, employeeImportDetailModel.getLineNum());
        Assertions.assertEquals(messsageLineList, employeeImportDetailModel.getMesssageLineList());
        Assertions.assertEquals(employeeImportId, employeeImportDetailModel.getEmployeeImportId());
        Assertions.assertEquals(employeeImportModel, employeeImportDetailModel.getEmployeeImportModel());
    }

    @Test
    void testEmployeeOnBenchModel_AllArgsConstructor() {
        // prepare
        employeeImportDetailModel = new EmployeeImportDetailModel(id, body, status, lineNum, messsageLineList,
                employeeImportId, employeeImportModel);
        // assert
        Assertions.assertEquals(id, employeeImportDetailModel.getId());
        Assertions.assertEquals(body, employeeImportDetailModel.getBody());
        Assertions.assertEquals(status, employeeImportDetailModel.getStatus());
        Assertions.assertEquals(lineNum, employeeImportDetailModel.getLineNum());
        Assertions.assertEquals(messsageLineList, employeeImportDetailModel.getMesssageLineList());
        Assertions.assertEquals(employeeImportId, employeeImportDetailModel.getEmployeeImportId());
        Assertions.assertEquals(employeeImportModel, employeeImportDetailModel.getEmployeeImportModel());
    }
}
