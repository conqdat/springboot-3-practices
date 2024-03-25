package com.hitachi.coe.fullstack.service;

import static com.hitachi.coe.fullstack.enums.Status.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.hitachi.coe.fullstack.model.EmployeeLeaveModel;
import com.hitachi.coe.fullstack.model.ExcelErrorDetail;
import com.hitachi.coe.fullstack.model.ExcelResponseModel;
import com.hitachi.coe.fullstack.model.ImportResponse;
import com.hitachi.coe.fullstack.repository.EmployeeRepository;
import com.hitachi.coe.fullstack.service.impl.EmployeeStatusServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.hitachi.coe.fullstack.entity.Employee;
import com.hitachi.coe.fullstack.entity.EmployeeStatus;
import com.hitachi.coe.fullstack.model.EmployeeStatusModel;
import com.hitachi.coe.fullstack.repository.EmployeeStatusRepository;
import com.hitachi.coe.fullstack.transformation.EmployeeStatusTransformer;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-data-test.properties")
public class EmployeeStatusServiceTests {

	@Autowired
	private EmployeeStatusService employeeStatusService;
	
	@MockBean
	private EmployeeStatusTransformer employeeStatusTransformer;
	@MockBean
	private EmployeeStatusRepository employeeStatusRepository;
	@MockBean
	private EmployeeService employeeService;
	@MockBean
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeStatusServiceImpl employeeStatusServiceIml;
	@Test
	void testDeleteEmployeeById_OnCommonSuccess() {
			Employee employee = new Employee();
			employee.setId(15);

			EmployeeStatus employeeStatus = new EmployeeStatus();
			employeeStatus.setStatus(0);
			employeeStatus.setEmployee(employee);

			EmployeeStatusModel employeeStatusModel = new EmployeeStatusModel();
			employeeStatusModel.setStatus(0);
			employeeStatus.setEmployee(employee);


			when(employeeStatusRepository.createDeleteStatusEmployee(anyInt())).thenReturn(employeeStatus);
			when(employeeStatusRepository.findById(anyInt())).thenReturn(Optional.of(employeeStatus));
			when(employeeStatusTransformer.apply(employeeStatus)).thenReturn(employeeStatusModel);

			EmployeeStatusModel employeeStatusCreated =  employeeStatusService.deleteEmployeeById(15);

			assertEquals(0,employeeStatusCreated.getStatus());
	}

	
//	TODO
//	mvn test have Failures: Expected com.hitachi.coe.fullstack.exceptions.CoEException to be thrown, but nothing was thrown.
//	@Test
//	void testDeleteEmployeeById_OnCommonNotFound() {
//			Employee employee = new Employee();
//			employee.setId(15);
//
//			EmployeeStatus employeeStatus = new EmployeeStatus();
//			employeeStatus.setStatus(0);
//			employeeStatus.setEmployee(employee);
//
//			EmployeeStatusModel employeeStatusModel = new EmployeeStatusModel();
//			employeeStatusModel.setStatus(0);
//			employeeStatus.setEmployee(employee);
//
//			when(employeeStatusRepository.findById(anyInt())).thenReturn(Optional.ofNullable(null));
//
//			Throwable throwable = assertThrows(CoEException.class, 
//					() -> employeeStatusService.deleteEmployeeById(anyInt()));
//		
//	        assertEquals(CoEException.class, throwable.getClass());
//	        assertEquals(ErrorConstant.MESSAGE_EMPLOYEE_NULL, throwable.getMessage());
//	}
	@Test
	public void testImportLeaveEmployee() {
		ExcelResponseModel excelResponseModel = createSampleExcelResponseModel();
		Employee employee1 = new Employee();
		employee1.setId(2);
		Employee employee2 = new Employee();
		employee2.setId(3);
		when(employeeRepository.findByLdap("125351")).thenReturn(Optional.empty());
		when(employeeRepository.findByLdap("125282")).thenReturn(Optional.of(employee1));
		when(employeeRepository.findByLdap("125283")).thenReturn(Optional.of(employee2));
		ImportResponse importResponse = employeeStatusServiceIml.importLeaveEmployee(excelResponseModel);
		assertEquals(2, importResponse.getSuccessRows());
		assertEquals(2, importResponse.getErrorRows());

	}

