package com.hitachi.coe.fullstack.service;


import com.hitachi.coe.fullstack.model.EmployeeStatusModel;
import com.hitachi.coe.fullstack.model.ExcelResponseModel;
import com.hitachi.coe.fullstack.model.ImportResponse;

import java.io.IOException;

/**
 * The interface EmployeeStatusService is the interface to EmployeeStatusServiceImplement.
 * 
 * @author loita
 *
 */
public interface EmployeeStatusService {
	/**
	 * Create status employee in employeeStatus entity with status 0 (deleted).
	 * 
	 * @param id {@link Integer}
	 * @return {@link EmployeeStatusModelTests}
	 */
	  public EmployeeStatusModel deleteEmployeeById(Integer id);

	/**
	 * @author ngocth
	 * @param listOfEmployee listOfEmployee list employees who have left.
	 * @return
	 * @throws IOException Throws an IOException if there is an error while reading data from the ExcelResponseModel or processing the data.
	 */
	ImportResponse importLeaveEmployee(ExcelResponseModel listOfEmployee) throws IOException;
}
