package com.hitachi.coe.fullstack.repository;

import com.hitachi.coe.fullstack.entity.EmployeeUtilization;
import com.hitachi.coe.fullstack.model.IEmployeeUTModel;
import com.hitachi.coe.fullstack.model.IEmployeeUtilizationDetail;
import com.hitachi.coe.fullstack.model.IEmployeeUtilizationDetailResponse;
import com.hitachi.coe.fullstack.model.IEmployeeUtilizationFree;
import com.hitachi.coe.fullstack.model.IEmployeeUtilizationModel;
import com.hitachi.coe.fullstack.model.IEmployeeUtilizationNoImport;
import com.hitachi.coe.fullstack.model.IPieChartModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;


/**
 * The Interface EmployeeUtilizationRepository is used to access EmployeeUtilization table.
 */
@Repository
public interface EmployeeUtilizationRepository extends JpaRepository<EmployeeUtilization, Integer> {

	/**

	Get data for utilization pie chart by percentage distribution of utilization hours of employees.
	@param branchId branch id (optional)
	@param coeCoreTeamId coe core team id of employee (optional)
	@param coeId coe id of employee (optional)
	@param fromDate start date of utilization time (optional)
	@param toDate end date of utilization time (optional)
	@return List of Object[], each Object[] includes utilization range and number of employees in that range.
	@author lta
	@author tquangpham edited in 14/08/2023
	*/
	@Query(value = "SELECT labels as label, amount as data FROM get_utilization_pie_chart(CAST(CAST(:branchId AS TEXT )AS INTEGER), CAST(CAST(:coeId AS TEXT )AS INTEGER),CAST(CAST(:coeCoreTeamId AS TEXT )AS INTEGER), CAST(CAST(:fromDate AS TEXT)AS DATE), CAST(CAST(:toDate AS TEXT)AS DATE));", nativeQuery = true)
	List<IPieChartModel> getUtilizationPieChart( Integer branchId, Integer coeId, Integer coeCoreTeamId, @Param("fromDate") Timestamp fromDate, @Param("toDate") Timestamp toDate);

	/**
	 * Get list of employees in utilization.
	 *
	 * @param keyword       The keyword to search for.
	 * @param branchId      The branch id of Branch.
	 * @param coeCoreTeamId The coeCoreTeam id of CoE Core Team.
	 * @param coeTeamId     The Center Of Excellence id of Center Of Excellence.
	 * @param fromDate start date of utilization time (optional)
	 * @param toDate end date of utilization time (optional)
	 * @return A List of IEmployeeUTModel interface that match the selected conditions.
	 * @author tquangpham
	 */
	@Query(value = "SELECT eu.id as employeeUtilizationId, em.hcc_id as hccId, em.name as name, em.email as email, em.ldap as ldap, eu.billable as billable, cu.duration as duration, cu.start_date AS startDate, cu.end_date AS endDate, cu.created_date AS createdDate "
			+ "FROM employee_utilization eu "
			+ "JOIN coe_utilization cu ON cu.id = eu.coe_utilization_id "
			+ "JOIN ( SELECT date_trunc('month', end_date) AS month, MAX(end_date) AS max_date "
			+ "FROM coe_utilization "
			+ "WHERE start_date BETWEEN COALESCE(CAST(CAST(:fromDate AS TEXT)AS DATE), start_date) AND COALESCE(CAST(CAST(:toDate AS TEXT)AS DATE), start_date) "
			+ "GROUP BY month) coeUT ON coeUT.max_date = cu.end_date "
			+ "JOIN employee em ON eu.employee_id = em.id "
			+ "JOIN branch br ON em.branch_id = br.id "
			+ "JOIN coe_core_team ct ON em.coe_core_team_id = ct.id "
			+ "JOIN center_of_excellence coe ON ct.coe_id = coe.id "
			+ "WHERE (:keyword IS NULL or em.ldap LIKE CONCAT('%', :keyword, '%') or em.hcc_id LIKE CONCAT('%', :keyword, '%') or em.email LIKE CONCAT('%', :keyword, '%') or em.name LIKE CONCAT('%', :keyword, '%')) "
			+ "AND (:branchId IS NULL OR br.id = CAST(CAST(:branchId AS TEXT) AS INTEGER)) "
			+ "AND (:coeCoreTeamId IS NULL OR ct.id = CAST(CAST(:coeCoreTeamId AS TEXT) AS INTEGER)) "
			+ "AND (:coeTeamId IS NULL OR coe.id = CAST(CAST(:coeTeamId AS TEXT) AS INTEGER)) "
			+ "AND cu.id in ("
			+ "SELECT coeut2.id "
			+ "FROM coe_utilization coeut2 "
			+ "JOIN ( "
			+ "SELECT coeut1.end_date, max(coeut1.created_date) as lasted_date "
			+ "FROM coe_utilization coeut1 "
			+ "WHERE coeut1.end_date = cu.end_date "
			+ "GROUP BY coeut1.end_date "
			+ ") coeLasted ON coeLasted.end_date = coeut2.end_date AND coeLasted.lasted_date = coeut2.created_date) "
			, nativeQuery = true)
	List<IEmployeeUTModel> searchEmployeeUtilization(@Param("keyword") String keyword, @Param("branchId") Integer branchId,
													 @Param("coeCoreTeamId") Integer coeCoreTeamId, @Param("coeTeamId") Integer coeTeamId, @Param("fromDate") Timestamp fromDate, @Param("toDate") Timestamp toDate);


