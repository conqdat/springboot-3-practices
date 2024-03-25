package com.hitachi.coe.fullstack.service;

import com.hitachi.coe.fullstack.entity.EmployeeOnBench;
import com.hitachi.coe.fullstack.model.EmployeeOnBenchModel;
import com.hitachi.coe.fullstack.repository.EmployeeOnBenchRepository;
import com.hitachi.coe.fullstack.transformation.EmployeeOnBenchTransformer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-data-test.properties")
public class EmployeeOnBenchServiceTest {
    @Autowired
    private EmployeeOnBenchService employeeOnBenchService;

    @MockBean
    private EmployeeOnBenchRepository employeeOnBenchRepository;

    @MockBean
    private EmployeeOnBenchTransformer employeeOnBenchTransformer;

    @Test
    public void testGetAllEmployeeOnBench() {
        int no = 0;
        int limit = 10;
        String sortBy = "created";
        Boolean desc = null;
        Sort sort = Sort.by(sortBy);

        final Pageable pageable = PageRequest.of(no, limit, sort);

        List<EmployeeOnBench> employeeOnBench = new ArrayList<>();
        employeeOnBench.add(new EmployeeOnBench());
        Page<EmployeeOnBench> page = new PageImpl<>(employeeOnBench);

        when(employeeOnBenchRepository.findAll(pageable)).thenReturn(page);
        when(employeeOnBenchTransformer.apply(Mockito.isA(EmployeeOnBench.class))).thenReturn(new EmployeeOnBenchModel());

        Page<EmployeeOnBenchModel> result = employeeOnBenchService.getAllEmployeeOnBench(no, limit, sortBy, desc);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }
}
