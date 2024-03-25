package com.hitachi.coe.fullstack.controller;


import com.hitachi.coe.fullstack.constant.CommonConstant;
import com.hitachi.coe.fullstack.model.EmployeeUTModel;
import com.hitachi.coe.fullstack.model.EmployeeUtilizationFree;
import com.hitachi.coe.fullstack.model.EmployeeUtilizationModelResponse;
import com.hitachi.coe.fullstack.model.EmployeeUtilizationNoImport;
import com.hitachi.coe.fullstack.model.IEmployeeUtilizationDetail;
import com.hitachi.coe.fullstack.model.IEmployeeUtilizationDetailResponse;
import com.hitachi.coe.fullstack.model.IPieChartModel;
import com.hitachi.coe.fullstack.model.AverageYearUTModel;
import com.hitachi.coe.fullstack.model.common.BaseResponse;
import com.hitachi.coe.fullstack.service.EmployeeUtilizationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee-utilization")
public class EmployeeUtilizationController {

	@Autowired
	EmployeeUtilizationService employeeUtilizationService;

	/**
	 * This API search employees in utilization.
	 *
	 * @param branchId The ID of the branch to retrieve utilization statistics for, or null to retrieve statistics for all branches.
	 * @param coeCoreTeamId The ID of the COE core team to retrieve utilization statistics for, or null to retrieve statistics for all COE core teams.
	 * @param coeId The ID of the COE to retrieve utilization statistics for, or null to retrieve statistics for all COEs.
	 * @param fromDate The start date of the date range to retrieve utilization statistics for, in the format "yyyy-MM-dd".
	 * @param toDate The end date of the date range to retrieve utilization statistics for, in the format "yyyy-MM-dd".
	 * @return BaseResponse containing the list of employees.
	 * @author loi ta
	 * @author tquangpham edited in 14/08/2023
	 */
	@GetMapping("/pie-chart")
	public BaseResponse<List<IPieChartModel>> getUtilizationPieChart(@RequestParam(value="branchId", required=false) Integer branchId,
																	   @RequestParam(value="coeCoreTeamId", required=false) Integer coeCoreTeamId,
																	   @RequestParam(value="coeId", required=false) Integer coeId,
																	   @RequestParam(value="fromDate", required=false) String fromDate,
																	   @RequestParam(value="toDate", required=false) String toDate) {
		return new BaseResponse<>(HttpStatus.OK.value(), null, employeeUtilizationService.getUtilizationPieChart(branchId, coeId,coeCoreTeamId,fromDate,toDate));
	}

	/**
	 * This API search employees in utilization.
	 *
	 * @param keyword       The keyword to search for.
	 * @param billable      The billable to search for.
	 * @param branchId      The branch id of Branch.
	 * @param coeCoreTeamId The coeCoreTeam id of CoE Core Team.
	 * @param coeId     The Center Of Excellence id of Center Of Excellence
	 * @param fromDate The start date of the date range to retrieve utilization statistics for, in the format "yyyy-MM-dd".
	 * @param toDate The end date of the date range to retrieve utilization statistics for, in the format "yyyy-MM-dd".
	 * @param no            The page number to retrieve.
	 * @param limit         The maximum employee of results to return per page.
	 * @param sortBy        The field to sort the results by.
	 * @param desc          The field to sort desc or asc the results.
	 * @return BaseResponse containing the list of employees.
	 * @author tquangpham
	 */
	@GetMapping("/search")
	public BaseResponse<EmployeeUTModel> searchEmployeeUtilization(@RequestParam(required = false) String keyword, @RequestParam(required = false) String billable, @RequestParam(required = false) Integer branchId,
																	   @RequestParam(required = false) Integer coeCoreTeamId, @RequestParam(required = false) Integer coeId, @RequestParam(required = false) Integer coeUTId,
																   @RequestParam(value="fromDate", required=false) String fromDate, @RequestParam(value="toDate", required=false) String toDate,
																   @RequestParam(defaultValue = "0") Integer no, @RequestParam(defaultValue = "10") Integer limit,
																   @RequestParam(defaultValue = "name") String sortBy, @RequestParam(required = false) Boolean desc) {
		return new BaseResponse<>(HttpStatus.OK.value(), null, employeeUtilizationService.searchEmployeeUtilization(keyword, billable, branchId, coeCoreTeamId, coeId, coeUTId, fromDate, toDate, no, limit, sortBy,desc));
	}

	/**
	 * This method is used to create an API to get information of employees utilization detail by employee hccId.
	 *
	 * @param hccId is employee utilization Id from employee utilization table
	 * @return a list that contains average numbers for statistics and a list of employee utilization by employee hccId
	 * @author tquangpham
	 */