	/**
	 * Get list of employees in utilization.
	 *
	 * @param keyword       The keyword to search for.
	 * @param branchId      The branch id of Branch.
	 * @param coeCoreTeamId The coeCoreTeam id of CoE Core Team.
	 * @param coeTeamId     The Center Of Excellence id of Center Of Excellence.
	 * @param coeUTId The CoE Utilization id of CoE Utilization.
	 * @return A List of IEmployeeUTModel interface that match the selected conditions.
	 * @author tquangpham
	 */
	@Query(value = "SELECT eu.id as employeeUtilizationId, em.hcc_id as hccId, em.name as name, em.email as email, em.ldap as ldap, eu.billable as billable, cu.duration as duration, cu.start_date AS startDate, cu.end_date AS endDate, cu.created_date AS createdDate "
			+ "FROM employee_utilization eu "
			+ "JOIN coe_utilization cu ON cu.id = eu.coe_utilization_id "
			+ "JOIN employee em ON eu.employee_id = em.id "
			+ "JOIN branch br ON em.branch_id = br.id "
			+ "JOIN coe_core_team ct ON em.coe_core_team_id = ct.id "
			+ "JOIN center_of_excellence coe ON ct.coe_id = coe.id "
			+ "WHERE (:keyword IS NULL or em.ldap LIKE CONCAT('%', :keyword, '%') or em.hcc_id LIKE CONCAT('%', :keyword, '%') or em.email LIKE CONCAT('%', :keyword, '%') or em.name LIKE CONCAT('%', :keyword, '%')) "
			+ "AND (:branchId IS NULL OR br.id = CAST(CAST(:branchId AS TEXT) AS INTEGER)) "
			+ "AND (:coeCoreTeamId IS NULL OR ct.id = CAST(CAST(:coeCoreTeamId AS TEXT) AS INTEGER)) "
			+ "AND (:coeTeamId IS NULL OR coe.id = CAST(CAST(:coeTeamId AS TEXT) AS INTEGER)) "
			+ "AND (:coeUTId IS NULL OR cu.id = CAST(CAST(:coeUTId AS TEXT)AS INTEGER)) "
			, nativeQuery = true)
	List<IEmployeeUTModel> searchEmployeeUtilizationByUtilizationId(@Param("keyword") String keyword, @Param("branchId") Integer branchId,
													 @Param("coeCoreTeamId") Integer coeCoreTeamId, @Param("coeTeamId") Integer coeTeamId, @Param("coeUTId") Integer coeUTId);
	/**
	 * @param hccId from employee table
	 * @return List of IEmployeeUtilizationModel base on the column follow the
	 *         query.
	 * @author tquangpham
	 */
	@Query(nativeQuery = true, value = "SELECT empUt.available_hours AS availableHours, empUt.billable_hours AS billableHours, "
			+ "empUt.pto_oracle AS ptoOracle, empUt.logged_hours AS loggedHours, "
			+ "empUt.billable AS billable, ut.start_date AS startDate, "
			+ "ut.end_date AS endDate, ut.duration AS Duration, empUt.created_date AS lockTime, "
			+ "pj.name as projectName "
			+ "FROM employee_utilization empUt "
			+ "JOIN employee emp ON emp.id = empUt.employee_id "
			+ "JOIN coe_utilization ut ON ut.id = empUt.coe_utilization_id "
			+ "JOIN project pj ON empUt.project_code = pj.code "
			+ "WHERE emp.hcc_id = CAST(:hccId AS TEXT) ")
	List<IEmployeeUtilizationModel> getEmployeeUtilizationDetailByHccId(@Param("hccId") String hccId);

