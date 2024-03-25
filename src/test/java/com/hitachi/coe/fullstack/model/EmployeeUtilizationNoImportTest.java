package com.hitachi.coe.fullstack.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

class EmployeeUtilizationNoImportTest {

	@Test
	void testEmployeeUtilizationNoImport() {
		// Arrange
		String hccId = "H001";
		Integer employeeId = 1;
		String employeeName = "John Doe";
		String email = "john.doe@example.com";
		String branchName = "Branch A";
		String businessName = "Business A";
		String teamName = "Team A";
		List<Integer> monthNoImport = Arrays.asList(1, 2, 3);

		EmployeeUtilizationNoImport employeeUtilizationNoImportMock = mock(EmployeeUtilizationNoImport.class);

		when(employeeUtilizationNoImportMock.getHccId()).thenReturn(hccId);
		when(employeeUtilizationNoImportMock.getEmployeeId()).thenReturn(employeeId);
		when(employeeUtilizationNoImportMock.getEmployeeName()).thenReturn(employeeName);
		when(employeeUtilizationNoImportMock.getEmail()).thenReturn(email);
		when(employeeUtilizationNoImportMock.getBranchName()).thenReturn(branchName);
		when(employeeUtilizationNoImportMock.getBusinessName()).thenReturn(businessName);
		when(employeeUtilizationNoImportMock.getTeamName()).thenReturn(teamName);
		when(employeeUtilizationNoImportMock.getMonthNoImport()).thenReturn(monthNoImport);

		// Act
		String actualHccId = employeeUtilizationNoImportMock.getHccId();
		Integer actualEmployeeId = employeeUtilizationNoImportMock.getEmployeeId();
		String actualEmployeeName = employeeUtilizationNoImportMock.getEmployeeName();
		String actualEmail = employeeUtilizationNoImportMock.getEmail();
		String actualBranchName = employeeUtilizationNoImportMock.getBranchName();
		String actualBusinessName = employeeUtilizationNoImportMock.getBusinessName();
		String actualTeamName = employeeUtilizationNoImportMock.getTeamName();
		List<Integer> actualMonthNoImport = employeeUtilizationNoImportMock.getMonthNoImport();

		// Assert
		assertEquals(hccId, actualHccId);
		assertEquals(employeeId, actualEmployeeId);
		assertEquals(employeeName, actualEmployeeName);
		assertEquals(email, actualEmail);
		assertEquals(branchName, actualBranchName);
		assertEquals(businessName, actualBusinessName);
		assertEquals(teamName, actualTeamName);
		assertEquals(monthNoImport, actualMonthNoImport);
	}
}
