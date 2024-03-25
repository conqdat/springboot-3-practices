package com.hitachi.coe.fullstack.service;

import com.hitachi.coe.fullstack.model.EmployeeUTModel;
import com.hitachi.coe.fullstack.model.EmployeeUtilizationFree;
import com.hitachi.coe.fullstack.model.EmployeeUtilizationModelResponse;
import com.hitachi.coe.fullstack.model.EmployeeUtilizationNoImport;
import com.hitachi.coe.fullstack.model.ExcelResponseModel;
import com.hitachi.coe.fullstack.model.IEmployeeUtilizationDetail;
import com.hitachi.coe.fullstack.model.IEmployeeUtilizationDetailResponse;
import com.hitachi.coe.fullstack.model.IPieChartModel;
import com.hitachi.coe.fullstack.model.ImportResponse;
import com.hitachi.coe.fullstack.model.AverageYearUTModel;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
/**
 * This class is a service to import employee_utilization table.
 */

public interface EmployeeUtilizationService {
	/**
	 * Imports employee utilization data from an Excel file.
	 *
	 * @param listOfEmployeeUT the list of Excel Response Model that contains data to import into the EmployeeUtilization table.
	 * @param fileType is the type of file input.
	 * @return the ImportResponse object.
	 * @author tquangpham
	 */
	ImportResponse importEmployeeUtilization(ExcelResponseModel listOfEmployeeUT, String fileType, InputStream stream, String strDate) throws IOException;

	/**
	 * Retrieves a PieChartModel representing utilization statistics for a specific branch, COE core team, or COE within a specified date range.
	 *
	 * @author lta
	 * @author tquangpham edited in 14/08/2023
	 * @param branchId The ID of the branch to retrieve utilization statistics for, or null to retrieve statistics for all branches.
	 * @param coeCoreTeamId The ID of the COE core team to retrieve utilization statistics for, or null to retrieve statistics for all COE core teams.
	 * @param coeId The ID of the COE to retrieve utilization statistics for, or null to retrieve statistics for all COEs.
	 * @param fromDateStr The start date of the date range to retrieve utilization statistics for, in the format "yyyy-MM-dd".
	 * @param toDateStr The end date of the date range to retrieve utilization statistics for, in the format "yyyy-MM-dd".
	 * @return A PieChartModel representing utilization statistics for the specified branch, COE core team, or COE within the specified date range.
	 * @throws CoeException If the fromDateStr or toDateStr parameters are not in the format "yyyy-MM-dd".
	 */
	List<IPieChartModel> getUtilizationPieChart(Integer branchId, Integer coeId, Integer coeCoreTeamId, String fromDateStr, String toDateStr);

	/**
	 * Get list of employees in utilization.
	 *
	 * @param keyword       The keyword to search for.
	 * @param billable      The billable to search for.
	 * @param branchId      The branch id of Branch.
	 * @param coeCoreTeamId The coeCoreTeam id of CoE Core Team.
	 * @param coeId     The Center Of Excellence id of Center Of Excellence.
	 * @param coeUTId The CoE Utilization id of CoE Utilization.
	 * @param fromDateStr The start date of the date range to retrieve utilization statistics for, in the format "yyyy-MM-dd".
	 * @param toDateStr The end date of the date range to retrieve utilization statistics for, in the format "yyyy-MM-dd".
	 * @param no            The page number to retrieve.
	 * @param limit         The maximum employee of results to return per page.
	 * @param sortBy        The field to sort the results by.
	 * @param desc          The field to sort desc or asc the results.
	 * @return An EmployeeUTModel that match the selected conditions.
	 * @author tquangpham
	 */
	EmployeeUTModel searchEmployeeUtilization(String keyword, String billable, Integer branchId, Integer coeCoreTeamId,
											  Integer coeId, Integer coeUTId, String fromDateStr, String toDateStr, Integer no, Integer limit, String sortBy, Boolean desc);


	/**
	 * Get project information by employee utilization by employee HCC ID.
	 *
	 * @param hccId is employee hccId from employee table
	 * @return a list that contains average numbers for statistics and a list of employee utilization by employee hccId
	 * @author tquangpham
	 */

	EmployeeUtilizationModelResponse getEmployeeUtilizationDetailByHccId(String hccId);

	/**
	 * Get project information by employee utilization by employee utilization id.
	 *
	 * @param empUtId is employee utilization id from employee utilization table
	 * @return a list of employee utilization details by employee utilization id
	 * @author tquangpham
	 */

	IEmployeeUtilizationDetailResponse getProjectInformationByEmployeeUtilizationId(Integer empUtId);
	/**
	 * Get list of employees that have no utilization.
	 *
	 * @param billableThreshold the Billable Threshold to retrieve list of employees that have no utilization.
	 * @param fromDateStr The start date of the date range to retrieve utilization statistics for, in the format "yyyy-MM-dd".
	 * @param toDateStr The end date of the date range to retrieve utilization statistics for, in the format "yyyy-MM-dd".
	 * @param no              The page number to retrieve.
	 * @param limit           The maximum employee of results to return per page.
	 * @param sortBy          The field to sort the results by.
	 * @param desc            The field to sort desc or asc the results.
	 * @return the list of employees utilization.
	 * @author tquangpham
	 */
	EmployeeUtilizationFree getListEmployeeUtilizationWithNoUT(Double billableThreshold, String fromDateStr, String toDateStr, Integer no, Integer limit, String sortBy, Boolean desc);

	/**
	 * Get an employee utilization detail by employee utilization id.
	 *
	 * @param id The ID of employee utilization table
	 * @return an employee utilization detail.
	 * @author tquangpham
	 */
	IEmployeeUtilizationDetail getEmployeeUtilizationDetailByEmployeeUtilizationId(Integer id);

	/**
	 * Get list of employees that have no import utilization.
	 *
	 * @param fromDateStr       The start date of the date range to retrieve
	 *                          utilization statistics for, in the format
	 *                          "yyyy-MM-dd".
	 * @param toDateStr         The end date of the date range to retrieve
	 *                          utilization statistics for, in the format
	 *                          "yyyy-MM-dd".
	 * @return the list of employees utilization.
	 * @author loita
	 */
	List<EmployeeUtilizationNoImport> getListEmployeeUtilizationWithNoImport(String fromDateStr, String toDateStr);

	/**
	 * Retrieves a list of average month utilization data by utilizing the specified criteria.
	 *
	 * @param fromDateStr    The starting list interger for get list of years.
	 * @param branchId       The branch identifier.
	 * @param coeId          The COE (Center of Excellence) identifier.
	 * @param coeCoreTeamId  The COE core team identifier.
	 * @return A list of IPieChartModel objects representing the average month utilization data.
	 * @author loita
	 */
	List<AverageYearUTModel> getListAverageMonthByUtilization(List<Integer> years, Integer branchId,
															  Integer coeId, Integer coeCoreTeamId);

}
