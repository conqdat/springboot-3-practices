package com.hitachi.coe.fullstack.transformation;

import com.hitachi.coe.fullstack.entity.Employee;
import com.hitachi.coe.fullstack.entity.EmployeeStatus;
import com.hitachi.coe.fullstack.model.EmployeeStatusModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class EmployeeStatusModelTransformerTest {

    @Autowired
    private EmployeeStatusModelTransformer employeeStatusModelTransformer;

    @Test
    public void testApply_ConvertsEmployeeStatusModel_To_EmployeeStatus() {
        EmployeeStatusModel employeeStatusModel1 = new EmployeeStatusModel();
        employeeStatusModel1.setId(1);
        employeeStatusModel1.setStatus(1);
        employeeStatusModel1.setStatusDate(new Timestamp(System.currentTimeMillis()));

        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("John Doe");

        employeeStatusModel1.setEmployeeId(employee.getId());

        EmployeeStatus employeeStatus = employeeStatusModelTransformer.apply(employeeStatusModel1);

        assertEquals(1, employeeStatus.getId());
        assertEquals(1, employeeStatus.getStatus());
        assertEquals(employeeStatusModel1.getStatusDate(), employeeStatus.getStatusDate());
        assertEquals(1, employeeStatus.getEmployee().getId());
    }

    @Test
    public void testApplyList_ConvertsEmployeeStatusModelList_To_EmployeeStatusList() {
        List<EmployeeStatusModel> employeeStatusList = new ArrayList<>();

        EmployeeStatusModel employeeStatusModel1 = new EmployeeStatusModel();
        employeeStatusModel1.setId(1);
        employeeStatusModel1.setStatus(1);
        employeeStatusModel1.setStatusDate(new Timestamp(System.currentTimeMillis()));

        Employee employee1 = new Employee();
        employee1.setId(1);
        employee1.setName("John Doe");

        employeeStatusModel1.setEmployeeId(employee1.getId());

        employeeStatusList.add(employeeStatusModel1);

        EmployeeStatusModel employeeStatusModel2 = new EmployeeStatusModel();
        employeeStatusModel2.setId(2);
        employeeStatusModel2.setStatus(2);
        employeeStatusModel2.setStatusDate(new Timestamp(System.currentTimeMillis()));

        Employee employee2 = new Employee();
        employee2.setId(2);
        employee2.setName("Jane Doe");

        employeeStatusModel2.setEmployeeId(employee2.getId());

        employeeStatusList.add(employeeStatusModel2);

        List<EmployeeStatus> employeeStatusModelList = employeeStatusModelTransformer.applyList(employeeStatusList);

        assertEquals(2, employeeStatusModelList.size());

        EmployeeStatus employeeStatus1 = employeeStatusModelList.get(0);
        assertEquals(1, employeeStatus1.getId());
        assertEquals(1, employeeStatus1.getStatus());
        assertEquals(employeeStatus1.getStatusDate(), employeeStatus1.getStatusDate());
        assertEquals(1, employeeStatus1.getEmployee().getId());

        EmployeeStatus employeeStatus2 = employeeStatusModelList.get(1);
        assertEquals(2, employeeStatus2.getId());
        assertEquals(2, employeeStatus2.getStatus());
        assertEquals(employeeStatus2.getStatusDate(), employeeStatus2.getStatusDate());
        assertEquals(2, employeeStatus2.getEmployee().getId());
    }

    @Test
    public void testApplyList_WhenEmptyInput_ThenReturnsEmptyList() {
        List<EmployeeStatusModel> models = Collections.emptyList();
        List<EmployeeStatus> result = employeeStatusModelTransformer.applyList(models);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testApplyList_WhenNullInput_ThenReturnsEmptyList() {
        List<EmployeeStatus> result = employeeStatusModelTransformer.applyList(null);
        assertEquals(Collections.emptyList(), result);
    }
}
