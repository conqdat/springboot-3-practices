package com.hitachi.coe.fullstack.controller;

import com.hitachi.coe.fullstack.model.EmployeeImportModel;
import com.hitachi.coe.fullstack.model.ImportOperationStatus;
import com.hitachi.coe.fullstack.model.ImportOperationType;
import com.hitachi.coe.fullstack.model.common.BaseResponse;
import com.hitachi.coe.fullstack.service.EmployeeImportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class EmployeeImportControllerTest {
        @MockBean
        EmployeeImportService employeeImportService;
        @Autowired
        EmployeeImportController employeeImportController;
        EmployeeImportModel employeeImportModel;
        int no;
        int limit;
        String sortBy;
        Boolean desc;
        ImportOperationType typeEnum;
        ImportOperationStatus statusEnum;
        Integer type;
        Integer status;
        String keyword;
        Sort sort;
        Pageable pageable;
        List<EmployeeImportModel> employees;
        Page<EmployeeImportModel> mockPage;

        @BeforeEach
        void setUp() {
                // prepare
                employeeImportModel = mock(EmployeeImportModel.class);
                no = 0;
                limit = 10;
                sortBy = "created";
                desc = true;
                typeEnum = ImportOperationType.EMPLOYEE_ON_BENCH;
                statusEnum = ImportOperationStatus.SUCCESS;
                type = 1;
                status = 1;
                keyword = "keyword";
                sort = Sort.by(Sort.Direction.DESC, sortBy);
                pageable = PageRequest.of(no, limit, sort);
                employees = Arrays.asList(employeeImportModel, employeeImportModel);
                mockPage = new PageImpl<>(employees, pageable, employees.size());
        }

        @Test
        void testSearchEmployeeImport_whenValidParams_thenReturnValidDataBaseResponse() {
                // when-then
                when(employeeImportService.search(anyString(),
                                anyInt(),
                                any(),
                                anyInt(),
                                any(),
                                anyInt(),
                                anyInt(),
                                anyString(),
                                anyBoolean()))
                                .thenReturn(mockPage);
                // invoke
                BaseResponse<Page<?>> result = employeeImportController.search(keyword,
                                type,
                                status,
                                typeEnum,
                                statusEnum,
                                no,
                                limit,
                                sortBy,
                                desc);
                // assert
                assertNotNull(result);
                assertFalse(result.getData().isEmpty());
                assertEquals(2, result.getData().getTotalElements());
                assertEquals(employees, result.getData().getContent());
                assertEquals(employees.size(), result.getData().getContent().size());
                assertEquals(employees.size(), result.getData().getNumberOfElements());
                assertEquals(pageable.getPageSize(), result.getData().getSize());
                assertEquals(pageable.getPageNumber(), result.getData().getNumber());
                assertEquals(1, result.getData().getTotalPages());
                // verify
                verify(employeeImportService).search(keyword,
                                type,
                                typeEnum,
                                status,
                                statusEnum,
                                no,
                                limit,
                                sortBy,
                                desc);
        }

        @Test
        void testSearchEmployeeImport_whenNotFoundKeyWord_thenReturnEmptyData() {
                // prepare
                int size = 0;
                mockPage = new PageImpl<>(Collections.emptyList(), pageable, size);
                // when-then
                when(employeeImportService.search(anyString(),
                                anyInt(),
                                any(),
                                anyInt(),
                                any(),
                                anyInt(),
                                anyInt(),
                                anyString(),
                                anyBoolean()))
                                .thenReturn(mockPage);
                // invoke
                BaseResponse<Page<?>> result = employeeImportController.search(keyword,
                                type,
                                status,
                                typeEnum,
                                statusEnum,
                                no,
                                limit,
                                sortBy,
                                desc);
                // assert
                assertNotNull(result);
                assertTrue(result.getData().isEmpty());
                assertEquals(size, result.getData().getTotalElements());
                assertEquals(size, result.getData().getContent().size());
                assertEquals(size, result.getData().getNumberOfElements());
                assertEquals(size, result.getData().getTotalPages());
                assertEquals(size, result.getData().getNumber());
                assertEquals(pageable.getPageSize(), result.getData().getSize());
                // verify
                verify(employeeImportService).search(keyword,
                                type,
                                typeEnum,
                                status,
                                statusEnum,
                                no,
                                limit,
                                sortBy,
                                desc);
        }
}
