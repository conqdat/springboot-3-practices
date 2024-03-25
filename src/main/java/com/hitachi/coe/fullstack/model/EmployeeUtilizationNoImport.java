package com.hitachi.coe.fullstack.model;

import java.util.List;

public class EmployeeUtilizationNoImport implements IEmployeeUtilizationNoImport {
	private String hccId;
	private Integer employeeId;
	private String employeeName;
	private String email;
	private String branchName;
	private String businessName;
	private String teamName;
	private List<Integer> monthNoImport;

	public EmployeeUtilizationNoImport(String hccId, Integer employeeId, String employeeName, String email,
			String branchName, String businessName, String teamName, List<Integer> monthNoImport) {
		this.hccId = hccId;
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.email = email;
		this.branchName = branchName;
		this.businessName = businessName;
		this.teamName = teamName;
		this.monthNoImport = monthNoImport;
	}

	@Override
	public String getHccId() {
		return hccId;
	}

	@Override
	public Integer getEmployeeId() {
		return employeeId;
	}

	@Override
	public String getEmployeeName() {
		return employeeName;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getBranchName() {
		return branchName;
	}

	@Override
	public String getBusinessName() {
		return businessName;
	}

	@Override
	public String getTeamName() {
		return teamName;
	}

	public List<Integer> getMonthNoImport() {
		return monthNoImport;
	}
}
