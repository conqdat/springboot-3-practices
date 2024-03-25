package com.hitachi.coe.fullstack.service;

import com.hitachi.coe.fullstack.entity.EmployeeRole;
import com.hitachi.coe.fullstack.model.EmployeeRoleModel;
import com.hitachi.coe.fullstack.repository.EmployeeRoleRepository;
import com.hitachi.coe.fullstack.service.impl.EmployeeRoleServiceImpl;
import com.hitachi.coe.fullstack.transformation.EmployeeRoleTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeRoleServiceTest {

    @InjectMocks
    EmployeeRoleServiceImpl employeeRoleService;
    @Mock
    EmployeeRoleRepository employeeRoleRepository;
    @Mock
    EmployeeRoleTransformer employeeRoleTransformer;
    EmployeeRole employeeRole;
    EmployeeRoleModel employeeRoleModel;
    List<EmployeeRole> employeeRoleList;
    List<EmployeeRoleModel> employeeRoleModelList;

    @BeforeEach
    void setUp() {
        employeeRole = new EmployeeRole();
        employeeRole.setId(1);
        employeeRole.setName("name");
        employeeRole.setCode("code");
        employeeRole.setDescription("desc");

        employeeRoleModel = new EmployeeRoleModel();
        employeeRoleModel.setId(1);
        employeeRoleModel.setName("name");
        employeeRoleModel.setCode("code");
        employeeRoleModel.setDescription("desc");

        employeeRoleList = new ArrayList<>();
        employeeRoleList.add(employeeRole);

        employeeRoleModelList = new ArrayList<>();
        employeeRoleModelList.add(employeeRoleModel);
    }

    @Test
    void testGetAll_thenReturnListOfEmployeeRoleModel() {
        // when-then
        when(employeeRoleRepository.findAll()).thenReturn(employeeRoleList);
        when(employeeRoleTransformer.apply(any(EmployeeRole.class))).thenReturn(employeeRoleModel);
        // invoke
        List<EmployeeRoleModel> resultList = employeeRoleService.getAll();
        // assert-verify
        verify(employeeRoleRepository, times(1)).findAll();
        verify(employeeRoleTransformer, times(1)).apply(any(EmployeeRole.class));
        assertNotNull(resultList);
        assertNotEquals(resultList.size(), 0);
        assertEquals(resultList.get(0).getId(), employeeRoleModel.getId());
        assertEquals(resultList.get(0).getCode(), employeeRoleModel.getCode());
        assertEquals(resultList.get(0).getName(), employeeRoleModel.getName());
        assertEquals(resultList.get(0).getDescription(), employeeRoleModel.getDescription());
    }
}
