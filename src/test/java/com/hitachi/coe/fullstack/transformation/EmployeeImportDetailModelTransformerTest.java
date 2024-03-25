package com.hitachi.coe.fullstack.transformation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.hitachi.coe.fullstack.entity.EmployeeImport;
import com.hitachi.coe.fullstack.entity.EmployeeImportDetail;
import com.hitachi.coe.fullstack.model.EmployeeImportDetailModel;
import com.hitachi.coe.fullstack.model.EmployeeImportModel;
import com.hitachi.coe.fullstack.model.ImportOperationStatus;

public class EmployeeImportDetailModelTransformerTest {

    EmployeeImportModelTransformer employeeImportModelTransformer = new EmployeeImportModelTransformer();
    EmployeeImportDetailModelTransformer transformer = new EmployeeImportDetailModelTransformer(employeeImportModelTransformer);

    @Test
    void testApply() {
        // prepare
        EmployeeImportModel employeeImportModel = new EmployeeImportModel();
        employeeImportModel.setId(1);
        EmployeeImportDetailModel model = new EmployeeImportDetailModel();
        model.setId(1L);
        model.setBody("body");
        model.setStatus(ImportOperationStatus.SUCCESS);
        model.setLineNum(1);
        model.setMesssageLineList("messsageLineList");
        model.setEmployeeImportId(1);
        model.setEmployeeImportModel(employeeImportModel);
        EmployeeImport employeeImport = new EmployeeImport();
        employeeImport.setId(1);
        EmployeeImportDetail entity = new EmployeeImportDetail();
        entity.setId(1L);
        entity.setBody("body");
        entity.setStatus(ImportOperationStatus.SUCCESS);
        entity.setLineNum(1);
        entity.setMessageLineList("messsageLineList");
        entity.setEmployeeImport(employeeImport);

        // invoke
        EmployeeImportDetail result = transformer.apply(model);
        // assert
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getBody(), result.getBody());
        assertEquals(entity.getStatus(), result.getStatus());
        assertEquals(entity.getLineNum(), result.getLineNum());
        assertEquals(entity.getMessageLineList(), result.getMessageLineList());
        assertEquals(entity.getEmployeeImport().getId(), result.getEmployeeImport().getId());
    }
}
