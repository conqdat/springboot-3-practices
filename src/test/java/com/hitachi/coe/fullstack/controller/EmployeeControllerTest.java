package com.hitachi.coe.fullstack.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.exceptions.CoEException;
import com.hitachi.coe.fullstack.model.BarChartModel;
import com.hitachi.coe.fullstack.model.ChartSkillAndLevelModel;
import com.hitachi.coe.fullstack.model.CountEmployeeModel;
import com.hitachi.coe.fullstack.model.EmployeeModel;
import com.hitachi.coe.fullstack.model.EmployeeStatusModel;
import com.hitachi.coe.fullstack.model.IBarChartDepartmentModel;
import com.hitachi.coe.fullstack.model.IEmployeeDetails;
import com.hitachi.coe.fullstack.model.IEmployeeSearchAdvance;
import com.hitachi.coe.fullstack.model.IEmployeeSearchForTeam;
import com.hitachi.coe.fullstack.model.IPieChartModel;
import com.hitachi.coe.fullstack.model.common.BaseResponse;
import com.hitachi.coe.fullstack.service.EmployeeService;
import com.hitachi.coe.fullstack.service.EmployeeStatusService;
import lombok.SneakyThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-data-test.properties")
class EmployeeControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private EmployeeStatusService employeeStatusService;

    @Autowired
    private EmployeeController employeeController;

    private IEmployeeSearchAdvance iEmployeeSearchAdvance;

    EmployeeModel employeeModel;

    @BeforeEach
    void setUp() {
        iEmployeeSearchAdvance = mock(IEmployeeSearchAdvance.class);
    }

    @Test
    void testSearch() {
        // Mock input data
        String keyword = "nien";
        String practiceName = "";
        String coeCoreTeamName = "";
        String branchName = "";
        String fromDateStr = "2023-05-13 00:00:00";
        String toDateStr = "2023-05-14 00:00:00";
        Integer no = 0;
        Integer limit = 10;
        String sortBy = "name";
        Boolean desc = false;
        // Mock the expected result from employeeService
        List<EmployeeModel> employees = new ArrayList<>();
        employees.add(new EmployeeModel());
        Page<EmployeeModel> expectedResult = new PageImpl<>(employees);

        // Mock the behavior of employeeService.search()
        when(employeeService.searchEmployees(keyword, practiceName, coeCoreTeamName, branchName, 1, fromDateStr, toDateStr,
                no, limit, sortBy, desc)).thenReturn(expectedResult);

        // Call the controller method
        employeeController.searchEmployees(keyword, practiceName, coeCoreTeamName, branchName, 1, fromDateStr, toDateStr,
                no, limit, sortBy, desc);

        // Verify that employeeService.search() is called with the correct parameters
        verify(employeeService).searchEmployees(keyword, practiceName, coeCoreTeamName, branchName, 1, fromDateStr,
                toDateStr, no, limit, sortBy, desc);

    }


    @Test
    void testDeleteEmployeeById_WhenEmployeeDeleted_ThenSuccess() throws Exception {
        Integer id = 1;
        EmployeeStatusModel employeeStatus = new EmployeeStatusModel();
        when(employeeService.deleteEmployeeById(id)).thenReturn(new EmployeeModel());
        when(employeeStatusService.deleteEmployeeById(id)).thenReturn(employeeStatus);
        employeeController.deleteEmployeeById(id);
        verify(employeeService).deleteEmployeeById(id);
        verify(employeeStatusService).deleteEmployeeById(id);
    }

    @Test
    @SneakyThrows
    void updateEmployee() {
        EmployeeModel employeeModel = new EmployeeModel();
        when(employeeService.updateEmployee(any(EmployeeModel.class))).thenReturn(1234);

        BaseResponse<Object> response = employeeController.updateEmployee(employeeModel);

        assertEquals(200, response.getStatus());
        assertNotNull(response.getData());
    }

    @Test
    void testGetEmployeeById() {
        Integer employeeId = 1;
        EmployeeModel employeeModel = new EmployeeModel();
        when(employeeService.getEmployeeById(employeeId)).thenReturn(employeeModel);
        ResponseEntity<EmployeeModel> response = employeeController.getEmployeeById(employeeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeModel, response.getBody());
    }

