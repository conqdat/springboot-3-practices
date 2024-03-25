package com.hitachi.coe.fullstack.controller;


import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.exceptions.CoEException;
import com.hitachi.coe.fullstack.model.EmployeeUTModel;
import com.hitachi.coe.fullstack.model.EmployeeUtilizationFree;
import com.hitachi.coe.fullstack.model.EmployeeUtilizationModelResponse;
import com.hitachi.coe.fullstack.model.EmployeeUtilizationNoImport;
import com.hitachi.coe.fullstack.model.IEmployeeUTModel;
import com.hitachi.coe.fullstack.model.IEmployeeUtilizationDetail;
import com.hitachi.coe.fullstack.model.IEmployeeUtilizationDetailResponse;
import com.hitachi.coe.fullstack.model.IEmployeeUtilizationFree;
import com.hitachi.coe.fullstack.model.IEmployeeUtilizationModel;
import com.hitachi.coe.fullstack.model.IPieChartModel;
import com.hitachi.coe.fullstack.model.PieChart;
import com.hitachi.coe.fullstack.model.AverageYearUTModel;
import com.hitachi.coe.fullstack.model.common.BaseResponse;
import com.hitachi.coe.fullstack.service.EmployeeUtilizationService;
import com.hitachi.coe.fullstack.util.DateFormatUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.sql.Timestamp;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-data-test.properties")
class EmployeeUtilizationControllerTest {

	@Autowired
	private EmployeeUtilizationController employeeUtilizationController;

	@MockBean
	private EmployeeUtilizationService employeeUtilizationServiceMock;

	@Test
	void testGetUTPieChart_Success() {
		List<IPieChartModel> expectedPieChartModel = new ArrayList<IPieChartModel>();

		when(employeeUtilizationServiceMock.getUtilizationPieChart(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(),
				Mockito.anyString(), Mockito.anyString())).thenReturn(expectedPieChartModel);

		BaseResponse<List<IPieChartModel>> result = employeeUtilizationController.getUtilizationPieChart(1, 2, 3,
				"2022-06-01", "2022-06-30");

		assertNotNull(result);
		assertEquals(HttpStatus.OK.value(), result.getStatus());
		assertNull(result.getMessage());
		assertEquals(expectedPieChartModel, result.getData());
	}