	/**
	 * Retrieves an employees utilization detail.
	 *
	 * @param id from employee utilization table
	 * @return List of IEmployeeUtilizationDetail base on the column follow the
	 *         query.
	 * @author tquangpham
	 */
	@Query(nativeQuery = true, value = "SELECT pj.name AS projectName, pj.project_manager AS projectManager, pj.start_date AS startDate, "
			+ "pj.end_date AS endDate, eu.billable AS billable "
			+ "FROM employee_utilization eu, project pj "
			+ "WHERE eu.project_code = pj.code AND eu.id = :id")
	IEmployeeUtilizationDetailResponse getProjectInformationByEmployeeUtilizationId(@Param("id") Integer id);

	/**
	 * Retrieves a list of employees that have no utilization.
	 *
	 * @param billableThreshold The ID of the branch. Pass null to include all branches.
	 * @param fromDate start date of utilization time (optional)
	 * @param toDate end date of utilization time (optional)
	 * @return the list of employees that have no utilization.
	 * @author tquangpham, tminhto (format, change return type to Page)
	 */
	@Query(value = "SELECT "
        + "    noUt.hccid AS hccId, "
        + "    noUt.id AS employeeId, "
        + "    noUt.employeeName AS employeeName, "
        + "    noUt.email AS email, "
        + "    noUt.branchName AS branchName, "
        + "    noUt.businessName AS businessName, "
        + "    noUt.teamName AS teamName, "
        + "    noUt.daysFree AS daysFree "
        + "FROM getEmployeeUtilizationNoUT( "
        + "    CAST(CAST(:billableThreshold AS TEXT) AS DOUBLE PRECISION), "
        + "    CAST(CAST(:fromDate AS TEXT) AS DATE), "
        + "    CAST(CAST(:toDate AS TEXT) AS DATE) "
        + ") noUt",
        nativeQuery = true)
	Page<IEmployeeUtilizationFree> getEmployeesUtilizationNoUT(	@Param("billableThreshold") Double billableThreshold, 
																@Param("fromDate") Timestamp fromDate, 
																@Param("toDate") Timestamp toDate,
																Pageable pageable);


	/**
	 * Retrieves an employees utilization detail.
	 *
	 * @param id The ID of employee utilization table.
	 * @return the list of employees utilization detail.
	 * @author tquangpham
	 */
	@Query(value = "SELECT e.ldap, e.hcc_id AS hccId, e.name, e.email, " +
			"eu.id AS employeeUtilizationId, " +
			"p.name AS projectName, cu.start_date AS startDate, cu.end_date AS endDate, " +
			"eu.billable, eu.pto_oracle AS ptoOracle, eu.billable_hours AS billableHours, " +
			"eu.logged_hours AS loggedHours, eu.available_hours AS availableHours, cu.duration AS duration " +
			"FROM project p " +
			"JOIN employee_utilization eu ON eu.project_code = p.code " +
			"JOIN coe_utilization cu ON cu.id = eu.coe_utilization_id " +
			"JOIN employee e ON e.id = eu.employee_id " +
			"WHERE eu.id = :id", nativeQuery = true)
	IEmployeeUtilizationDetail getEmployeeUtilizationDetailById(@Param("id") Integer id);

