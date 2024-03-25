package com.hitachi.coe.fullstack.transformation;

import com.hitachi.coe.fullstack.entity.EmployeeOnBench;
import com.hitachi.coe.fullstack.model.EmployeeOnBenchModel;
import com.hitachi.coe.fullstack.util.DateFormatUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EmployeeOnBenchTransformerTest {

    @Autowired
    EmployeeOnBenchTransformer transformer;

    @Test
    void testApply() {
        Timestamp startDate = DateFormatUtils.convertTimestampFromString("2023-10-31");
        Timestamp endDate = DateFormatUtils.convertTimestampFromString("2023-10-31");

        EmployeeOnBench employeeOnBench = new EmployeeOnBench();
        employeeOnBench.setId(1);
        employeeOnBench.setName("VDC");
        employeeOnBench.setStartDate(startDate);
        employeeOnBench.setEndDate(endDate);

        EmployeeOnBenchModel model = transformer.apply(employeeOnBench);
        assertEquals(1, model.getId());
        assertEquals("VDC", model.getName());
        assertEquals(startDate, model.getStartDate());
        assertEquals(endDate, model.getEndDate());
    }
}