	private ExcelResponseModel createSampleExcelResponseModel() {
		ExcelResponseModel excelResponseModel = new ExcelResponseModel();
		excelResponseModel.setData(createSampleData());
		excelResponseModel.setTotalRows(3);
		excelResponseModel.setErrorDetails(createErrorData());
		excelResponseModel.setStatus(SUCCESS);
		return excelResponseModel;
	}
	private HashMap<Integer, Object> createSampleData() {
		HashMap<Integer, Object> data = new HashMap<>();
		data.put(0, createSampleEmployeeLeaveModel("125351", "125351", "To Minh Tri", "C1", "4/12/2021", "huy.mai@hitachivantara.com", "DS IOT Embedded Systems", "(HV) VN Ho Chi Minh City - QTSC Bldg", "5/24/2023", "Nguyen Quang Huy", "Vo Nguyen Anh Tho", "IoT", "hello"));
		data.put(1, createSampleEmployeeLeaveModel("125282", "125282", "Dat Cong Tran", "C2", "10/23/2019", "loc.nguyen@hitachivantara.com", "DS IOT Embedded Systems", "(HV) VN Ho Chi Minh City - QTSC Bldg", "06/30/23", "Nguyen Quang Huy", "Vo Nguyen Anh Tho", "IoT", "hello"));
		data.put(2, createSampleEmployeeLeaveModel("125283", "148162", "Nguyen Phuoc", "C2", "5/17/2021", "hung.tran3@hitachivantara.com", "Asset Lifecycle Management", "(HV) VN Ho Chi Minh City - Helios Building", "02/07/23", "Huynh Tan Hai", "Vo Nguyen Anh Tho", "IoT", "hello"));

		return data;
	}
	private EmployeeLeaveModel createSampleEmployeeLeaveModel(
			String ldap, String oracleId, String employeeName, String level,
			String legalEntityHireDate, String email, String businessUnit, String location,
			String leaveDate, String reportManager, String hinextManager, String buCode, String reason) {

		EmployeeLeaveModel employeeLeaveModel = new EmployeeLeaveModel();
		employeeLeaveModel.setLdap(ldap);
		employeeLeaveModel.setOracleId(oracleId);
		employeeLeaveModel.setEmployeeName(employeeName);
		employeeLeaveModel.setLevel(level);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		try {
			Date hireDate = dateFormat.parse(legalEntityHireDate);
			employeeLeaveModel.setLegalEntityHireDate(hireDate);

			Date leaveDateValue = dateFormat.parse(leaveDate);
			employeeLeaveModel.setLeaveDate(leaveDateValue);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		employeeLeaveModel.setEmail(email);
		employeeLeaveModel.setBusinessUnit(businessUnit);
		employeeLeaveModel.setLocation(location);
		employeeLeaveModel.setReportManager(reportManager);
		employeeLeaveModel.setHinextManager(hinextManager);
		employeeLeaveModel.setBuCode(buCode);
		employeeLeaveModel.setReason(reason);
		employeeLeaveModel.setId(null);
		employeeLeaveModel.setCreated(null);
		employeeLeaveModel.setUpdated(null);
		employeeLeaveModel.setUpdatedBy(null);
		return employeeLeaveModel;
	}

	private List<ExcelErrorDetail> createErrorData() {
		List<ExcelErrorDetail> mockErrorDetails = new ArrayList<>();
		ExcelErrorDetail mockErrorDetail = new ExcelErrorDetail();
		mockErrorDetail.setRowIndex(1);
		mockErrorDetail.setDetails(new HashMap<>());
		mockErrorDetail.getDetails().put("Employee", "Employee do not exist");
		mockErrorDetails.add(mockErrorDetail);
		return mockErrorDetails;
	}


}