	/**
	 * Retrieves a list of employee utilization data with no recorded import for the
	 * specified month and year.
	 *
	 * @param month The month (1-12) for which the utilization data is requested.
	 * @param year  The year for which the utilization data is requested.
	 * @return A list of IEmployeeUtilizationNoImport objects representing the
	 *         employee utilization data.
	 * @author loita, tminhto (formnat, modify status <> 0)
	 */
	@Query(value = "SELECT "
			+ "e.hcc_id as hccId, "
			+ "e.id as employeeId, "
			+ "e.name as employeeName, "
			+ "e.email, "
			+ "b.name as branchName, "
			+ "bu.name as businessName, "
			+ "coe.name as teamName "
			+ "FROM employee e "
			+ "INNER JOIN branch b ON b.id = e.branch_id "
			+ "INNER JOIN business_unit bu ON bu.id = e.business_unit_id "
			+ "INNER JOIN coe_core_team coe ON coe.id = e.coe_core_team_id "
			+ "INNER JOIN ( "
			+ "    SELECT es.employee_id, MAX(es.status_date) AS max_status_date "
			+ "    FROM employee_status es "
			+ "    WHERE es.status <> 0 "
			+ "    GROUP BY es.employee_id "
			+ ") latest_status ON latest_status.employee_id = e.id "
			+ "INNER JOIN employee_status es ON es.status_date = latest_status.max_status_date "
			+ "LEFT JOIN ( "
			+ "    SELECT eu.employee_id as id "
			+ "    FROM employee_utilization eu "
			+ "    INNER JOIN coe_utilization cu ON cu.id = eu.coe_utilization_id "
			+ "    WHERE EXTRACT(MONTH FROM cu.start_date) = :month "
			+ "    AND EXTRACT(YEAR FROM cu.start_date) = :year "
			+ ") recorded on recorded.id = e.id "
			+ "WHERE recorded.id IS NULL ", nativeQuery = true)
	List<IEmployeeUtilizationNoImport> getEmployeeUtilizationNoImport(@Param("month") Integer month,
																	  @Param("year") Integer year);



/**
	 * Retrieves the average month utilization data for the specified month, year, branch, COE (Center of Excellence),
	 * and COE Core Team.
	 *
	 * @param month          The month (1-12) for which the average utilization data is requested.
	 * @param year           The year for which the average utilization data is requested.
	 * @param branchId       The branch identifier. Pass null if not applicable.
	 * @param coeId          The COE (Center of Excellence) identifier. Pass null if not applicable.
	 * @param coeCoreTeamId  The COE Core Team identifier. Pass null if not applicable.
	 * @return An IPieChartModel object representing the average month utilization data.
	 * @author loita
	 */
	@Query(value = "SELECT ROUND(SUM(CAST(sb.billable AS numeric))/ec.employee_count,2) AS data" +
			"	FROM " +
			"	( " +
			"		SELECT " +
			"			eu.billable" +
			"		FROM" +
			"			public.employee_utilization eu" +
			"		INNER JOIN" +
			"			public.employee e ON e.id = eu.employee_id" +
			"   	INNER JOIN " +
			"			public.center_of_excellence coe ON coe.id = e.coe_core_team_id" +
			"		INNER JOIN" +
			"			public.coe_utilization cu ON cu.id = eu.coe_utilization_id" +
			"		WHERE" +
			"        EXTRACT(MONTH FROM cu.start_date) = CAST(CAST(:month AS TEXT) AS INTEGER)" +
			"        AND EXTRACT(MONTH FROM cu.end_date) = CAST(CAST(:month AS TEXT) AS INTEGER)" +
			"        AND EXTRACT(YEAR FROM cu.start_date) = CAST(CAST(:year AS TEXT) AS INTEGER)" +
			"   	 AND (:branchId IS NULL OR e.branch_id = CAST(CAST(:branchId AS TEXT) AS INTEGER))" +
			"        AND (:coeCoreTeamId IS NULL OR e.coe_core_team_id = CAST(CAST(:coeCoreTeamId AS TEXT) AS INTEGER))" +
			"        AND (:coeId IS NULL OR coe.id = CAST(CAST(:coeId AS TEXT) AS INTEGER))" +
			") AS sb," +
			"( " +
			"	SELECT COUNT(DISTINCT e.id) AS employee_count" +
			"	FROM public.employee e " +
			"	INNER JOIN public.employee_status es ON es.employee_id = e.id " +
			"   INNER JOIN public.center_of_excellence coe ON coe.id = e.coe_core_team_id" +
			"	INNER JOIN public.employee_level el ON e.id = el.employee_id" +
			"	INNER JOIN (" +
			"		SELECT es.employee_id, MAX(es.status_date) AS max_status_date" +
			"		FROM employee_status es" +
			"		WHERE es.status <> 0" +
			"		GROUP BY es.employee_id" +
			"	) latest_status ON latest_status.employee_id = e.id " +
			"	WHERE" +
			"		 el.level_id <> 9  " +  // --intern
			"   	 AND (:branchId IS NULL OR e.branch_id = CAST(CAST(:branchId AS TEXT) AS INTEGER))" +
			"        AND (:coeCoreTeamId IS NULL OR e.coe_core_team_id = CAST(CAST(:coeCoreTeamId AS TEXT) AS INTEGER))" +
			"        AND (:coeId IS NULL OR coe.id = CAST(CAST(:coeId AS TEXT) AS INTEGER))" +
			")  AS ec" +
			"	group by ec.employee_count", nativeQuery = true)
	IPieChartModel getAverageMonthUtilization(@Param("month") Integer month,@Param("year") Integer year, @Param("branchId") Integer branchId,
											  @Param("coeId") Integer coeId,@Param("coeCoreTeamId") Integer coeCoreTeamId);
}
