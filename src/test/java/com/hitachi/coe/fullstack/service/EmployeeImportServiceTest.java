package com.hitachi.coe.fullstack.service;

import com.hitachi.coe.fullstack.model.EmployeeImportModel;
import com.hitachi.coe.fullstack.model.IEmployeeImportSearch;
import com.hitachi.coe.fullstack.model.ImportOperationStatus;
import com.hitachi.coe.fullstack.model.ImportOperationType;
import com.hitachi.coe.fullstack.repository.EmployeeImportRepository;
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
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-data-test.properties")
public class EmployeeImportServiceTest {

        @Autowired
        EmployeeImportService employeeImportService;
        @MockBean
        EmployeeImportRepository employeeImportRepository;
        EmployeeImportModel employeeImportModel;
        IEmployeeImportSearch iEmployeeImportSearch;
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
        List<EmployeeImportModel> employeeImportModelList;
        List<IEmployeeImportSearch> iEmployeeImportSearchList;
        Page<EmployeeImportModel> mockEmployeeImportModelPage;
        Page<IEmployeeImportSearch> mockIEmployeeImportSearchPage;

        @BeforeEach
        void setUp() {
                // prepare
                employeeImportModel = mock(EmployeeImportModel.class);
                iEmployeeImportSearch = mock(IEmployeeImportSearch.class);
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
                employeeImportModelList = Arrays.asList(employeeImportModel, employeeImportModel);
                iEmployeeImportSearchList = Arrays.asList(iEmployeeImportSearch, iEmployeeImportSearch);
                mockEmployeeImportModelPage = new PageImpl<>(employeeImportModelList, pageable,
                                employeeImportModelList.size());
                mockIEmployeeImportSearchPage = new PageImpl<>(iEmployeeImportSearchList, pageable,
                                iEmployeeImportSearchList.size());
        }

        @Test
        void testSearch_whenValidParams_thenReturnValidPage() {
                // when-then
                when(employeeImportRepository.search(anyString(), anyInt(), anyInt(), any(Pageable.class)))
                                .thenReturn(mockIEmployeeImportSearchPage);
                // invoke
                Page<EmployeeImportModel> result = employeeImportService.search(keyword, type, typeEnum, status,
                                statusEnum, no,
                                limit, sortBy, desc);
                // assert
                assertNotNull(result);
                assertFalse(result.isEmpty());
                assertEquals(2, result.getTotalElements());
                assertEquals(employeeImportModelList.size(), result.getContent().size());
                verify(employeeImportRepository).search(anyString(), anyInt(), anyInt(), any(Pageable.class));
        }

        @Test
        void testSearch_whenNotFoundKeyWord_thenReturnEmptyData() {
                // when-then
                when(employeeImportRepository.search(anyString(), anyInt(), anyInt(), any(Pageable.class)))
                                .thenReturn(Page.empty());
                // invoke
                Page<EmployeeImportModel> result = employeeImportService.search(keyword, type, typeEnum, status,
                                statusEnum, no,
                                limit, sortBy, desc);
                // assert
                assertNotNull(result);
                assertTrue(result.isEmpty());
                assertEquals(0, result.getTotalElements());
                assertEquals(0, result.getContent().size());
                verify(employeeImportRepository).search(anyString(), anyInt(), anyInt(), any(Pageable.class));
        }
}