	@Test
	void testGetUTPieChart_Exception() {
		when(employeeUtilizationServiceMock.getUtilizationPieChart(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(),
				Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new CoEException(ErrorConstant.CODE_DATA_IS_EMPTY, ErrorConstant.MESSAGE_DATA_IS_EMPTY));

		CoEException exception = assertThrows(CoEException.class, () -> {
			employeeUtilizationController.getUtilizationPieChart(1, 2, 3, "2022-06-01", "2022-06-30");
		});
		assertEquals(CoEException.class, exception.getClass());
	}

	@Test
	void testGetEmployeesNoUtilization_whenValidData_thenSuccess() {

		IEmployeeUtilizationFree iEmployeeUtilizationFreeOne =  mock(IEmployeeUtilizationFree.class);
		IEmployeeUtilizationFree iEmployeeUtilizationFreeTwo =  mock(IEmployeeUtilizationFree.class);

		when(iEmployeeUtilizationFreeOne.getHccId()).thenReturn("Hcc1");
		when(iEmployeeUtilizationFreeOne.getEmployeeId()).thenReturn(1);
		when(iEmployeeUtilizationFreeOne.getEmployeeName()).thenReturn("Nguyen Van A");
		when(iEmployeeUtilizationFreeOne.getEmail()).thenReturn("a.1@example.com");
		when(iEmployeeUtilizationFreeOne.getBranchName()).thenReturn("Branch a");
		when(iEmployeeUtilizationFreeOne.getBusinessName()).thenReturn("Business unit a");
		when(iEmployeeUtilizationFreeOne.getTeamName()).thenReturn("Team a");
		when(iEmployeeUtilizationFreeOne.getDaysFree()).thenReturn(25);

		when(iEmployeeUtilizationFreeTwo.getHccId()).thenReturn("Hcc2");
		when(iEmployeeUtilizationFreeTwo.getEmployeeId()).thenReturn(2);
		when(iEmployeeUtilizationFreeTwo.getEmployeeName()).thenReturn("Nguyen Van B");
		when(iEmployeeUtilizationFreeTwo.getEmail()).thenReturn("aa.2@example.com");
		when(iEmployeeUtilizationFreeTwo.getBranchName()).thenReturn("Branch b");
		when(iEmployeeUtilizationFreeTwo.getBusinessName()).thenReturn("Business unit b");
		when(iEmployeeUtilizationFreeTwo.getTeamName()).thenReturn("Team b");
		when(iEmployeeUtilizationFreeTwo.getDaysFree()).thenReturn(27);

		// List<IEmployeeUtilizationFree> iEmployeeUtilizationFreeList = Arrays.asList(iEmployeeUtilizationFreeOne,iEmployeeUtilizationFreeTwo);
		EmployeeUtilizationFree employeeUtilizationFree = new EmployeeUtilizationFree();
		List<IEmployeeUtilizationFree> iEmployeeUtilizationFreeList = Arrays.asList(iEmployeeUtilizationFreeOne,iEmployeeUtilizationFreeOne);
		employeeUtilizationFree.setIEmployeeUtilizationFreePage(new PageImpl(iEmployeeUtilizationFreeList));
		employeeUtilizationFree.setBillable(90.7);

		when(employeeUtilizationServiceMock.getListEmployeeUtilizationWithNoUT(Mockito.anyDouble(), Mockito.anyString(), Mockito.anyString()
		, Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyBoolean())).thenReturn(employeeUtilizationFree);

		BaseResponse<EmployeeUtilizationFree> result = employeeUtilizationController.getEmployeesNoUtilization("2023-09-01", 
				"2023-08-30", 0, 10, "employeeName", false);

		assertNotNull(result);
		assertEquals(HttpStatus.OK.value(), result.getStatus());
		assertNull(result.getMessage());
		assertEquals(employeeUtilizationFree, result.getData());
	}

	@Test
	void testGetEmployeeUtilizationDetailByEmployeeUtilizationId_whenValidData_thenSuccess() {

        Timestamp startDate = DateFormatUtils.convertTimestampFromString("2023-08-01");
        Timestamp endDate = DateFormatUtils.convertTimestampFromString("2023-08-30");

        IEmployeeUtilizationDetail iEmployeeUtilizationDetail = mock(IEmployeeUtilizationDetail.class);
        when(iEmployeeUtilizationDetail.getHccId()).thenReturn("001");
        when(iEmployeeUtilizationDetail.getLdap()).thenReturn("1");
        when(iEmployeeUtilizationDetail.getName()).thenReturn("Nguyen Van A");
        when(iEmployeeUtilizationDetail.getEmail()).thenReturn("a.1@example.com");
        when(iEmployeeUtilizationDetail.getEmployeeUtilizationId()).thenReturn(1);
        when(iEmployeeUtilizationDetail.getProjectName()).thenReturn("Project X");
        when(iEmployeeUtilizationDetail.getStartDate()).thenReturn(startDate);
        when(iEmployeeUtilizationDetail.getEndDate()).thenReturn(endDate);
        when(iEmployeeUtilizationDetail.getBillable()).thenReturn(10.5);
        when(iEmployeeUtilizationDetail.getPtoOracle()).thenReturn(5);
        when(iEmployeeUtilizationDetail.getBillableHours()).thenReturn(40);
        when(iEmployeeUtilizationDetail.getLoggedHours()).thenReturn(45);
        when(iEmployeeUtilizationDetail.getAvailableHours()).thenReturn(0);

		when(employeeUtilizationServiceMock.getEmployeeUtilizationDetailByEmployeeUtilizationId(Mockito.anyInt())).thenReturn(iEmployeeUtilizationDetail);

		BaseResponse<IEmployeeUtilizationDetail> result = employeeUtilizationController.getEmployeeUtilizationDetailByEmployeeUtilizationId(1);

		assertNotNull(result);
		assertEquals(HttpStatus.OK.value(), result.getStatus());
		assertNull(result.getMessage());
		assertEquals(iEmployeeUtilizationDetail, result.getData());
	}

    @Test
    void testGetProjectInformationByEmployeeUtilizationId_whenValidData_thenSuccess() {

        Timestamp startDate = DateFormatUtils.convertTimestampFromString("2023-08-01");
        Timestamp endDate = DateFormatUtils.convertTimestampFromString("2023-08-30");

        IEmployeeUtilizationDetailResponse mockEmployeeUtilization = mock(IEmployeeUtilizationDetailResponse.class);
        when(mockEmployeeUtilization.getProjectName()).thenReturn("FSCMS");
        when(mockEmployeeUtilization.getProjectManager()).thenReturn("Pm A");
        when(mockEmployeeUtilization.getStartDate()).thenReturn(startDate);
        when(mockEmployeeUtilization.getEndDate()).thenReturn(endDate);
        when(mockEmployeeUtilization.getBillable()).thenReturn(60.5);

        when(employeeUtilizationServiceMock.getProjectInformationByEmployeeUtilizationId(Mockito.anyInt())).thenReturn(mockEmployeeUtilization);

        BaseResponse<IEmployeeUtilizationDetailResponse> result = employeeUtilizationController.getProjectInformationByEmployeeUtilizationId(1);

        assertNotNull(result);
        assertEquals(HttpStatus.OK.value(), result.getStatus());
        assertNull(result.getMessage());
        assertEquals(mockEmployeeUtilization, result.getData());
    }

    @Test
    void testGetEmployeeUtilizationDetailByHccId_whenValidData_thenSuccess() {

        Timestamp startDate = DateFormatUtils.convertTimestampFromString("2023-08-01");
        Timestamp endDate = DateFormatUtils.convertTimestampFromString("2023-08-30");

        IEmployeeUtilizationModel mockEmployeeUtilization = mock(IEmployeeUtilizationModel.class);
		EmployeeUtilizationModelResponse employeeUtilizationModelResponse = new EmployeeUtilizationModelResponse();
        when(mockEmployeeUtilization.getProjectName()).thenReturn("Project X");
        when(mockEmployeeUtilization.getStartDate()).thenReturn(startDate);
        when(mockEmployeeUtilization.getEndDate()).thenReturn(endDate);
        when(mockEmployeeUtilization.getBillable()).thenReturn(10.5);
        when(mockEmployeeUtilization.getPtoOracle()).thenReturn(5);
        when(mockEmployeeUtilization.getBillableHours()).thenReturn(40);
        when(mockEmployeeUtilization.getLoggedHours()).thenReturn(45);
        when(mockEmployeeUtilization.getAvailableHours()).thenReturn(0);
        when(mockEmployeeUtilization.getDuration()).thenReturn("01 Aug 2023 - 31 Aug 2023");
        when(mockEmployeeUtilization.getLockTime()).thenReturn(endDate);

		employeeUtilizationModelResponse.setEmployeeUtilizationModels(List.of(mockEmployeeUtilization));
		employeeUtilizationModelResponse.setAvgPtoOracle(5.0);
		employeeUtilizationModelResponse.setAvgBillableHours(40.0);
		employeeUtilizationModelResponse.setAvgBillable(10.5);
		employeeUtilizationModelResponse.setAvgLoggedHours(45.0);
		employeeUtilizationModelResponse.setAvgAvailableHours(0.0);

        when(employeeUtilizationServiceMock.getEmployeeUtilizationDetailByHccId(Mockito.anyString())).thenReturn(employeeUtilizationModelResponse);

        BaseResponse<EmployeeUtilizationModelResponse> result = employeeUtilizationController.getEmployeeUtilizationDetailByHccId("123456");

        assertNotNull(result);
        assertEquals(HttpStatus.OK.value(), result.getStatus());
        assertNull(result.getMessage());
        assertEquals(employeeUtilizationModelResponse, result.getData());
    }

	@Test
	public void testSearchEmployeeUtilization_whenValidData_thenSuccess() {

		IEmployeeUTModel iEmployeeUTModelOne = mock(IEmployeeUTModel.class);

		IEmployeeUTModel iEmployeeUTModelTwo = mock(IEmployeeUTModel.class);
		EmployeeUTModel employeeUTModel = new EmployeeUTModel();

		Integer no = 0;
		Integer limit = 10;
		String sortBy = "name";
		Boolean desc = true;

		Timestamp startDate = DateFormatUtils.convertTimestampFromString("2023-08-01");
		Timestamp endDate = DateFormatUtils.convertTimestampFromString("2023-08-30");

		when(iEmployeeUTModelOne.getEmployeeUtilizationId()).thenReturn(1);
		when(iEmployeeUTModelOne.getHccId()).thenReturn("Hcc1");
		when(iEmployeeUTModelOne.getName()).thenReturn("Nguyen Van B");
		when(iEmployeeUTModelOne.getEmail()).thenReturn("a.1@example.com");
		when(iEmployeeUTModelOne.getLdap()).thenReturn("Ldap1");
		when(iEmployeeUTModelOne.getBillable()).thenReturn(50.5);
		when(iEmployeeUTModelOne.getDuration()).thenReturn("01 Aug 2023 - 30 Aug 2023");
		when(iEmployeeUTModelOne.getStartDate()).thenReturn(startDate);
		when(iEmployeeUTModelOne.getEndDate()).thenReturn(endDate);
		when(iEmployeeUTModelOne.getCreatedDate()).thenReturn(endDate);

		when(iEmployeeUTModelTwo.getEmployeeUtilizationId()).thenReturn(1);
		when(iEmployeeUTModelTwo.getHccId()).thenReturn("Hcc2");
		when(iEmployeeUTModelTwo.getName()).thenReturn("Nguyen Van A");
		when(iEmployeeUTModelTwo.getEmail()).thenReturn("a.2@example.com");
		when(iEmployeeUTModelTwo.getLdap()).thenReturn("Ldap2");
		when(iEmployeeUTModelTwo.getBillable()).thenReturn(60.5);
		when(iEmployeeUTModelTwo.getDuration()).thenReturn("01 Aug 2023 - 30 Aug 2023");
		when(iEmployeeUTModelTwo.getStartDate()).thenReturn(startDate);
		when(iEmployeeUTModelTwo.getEndDate()).thenReturn(endDate);
		when(iEmployeeUTModelTwo.getCreatedDate()).thenReturn(endDate);

		List<IEmployeeUTModel> iEmployeeUTModels = Arrays.asList(iEmployeeUTModelOne,iEmployeeUTModelTwo);
		Page<IEmployeeUTModel> mockPage = new PageImpl<>(iEmployeeUTModels);
		employeeUTModel.setAvgBillable(55.5);
		employeeUTModel.setIEmployeeUTModels(mockPage);
		when(employeeUtilizationServiceMock.searchEmployeeUtilization(any(String.class), any(String.class), 
		any(Integer.class), any(Integer.class), any(Integer.class), any(Integer.class), any(String.class), 
		any(String.class), any(Integer.class), any(Integer.class), any(String.class), 
		any(Boolean.class))).thenReturn(employeeUTModel);

		BaseResponse<EmployeeUTModel> result = employeeUtilizationController.searchEmployeeUtilization("Van", "60", 1, 1, 1,1,"2023-08-01","2023-08-31", no, limit, sortBy, desc);

		assertNotNull(result);
		assertEquals(HttpStatus.OK.value(), result.getStatus());
		assertNull(result.getMessage());
		assertEquals(employeeUTModel, result.getData());
	}
	
	@Test
	void testGetEmployeesUtilizationNoImport_whenValidData_thenSuccess() {
        String fromDate = "2023-01-01";
        String toDate = "2023-12-31";
		List<EmployeeUtilizationNoImport> expectedResult = new ArrayList<>();
		when(employeeUtilizationServiceMock.getListEmployeeUtilizationWithNoImport(fromDate, toDate))
                .thenReturn(expectedResult);

        BaseResponse<List<EmployeeUtilizationNoImport>> result = employeeUtilizationController.getEmployeesNoImportUtilization(fromDate, toDate);
        assertEquals(HttpStatus.OK.value(), result.getStatus());
        assertEquals(expectedResult, result.getData());
	}
	@Test
	public void testGetAvgUtilization() {
		List<Integer> years = new ArrayList<>();
		years.add(2023);
		Integer branchId = 1;
		Integer coeCoreTeamId = 2;
		Integer coeId = 3;

		List<AverageYearUTModel> expectedList = new ArrayList<>();

		when(employeeUtilizationServiceMock.getListAverageMonthByUtilization(years, branchId, coeId, coeCoreTeamId))
				.thenReturn(expectedList);

		BaseResponse<List<AverageYearUTModel>> response = employeeUtilizationController.getAvgUtilization(years, branchId, coeCoreTeamId, coeId);

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals(expectedList, response.getData());
	}
}
