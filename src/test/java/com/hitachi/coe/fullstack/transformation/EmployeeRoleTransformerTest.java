package com.hitachi.coe.fullstack.transformation;

import com.hitachi.coe.fullstack.entity.EmployeeRole;
import com.hitachi.coe.fullstack.model.EmployeeRoleModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class EmployeeRoleTransformerTest {

    @Autowired
    private EmployeeRoleTransformer employeeRoleTransformer;

    private EmployeeRole employeeRole;

    @BeforeEach
    void setUpEach() {
        employeeRole = new EmployeeRole();
        employeeRole.setId(1);
        employeeRole.setCode("Code");
        employeeRole.setDescription("Description");
        employeeRole.setName("Name");
    }

    @Test
    void testEmployeeRoleTransformer_Apply_thenSuccess() {
        // prepare
        EmployeeRoleModel model = employeeRoleTransformer.apply(employeeRole);
        // assert
        assertEquals(1, model.getId());
    }

    @Test
    void testEmployeeRoleTransformer_ApplyList_thenSuccess() {
        // prepare
        List<EmployeeRole> employeeRoleList = Arrays.asList(employeeRole);
        List<EmployeeRoleModel> employeeRoleModelList = employeeRoleTransformer.applyList(employeeRoleList);
        // assert
        assertEquals(1, employeeRoleModelList.size());
    }

    @Test
    void testEmployeeRoleTransformer_ApplyList_EmptyList() {
        // prepare
        List<EmployeeRole> employeeRoleList = Collections.emptyList();
        List<EmployeeRoleModel> employeeRoleModelList = employeeRoleTransformer.applyList(employeeRoleList);
        // assert
        assertTrue(employeeRoleModelList.isEmpty());
    }
}
