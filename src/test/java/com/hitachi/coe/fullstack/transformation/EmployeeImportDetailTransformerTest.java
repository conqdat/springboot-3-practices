package com.hitachi.coe.fullstack.transformation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.hitachi.coe.fullstack.entity.EmployeeImport;
import com.hitachi.coe.fullstack.entity.EmployeeImportDetail;
import com.hitachi.coe.fullstack.model.EmployeeImportDetailModel;
import com.hitachi.coe.fullstack.model.ImportOperationStatus;

public class EmployeeImportDetailTransformerTest {

    EmployeeImportDetailTransformer transformer = new EmployeeImportDetailTransformer();

    @Test
    void testApply() {
        // prepare
        EmployeeImportDetailModel model = new EmployeeImportDetailModel();
        model.setId(1L);
        model.setBody("body");
        model.setStatus(ImportOperationStatus.SUCCESS);
        model.setLineNum(1);
        model.setMesssageLineList("messsageLineList");
        model.setEmployeeImportId(1);
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
        EmployeeImportDetailModel result = transformer.apply(entity);
        // assert
        assertNotNull(result);
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getBody(), result.getBody());
        assertEquals(model.getStatus(), result.getStatus());
        assertEquals(model.getLineNum(), result.getLineNum());
        assertEquals(model.getMesssageLineList(), result.getMesssageLineList());
        assertEquals(model.getEmployeeImportId(), result.getEmployeeImportId());
    }
}