//	TODO
//	mvn test have Error
//    @Test
//    @SneakyThrows
//    void testAddEmployeeController() {
//        String url = "/api/v1/member/create";
//        EmployeeModel employeeModel = new EmployeeModel();
//        employeeModel.setEmail("pikachu@gmail.com");
//        employeeModel.setName("Tien");
//        employeeModel.setHccId("tien");
//        employeeModel.setLdap("tien");
//        employeeModel.setBranch(new Branch());
//        employeeModel.setCoeCoreTeam(new CoeCoreTeam());
//        employeeModel.setPractice(new Practice());
//        when(employeeService.add(any(EmployeeModel.class))).thenReturn(anyInt());
//        mvc.perform(post(url)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(employeeModel)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isNotEmpty())
//                .andExpect(jsonPath("$.id", is("0")))
//                .andReturn();
//    }

    @Test
    void testGetQuantityDepartmentOfLevel_WithSpecificBranch() {
        Integer branchId = 1;
        Integer groupId = null;
        Integer teamId = null;
        ResponseEntity<List<IBarChartDepartmentModel>> response = employeeController.getQuantityOfLevel(branchId,
                groupId, teamId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetQuantityDepartmentOfLevel_WithSpecificBranchAndGroup() {
        Integer branchId = 1;
        Integer groupId = 1;
        Integer teamId = null;
        ResponseEntity<List<IBarChartDepartmentModel>> response = employeeController.getQuantityOfLevel(branchId,
                groupId, teamId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetQuantityDepartmentOfLevel_WithSpecificBranchGroupAndTeam() {
        Integer branchId = 1;
        Integer groupId = 1;
        Integer teamId = 1;
        ResponseEntity<List<IBarChartDepartmentModel>> response = employeeController.getQuantityOfLevel(branchId,
                groupId, teamId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetQuantityDepartmentOfLevel_WithSpecificGroupAndTeam() {
        Integer branchId = null;
        Integer groupId = 1;
        Integer teamId = 1;
        ResponseEntity<List<IBarChartDepartmentModel>> response = employeeController.getQuantityOfLevel(branchId,
                groupId, teamId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetQuantityDepartmentOfLevel() {
        Integer branchId = null;
        Integer groupId = null;
        Integer teamId = null;
        ResponseEntity<List<IBarChartDepartmentModel>> response = employeeController.getQuantityOfLevel(branchId,
                groupId, teamId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetQuantityOfLevel_whenAllNull() {
        Integer branchId = null;
        Integer groupId = null;
        Integer teamId = null;
        ResponseEntity<BarChartModel> response = employeeController.getQuantityOfLevelForBarChart(branchId, groupId,
                teamId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetQuantityOfLevel_whenBranchNotNull() {
        Integer branchId = 1;
        Integer groupId = null;
        Integer teamId = null;
        ResponseEntity<BarChartModel> response = employeeController.getQuantityOfLevelForBarChart(branchId, groupId,
                teamId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetQuantityOfLevel_whenBranchAndGroupNotNull() {
        Integer branchId = 1;
        Integer groupId = 1;
        Integer teamId = null;
        ResponseEntity<BarChartModel> response = employeeController.getQuantityOfLevelForBarChart(branchId, groupId,
                teamId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetQuantityOfLevel_whenBranchGroupAndTeamNotNull() {
        Integer branchId = 1;
        Integer groupId = 1;
        Integer teamId = 1;
        ResponseEntity<BarChartModel> response = employeeController.getQuantityOfLevelForBarChart(branchId, groupId,
                teamId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetQuantityOfLevel_whenGroupAndTeamNotNull() {
        Integer branchId = null;
        Integer groupId = 1;
        Integer teamId = 1;
        ResponseEntity<BarChartModel> response = employeeController.getQuantityOfLevelForBarChart(branchId, groupId,
                teamId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetQuantityOfEachSkillForBarChart_thenSuccess() {
        Integer branchId = 1;
        Integer groupId = 1;
        Integer teamId = 1;
        List<Integer> skillIds = Arrays.asList(1, 2);
        ResponseEntity<?> response = employeeController.getPercentofSkills(branchId, groupId, teamId, skillIds);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void testGetLevelPieChart() {
        // Arrange
        Integer branchId = 1;
        Integer coeCoreTeamId = 2;
        Integer coeId = 3;

        List<IPieChartModel> expectedResponse = new ArrayList<>();
        when(employeeService.getLevelPieChart(branchId, coeCoreTeamId, coeId)).thenReturn(expectedResponse);

        // Act
        BaseResponse<List<IPieChartModel>> response = employeeController.getLevelPieChart(branchId, coeCoreTeamId, coeId);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNull(response.getMessage());
        assertEquals(expectedResponse, response.getData());

        // Verify the method call
        verify(employeeService).getLevelPieChart(branchId, coeCoreTeamId, coeId);
    }

    @Test
    void testGetEmployeeDetailsByHccId_whenHccIdIsValid_thenReturnIEmployeeDetails() {
        // prepare
        String testHccId = "999999";
        IEmployeeDetails iEmployeeDetails = mock(IEmployeeDetails.class);
        // when - then
        when(employeeService.getEmployeeDetailsByHccId(anyString())).thenReturn(iEmployeeDetails);
        // invoke
        employeeController.getEmployeeDetailsByHccId(testHccId);
        // verify
        verify(employeeService).getEmployeeDetailsByHccId(testHccId);
    }

    @Test
    void testGetEmployeeDetailsByHccId_whenHccIdIsInvalid_thenThrowsCoEException() {
        // prepare
        String testHccId = "999999";
        IEmployeeDetails iEmployeeDetails = mock(IEmployeeDetails.class);
        // when - then
        when(employeeService.getEmployeeDetailsByHccId(anyString())).thenThrow(new CoEException("1", "Error"));
        // invoke & assert
        assertThrows(CoEException.class, () -> employeeController.getEmployeeDetailsByHccId(testHccId));
        // verify
        verify(employeeService).getEmployeeDetailsByHccId(testHccId);
    }

    @Test
    void testSearchEmployeesAdvance_whenValidData_thenReturnSuccess() {
        // prepare
        Page<IEmployeeSearchAdvance> mockPage = new PageImpl<>(Arrays.asList(iEmployeeSearchAdvance, iEmployeeSearchAdvance));
        // when-then
        when(employeeService.searchEmployeesAdvance(anyString(), anyInt(), anyInt(), anyInt(), anyString(), anyInt(),
                any(LocalDate.class), any(LocalDate.class), anyInt(), anyInt(),
                anyString(), anyBoolean())).thenReturn(mockPage);
        // invoke
        Page<IEmployeeSearchAdvance> result = employeeService.searchEmployeesAdvance("searchTerm", 10, 10, 10,
                "sortField", 1, LocalDate.now(), LocalDate.now(), 5, 100,
                "filterValue", true);
        // assert
        assertEquals(mockPage, result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(iEmployeeSearchAdvance, result.getContent().get(0));
        assertEquals(iEmployeeSearchAdvance, result.getContent().get(1));
        // verify
        verify(employeeService).searchEmployeesAdvance("searchTerm", 10, 10, 10,
                "sortField", 1, LocalDate.now(), LocalDate.now(), 5, 100,
                "filterValue", true);
    }

    @Test
    void testSearchEmployeesAdvance_whenEmptyResponse_thenReturnEmptyPage() {
        // prepare
        Page<IEmployeeSearchAdvance> mockPage = new PageImpl<>(Collections.emptyList());
        // when-then
        when(employeeService.searchEmployeesAdvance(anyString(), anyInt(), anyInt(), anyInt(), anyString(), anyInt(),
                any(LocalDate.class), any(LocalDate.class), anyInt(), anyInt(),
                anyString(), anyBoolean())).thenReturn(mockPage);
        // invoke
        Page<IEmployeeSearchAdvance> result = employeeService.searchEmployeesAdvance("searchTerm", 10, 10, 10,
                "sortField", 1, LocalDate.now(), LocalDate.now(), 5, 100,
                "filterValue", true);
        // assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getContent().size());
        // verify
        verify(employeeService).searchEmployeesAdvance("searchTerm", 10, 10, 10,
                "sortField", 1, LocalDate.now(), LocalDate.now(), 5, 100,
                "filterValue", true);
    }

    @Test
    void testSearchEmployeesAdvance_whenEmptySearchTerm_thenReturnAllResults() {
        // prepare
        List<IEmployeeSearchAdvance> employees = Arrays.asList(iEmployeeSearchAdvance, iEmployeeSearchAdvance);
        Page<IEmployeeSearchAdvance> mockPage = new PageImpl<>(employees);
        // when-then
        when(employeeService.searchEmployeesAdvance(eq(""), anyInt(), anyInt(), anyInt(), anyString(), anyInt(),
                any(LocalDate.class), any(LocalDate.class), anyInt(), anyInt(),
                anyString(), anyBoolean())).thenReturn(mockPage);
        // invoke
        Page<IEmployeeSearchAdvance> result = employeeService.searchEmployeesAdvance("", 10, 10, 10,
                "sortField", 1, LocalDate.now(), LocalDate.now(), 5, 100,
                "filterValue", true);
        // assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.getTotalElements());
        assertEquals(employees, result.getContent());
        // verify
        verify(employeeService).searchEmployeesAdvance("", 10, 10, 10,
                "sortField", 1, LocalDate.now(), LocalDate.now(), 5, 100,
                "filterValue", true);
    }

    @Test
    void testSearchEmployeesAdvance_withLargePageSize_thenReturnLimitedResults() {
        // prepare
        List<IEmployeeSearchAdvance> employees = Arrays.asList(iEmployeeSearchAdvance, iEmployeeSearchAdvance, iEmployeeSearchAdvance);
        Page<IEmployeeSearchAdvance> mockPage = new PageImpl<>(employees);
        // when-then
        when(employeeService.searchEmployeesAdvance(anyString(), eq(1000), eq(250), eq(300), anyString(), anyInt(),
                any(LocalDate.class), any(LocalDate.class), anyInt(), anyInt(),
                anyString(), anyBoolean())).thenReturn(mockPage);
                
        // invoke
        Page<IEmployeeSearchAdvance> result = employeeService.searchEmployeesAdvance("searchTerm", 1000, 250, 300,
                "sortField", 1, LocalDate.now(), LocalDate.now(), 5, 100,
                "filterValue", true);
        // assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(3, result.getTotalElements());
        assertEquals(employees, result.getContent());
        // verify
        verify(employeeService).searchEmployeesAdvance("searchTerm", 1000, 250, 300,
                "sortField", 1, LocalDate.now(), LocalDate.now(), 5, 100,
                "filterValue", true);
    }

    @Test
    void testgetQuantityOfEmployeesForEachSkill_WhenFullParams_ThenSuccess() {
		Integer branchId = 1;
		Integer coeId = 1;
		List<Integer> arrSkillIds = Arrays.asList(1, 2, 3, 4, 5, 6);
		ChartSkillAndLevelModel expected = new ChartSkillAndLevelModel();
		Mockito.when(employeeService.getQuantityOfEmployeesForEachSkill(branchId, coeId, arrSkillIds))
				.thenReturn(expected);

		ResponseEntity<ChartSkillAndLevelModel> response = employeeController.getQuantityOfEmployeesForEachSkill(branchId,
				coeId, arrSkillIds);

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(expected, response.getBody());
	}

    @Test
    void testgetQuantityOfEmployeesForEachSkill_WhenNullBranchIdAndCoeId_ThenSuccess() {
		Integer branchId = null;
		Integer coeId = null;
		List<Integer> arrSkillIds = Arrays.asList(1, 2, 3, 4, 5, 6);
		ChartSkillAndLevelModel expected = new ChartSkillAndLevelModel();
		Mockito.when(employeeService.getQuantityOfEmployeesForEachSkill(branchId, coeId, arrSkillIds))
				.thenReturn(expected);

		ResponseEntity<ChartSkillAndLevelModel> response = employeeController.getQuantityOfEmployeesForEachSkill(branchId,
				coeId, arrSkillIds);

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(expected, response.getBody());
	}

    @Test
    void testgetQuantityOfEmployeesForEachSkill_WhenOnlyNullBranchId_ThenSuccess() {
		Integer branchId = null;
		Integer coeId = 8;
		List<Integer> arrSkillIds = Arrays.asList(1, 2, 3, 4, 5, 6);
		ChartSkillAndLevelModel expected = new ChartSkillAndLevelModel();
		Mockito.when(employeeService.getQuantityOfEmployeesForEachSkill(branchId, coeId, arrSkillIds))
				.thenReturn(expected);

		ResponseEntity<ChartSkillAndLevelModel> response = employeeController.getQuantityOfEmployeesForEachSkill(branchId,
				coeId, arrSkillIds);

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(expected, response.getBody());
	}

    @Test
    void testgetQuantityOfEmployeesForEachSkill_WhenOnlyNullCoeId_ThenSuccess() {
		Integer branchId = 1;
		Integer coeId = null;
		List<Integer> arrSkillIds = Arrays.asList(1, 2, 3, 4, 5, 6);
		ChartSkillAndLevelModel expected = new ChartSkillAndLevelModel();
		Mockito.when(employeeService.getQuantityOfEmployeesForEachSkill(branchId, coeId, arrSkillIds))
				.thenReturn(expected);

		ResponseEntity<ChartSkillAndLevelModel> response = employeeController.getQuantityOfEmployeesForEachSkill(branchId,
				coeId, arrSkillIds);

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(expected, response.getBody());
	}

    @Test
	void testgetQuantityOfEmployeesForEachSkill_WhenAllNull_ThenThrowCoEException(){

		Integer branchId = null;
		Integer coeId = null;
		List<Integer> arrTopSkillIds = null;
		Mockito.when(employeeService.getQuantityOfEmployeesForEachSkill(branchId, coeId, arrTopSkillIds))
				.thenThrow(new CoEException(ErrorConstant.CODE_LIST_ID_OF_SKILL_NULL,
                ErrorConstant.MESSAGE_LIST_ID_OF_SKILL_NULL)); 

		Throwable throwable = Assertions.assertThrows(CoEException.class, () -> {
			employeeService.getQuantityOfEmployeesForEachSkill(branchId, coeId, arrTopSkillIds);
		});

		Assertions.assertEquals(CoEException.class, throwable.getClass());
	}

    @Test
    void testGetEmployeesByTeam_withGivenId_existingTeam() {
        List<EmployeeModel> employeeList = new ArrayList<>();
        EmployeeModel test1 = new EmployeeModel();
        EmployeeModel test2 = new EmployeeModel();
        EmployeeModel test3 = new EmployeeModel();
        employeeList.add(test1);
        employeeList.add(test2);
        employeeList.add(test3);

        when(employeeService.getEmployeesByTeamId(3)).thenReturn(employeeList);

        ResponseEntity<List<EmployeeModel>> result = employeeController.getEmployeesByTeam(3);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(3, result.getBody().size());
    }

    @Test
    void testGetEmployeesByTeam_withGivenId_nonExistingTeam() {
        when(employeeService.getEmployeesByTeamId(1)).thenReturn(new ArrayList<>());

        ResponseEntity<List<EmployeeModel>> result = employeeController.getEmployeesByTeam(3);
        
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(0, result.getBody().size());
    }

    @Test
    void testGetCountOfEmployeesForCurrentMonth_ThenSuccess(){
        CountEmployeeModel expected = new CountEmployeeModel();
		Mockito.when(employeeService.getCountOfEmployeesForCurrentMonth())
				.thenReturn(expected);

		ResponseEntity<CountEmployeeModel> response = employeeController.getCountOfEmployeesForCurrentMonth();

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(expected, response.getBody());
    }

    @Test
    @SneakyThrows
    void testSearchEmployeesByCoeAndNameString_whenValidParams_thenReturnSuccess() {
       List<IEmployeeSearchForTeam> mockEmployees = new ArrayList<IEmployeeSearchForTeam>();
       when(employeeService.searchEmployeesByCoeAndNameString(anyInt(), anyString())).thenReturn(mockEmployees);
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/employee/search-by-coe-and-name").contentType(MediaType.APPLICATION_JSON)
                .param("coeId", "1")
                .param("nameString", "nguyen"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("200"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
				.andDo(MockMvcResultHandlers.print());
    }

    @Test
    @SneakyThrows
    void testSearchEmployeesByCoeAndNameString_whenAllParamsNull_thenReturnBadRequest() {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/employee/search-by-coe-and-name").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Input at least one parameter"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist())
				.andDo(MockMvcResultHandlers.print());
    }
}