	@GetMapping("/detail-HCCID/{hccId}")
	public BaseResponse<EmployeeUtilizationModelResponse> getEmployeeUtilizationDetailByHccId(@PathVariable("hccId") String hccId) {
		return new BaseResponse<>(HttpStatus.OK.value(), null, employeeUtilizationService.getEmployeeUtilizationDetailByHccId(hccId));
	}

	/**
	 * This method is used to create an API to get list of employees that have no utilization.
	 * @param fromDate The start date of the date range to retrieve utilization statistics for, in the format "yyyy-MM-dd".
	 * @param toDate The end date of the date range to retrieve utilization statistics for, in the format "yyyy-MM-dd".
	 * @param no            The page number to retrieve.
	 * @param limit         The maximum employee of results to return per page.
	 * @param sortBy        The field to sort the results by.
	 * @param desc          The field to sort desc or asc the results.
	 * @return A BaseResponse object with a status code, a message and data.
	 * @author tquangpham
	 */
	@GetMapping("/no-utilization")
	@ApiOperation("This API will return a list of employees with no utilization.")
	public BaseResponse<EmployeeUtilizationFree> getEmployeesNoUtilization(@RequestParam(value="fromDate", required=false) String fromDate,
																		   @RequestParam(value="toDate", required=false) String toDate,
																		   @RequestParam(defaultValue = "0") Integer no, 
																		   @RequestParam(defaultValue = "10") Integer limit,
																		   @RequestParam(defaultValue = "employeeName") String sortBy, 
																		   @RequestParam(required = false) Boolean desc) {
		return new BaseResponse<>(HttpStatus.OK.value(), null,employeeUtilizationService.getListEmployeeUtilizationWithNoUT(CommonConstant.BILLABLE_THRESHOLD, fromDate, toDate, no, limit, sortBy, desc));
	}

	/**
	 * This method is used to create an API to get information of employees utilization detail.
	 *
	 * @param employeeUtilizationId  id of employee utilization to get employee utilization details information
	 * @return A BaseResponse object with a status code, a message and data.
	 * @author tquangpham
	 */
	@GetMapping("/detail/{employeeUtilizationId}")
	@ApiOperation("This API will return an employee utilization detail.")
	public BaseResponse<IEmployeeUtilizationDetail> getEmployeeUtilizationDetailByEmployeeUtilizationId(@PathVariable Integer employeeUtilizationId) {
		return new BaseResponse<>(HttpStatus.OK.value(), null,employeeUtilizationService.getEmployeeUtilizationDetailByEmployeeUtilizationId(employeeUtilizationId));
	}

	/**
	 * This method is used to create an API to get information of project information by employee utilization.
	 *
	 * @param employeeUtilizationId  id of employee utilization to get employee utilization details information
	 * @return A BaseResponse object with a status code, a message and data.
	 * @author tquangpham
	 */
	@GetMapping("/detail-project/{employeeUtilizationId}")
	@ApiOperation("This API will return an project information by employee utilization.")
	public BaseResponse<IEmployeeUtilizationDetailResponse> getProjectInformationByEmployeeUtilizationId(@PathVariable Integer employeeUtilizationId) {
		return new BaseResponse<>(HttpStatus.OK.value(), null,employeeUtilizationService.getProjectInformationByEmployeeUtilizationId(employeeUtilizationId));
	}
	
	/**
	 * This method is used to create an API to get list of employees that have no
	 * import utilization.
	 * 
	 * @param fromDate The start date of the date range to retrieve utilization
	 *                 statistics for, in the format "yyyy-MM-dd".
	 * @param toDate   The end date of the date range to retrieve utilization
	 * @author loita
	 */
	@GetMapping("/no-import-utilization")
	@ApiOperation("This API will return a list of employees with no utilization.")
	public BaseResponse<List<EmployeeUtilizationNoImport>> getEmployeesNoImportUtilization(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate) {
		return new BaseResponse<>(HttpStatus.OK.value(), null, employeeUtilizationService
				.getListEmployeeUtilizationWithNoImport(fromDate, toDate));
	}
	
	/**
	Retrieves a list of average utilization by month.
	@param years A list of years for which to calculate the average utilization.
	@param branchId (Optional) The ID of the branch to filter the results by.
	@param coeCoreTeamId (Optional) The ID of the COE core team to filter the results by.
	@param coeId (Optional) The ID of the COE to filter the results by.
	@author lta
	*/
	@GetMapping("/get-average-utilization")
	@ApiOperation("This API will return a list of average of month.")
	public BaseResponse<List<AverageYearUTModel>> getAvgUtilization(
			@RequestParam(value = "years", required = true) List<Integer> years, @RequestParam(required = false) Integer branchId,
			@RequestParam(required = false) Integer coeCoreTeamId, @RequestParam(required = false) Integer coeId) {
		return new BaseResponse<>(HttpStatus.OK.value(), null, employeeUtilizationService
				.getListAverageMonthByUtilization(years, branchId, coeId,coeCoreTeamId));
	}
}
