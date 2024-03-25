package com.hitachi.coe.fullstack.transformation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;

import com.hitachi.coe.fullstack.entity.EmployeeOnBench;
import com.hitachi.coe.fullstack.model.EmployeeOnBenchModel;

public class EmployeeOnBenchModelTransformerTest {

    EmployeeOnBenchModelTransformer transformer = new EmployeeOnBenchModelTransformer();

    @Test
    void testApply() {
        // prepare
        EmployeeOnBenchModel model = new EmployeeOnBenchModel();
        model.setId(1);
        model.setName("VDC");
        model.setStartDate(new Date());
        model.setEndDate(new Date());
        EmployeeOnBench entity = new EmployeeOnBench();
        entity.setId(1);
        entity.setName("VDC");
        entity.setStartDate(new Date());
        entity.setEndDate(new Date());
        // invoke
        EmployeeOnBench result = transformer.apply(model);
        // assert
        assertEquals(entity, result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getStartDate(), result.getStartDate());
        assertEquals(entity.getEndDate(), result.getEndDate());
    }
}
