package com.hitachi.coe.fullstack.transformation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.hitachi.coe.fullstack.entity.EmployeeImport;
import com.hitachi.coe.fullstack.model.EmployeeImportModel;
import com.hitachi.coe.fullstack.model.ImportOperationStatus;
import com.hitachi.coe.fullstack.model.ImportOperationType;

public class EmployeeImportModelTransformerTest {

    EmployeeImportModelTransformer transformer = new EmployeeImportModelTransformer();

    @Test
    void testApply() {
        // prepare
        EmployeeImportModel model = new EmployeeImportModel();
        model.setId(1);
        model.setName("VDC");
        model.setType(ImportOperationType.EMPLOYEE_ON_BENCH);
        model.setStatus(ImportOperationStatus.SUCCESS);
        model.setMessage("json");
        EmployeeImport entity = new EmployeeImport();
        entity.setId(1);
        entity.setName("VDC");
        entity.setType(ImportOperationType.EMPLOYEE_ON_BENCH);
        entity.setStatus(ImportOperationStatus.SUCCESS);
        entity.setMessage("json");
        // invoke
        EmployeeImport result = transformer.apply(model);
        // assert
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getType(), result.getType());
        assertEquals(entity.getStatus(), result.getStatus());
        assertEquals(entity.getMessage(), result.getMessage());
    }
}
