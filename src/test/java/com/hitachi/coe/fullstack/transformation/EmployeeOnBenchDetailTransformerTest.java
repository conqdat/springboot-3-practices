package com.hitachi.coe.fullstack.transformation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;

import org.junit.jupiter.api.Test;

import com.hitachi.coe.fullstack.entity.Employee;
import com.hitachi.coe.fullstack.entity.EmployeeOnBench;
import com.hitachi.coe.fullstack.entity.EmployeeOnBenchDetail;
import com.hitachi.coe.fullstack.model.EmployeeOnBenchDetailModel;
import com.hitachi.coe.fullstack.model.EmployeeOnBenchModel;

public class EmployeeOnBenchDetailTransformerTest {

    EmployeeOnBenchDetailTransformer transformer = new EmployeeOnBenchDetailTransformer();

    @Test
    void testApply() {
        // prepare
        Date date = new Date();
        EmployeeOnBenchModel onBenchModel = new EmployeeOnBenchModel();
        onBenchModel.setId(1);
        EmployeeOnBench onBench = new EmployeeOnBench();
        onBench.setId(1);
        Employee employee = new Employee();
        employee.setId(1);
        EmployeeOnBenchDetailModel employeeOnBenchDetailModel = new EmployeeOnBenchDetailModel();
        employeeOnBenchDetailModel.setId(1L);
        employeeOnBenchDetailModel.setBenchDays(23);
        employeeOnBenchDetailModel.setDateOfJoin(date);
        employeeOnBenchDetailModel.setStatusChangeDate(date);
        employeeOnBenchDetailModel.setCategoryCode(1);
        employeeOnBenchDetailModel.setEmployeeOnBenchId(1);
        employeeOnBenchDetailModel.setEmployeeId(1);
        EmployeeOnBenchDetail employeeOnBenchDetail = new EmployeeOnBenchDetail();
        employeeOnBenchDetail.setId(1L);
        employeeOnBenchDetail.setBenchDays(23);
        employeeOnBenchDetail.setDateOfJoin(date);
        employeeOnBenchDetail.setStatusChangeDate(date);
        employeeOnBenchDetail.setCategoryCode(1);
        employeeOnBenchDetail.setEmployeeOnBench(onBench);
        employeeOnBenchDetail.setEmployee(employee);
        // invoke
        EmployeeOnBenchDetailModel result = transformer.apply(employeeOnBenchDetail);
        // assert
        assertNotNull(result);
        assertEquals(employeeOnBenchDetailModel.getId(), result.getId());
        assertEquals(employeeOnBenchDetailModel.getBenchDays(), result.getBenchDays());
        assertEquals(employeeOnBenchDetailModel.getDateOfJoin(), result.getDateOfJoin());
        assertEquals(employeeOnBenchDetailModel.getStatusChangeDate(), result.getStatusChangeDate());
        assertEquals(employeeOnBenchDetailModel.getCategoryCode(), result.getCategoryCode());
        assertEquals(employeeOnBenchDetailModel.getEmployeeOnBenchId(), result.getEmployeeOnBenchId());
        assertEquals(employeeOnBenchDetailModel.getEmployeeId(), result.getEmployeeId());
    }
}
