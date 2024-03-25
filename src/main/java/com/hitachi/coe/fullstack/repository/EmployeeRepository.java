package com.hitachi.coe.fullstack.repository;

import com.hitachi.coe.fullstack.entity.Employee;
import com.hitachi.coe.fullstack.model.IBarChartDepartmentModel;
import com.hitachi.coe.fullstack.model.IEmployeeDetails;
import com.hitachi.coe.fullstack.model.IEmployeeSearchAdvance;
import com.hitachi.coe.fullstack.model.IEmployeeSearchForTeam;
import com.hitachi.coe.fullstack.model.IEmployeeSpecificField;
import com.hitachi.coe.fullstack.model.IResultOfQueryBarChart;
import com.hitachi.coe.fullstack.model.IResultOfQueryCountEmployees;
import com.hitachi.coe.fullstack.model.IResultOfQuerySkillAndLevel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * The Interface EmployeeRepository is using to access Employee table.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    /**
     * Filters employees based on the provided conditions.
     *
     * @param keyword          The keyword to search for. If null, no keyword
     *                         filtering will be applied.
     * @param businessUnitName The businessUnit name to filter by. If null, no filtering
     *                         by businessUnit name will be applied.
     * @param coeCoreTeamName  The COE core team name to filter by. If null, no
     *                         filtering by COE core team name will be applied.
     * @param branchName       The branch name to filter by. If null, no filtering by
     *                         branch name will be applied .
     * @param fromDate         The start date for filtering. If null, no filtering by
     *                         start date will be applied.
     * @param toDate           The end date for filtering. If null, no filtering by
     *                         end date will be applied.
     * @param page             The pagination information.
     * @return A page of employees that match the selected conditions.
     * @author nnien
     */
    @Query("SELECT e " +
            "FROM Employee e " +
            "JOIN e.businessUnit bu " +
            "JOIN e.coeCoreTeam c " +
            "JOIN e.branch b " +
            "WHERE  ((:keyword IS NULL) " +
            "       OR (e.ldap LIKE %:keyword%) " +
            "       OR (e.hccId LIKE %:keyword%) " +
            "       OR (e.email LIKE %:keyword%) " +
            "       OR (e.name LIKE %:keyword%)) " +
            "       AND ((:businessUnitName IS NULL) OR (bu.name LIKE %:businessUnitName%)) " +
            "       AND ((:coeCoreTeamName IS NULL) OR (c.name LIKE %:coeCoreTeamName%)) " +
            "       AND ((:branchName IS NULL) OR (b.name LIKE %:branchName%)) " +
            "       AND (CAST(:fromDate AS date) IS NULL OR e.legalEntityHireDate >= :fromDate) " +
            "       AND (CAST(:toDate AS date) IS NULL OR (e.legalEntityHireDate <= :toDate)) " +
            "       AND e.id IN ( " +
            "               SELECT es.employee.id " +
            "               FROM EmployeeStatus es " +
            "               WHERE :status IS NULL OR es.status = :status " +
            "               AND es.employee.id = e.id " +
            "               GROUP BY es.employee.id " +
            "               HAVING MAX(es.statusDate) = ( " +
            "                        SELECT MAX(es2.statusDate) " +
            "                        FROM EmployeeStatus es2 " +
            "                        WHERE es2.employee.id = es.employee.id ) " +
            "                   )")
    Page<Employee> filterEmployees(@Param("keyword") String keyword,
                                   @Param("businessUnitName") String businessUnitName,
                                   @Param("coeCoreTeamName") String coeCoreTeamName,
                                   @Param("branchName") String branchName,
                                   @Param("status") Integer status,
                                   @Param("fromDate") Date fromDate,
                                   @Param("toDate") Date toDate,
                                   Pageable page);

	/**
	 * Delete employees form system.
	 *
	 * @author loita
	 */
	@Query(value = "UPDATE public.employee SET updated_by= 'admin', updated_date=CURRENT_DATE WHERE id = ?1 RETURNING *", nativeQuery = true)
	Employee deleteEmployeeById(@PathVariable("id") Integer id);

	/**
	 * Finds an employee by HCC ID and LDAP.
	 *
	 * @param hccId The HCC ID of the employee.
	 * @param ldap  The LDAP of the employee.
	 * @return The employee matching the given HCC ID and LDAP, or null if not
	 *         found.
	 * @author tquangpham
	 */
	@Query("SELECT e FROM Employee e WHERE e.hccId = :hccId AND e.ldap = :ldap")
	Employee findByHccIdAndLdap(String hccId, String ldap);

	/**
	 * Finds an employee by employee email or HCC ID and LDAP.
	 * 
	 * @param email The email of the employee.
	 * @param hccId The HCC ID of the employee.
	 * @param ldap  The LDAP of the employee.
	 * @return The employee matching the given employee email, HCC ID and LDAP, or
	 *         null if not found.
	 * @author tquangpham
	 */
	@Query("SELECT e FROM Employee e WHERE e.email = :email OR e.ldap = :ldap OR e.hccId = :hccId ")
	List<Employee> findByEmailOrLdapOrHccId(@Param("email") String email, @Param("ldap") String ldap,
			@Param("hccId") String hccId);
                        
        /**
         * Find an employee by LDAP or HCCID.
         *
         * @param hccId the hccId to search for
         * @param ldap  the ldap to search for
         * @return An {@link Optional} containing the employee if found, or an empty {@link Optional} otherwise.
         * @author tminhto
         */
        @Query("SELECT e FROM Employee e WHERE e.hccId = :hccId OR e.ldap = :ldap")
        Optional<Employee> findByLdapOrHccId(String hccId, String ldap);

	/**
	 * Get the quantity of per skill bar chart based on the specified branch, group,
	 * and team IDs.
	 *
	 * @param branchId The ID of the branch to filter the data (optional).
	 * @param groupId  The ID of the group to filter the data (optional).
	 * @param teamId   The ID of the team to filter the data (optional).
	 * @return An array of objects representing the quantity values for the levels
	 *         bar chart.
	 * @author hchantran
	 */
	@Query(nativeQuery = true, value = "SELECT result.levelName as level, result.total as quantity FROM getQuantityEmployeeOfLevel(CAST(CAST(:branchId AS TEXT) AS INTEGER),"
			+ "CAST(CAST(:groupId AS TEXT) AS INTEGER), " + "CAST(CAST(:teamId AS TEXT) AS INTEGER)) AS result")
	List<IBarChartDepartmentModel> getQuantityEmployeeOfLevel(@Param("branchId") Integer branchId,
			@Param("groupId") Integer groupId, @Param("teamId") Integer teamId);

    /**
     * Finds an employee by HCC ID.
     *
     * @param hccId The HCC ID of the employee.
     * @return The employee matching the given HCC ID, or null if not found.
     * @author tquangpham
     */
    @Query("SELECT e FROM Employee e WHERE e.hccId = :hccId")
    Employee findByHccId(String hccId);

	/**
	 * Get the quantity of per skill bar chart based on the specified branch, group,
	 * and team IDs.
	 *
	 * @param branchId The ID of the branch to filter the data (optional).
	 * @param groupId  The ID of the group to filter the data (optional).
	 * @param teamId   The ID of the team to filter the data (optional).
	 * @param SkillIds The list Id of the skill to filter the data (optional).
	 * @return An array of objects representing the quantity values for the skill
	 * bar chart.
	 */
	@Query(value = "SELECT result.labels as label, result.skillName as fieldName, result.total as total "
			+ "FROM getQuantityOfSkillByConditions(Cast(Cast(:branchId as text)as Integer), Cast(Cast(:groupId as text)as Integer), "
			+ "Cast(Cast(:teamId as text)as Integer), Cast(:SkillIds as text)) as result", nativeQuery = true)
	List<IResultOfQueryBarChart> getQuantityOfEachSkillForBarChart(@Param("branchId") Integer branchId,
																   @Param("groupId") Integer groupId,
																   @Param("teamId") Integer teamId,
																   @Param("SkillIds") String SkillIds);

	/**
	 * Retrieves the count of employees for each level.
	 *
	 * @param branchId The ID of the branch. Pass null to include all branches.
	 * @param groupId  The ID of the group. Pass null to include all groups.
	 * @param teamId   The ID of the team. Pass null to include all teams.
	 * @return A list of objects containing the label name, level name, and the
	 * total count of employees.
	 */
	@Query(nativeQuery = true, value = "SELECT results.labels as label, results.levelName as fieldName, results.total as total "
			+ "FROM getQuantityOfLevelForBarChart(CAST(CAST(:branchId AS TEXT) AS INTEGER),"
			+ "CAST(CAST(:groupId AS TEXT) AS INTEGER), " + "CAST(CAST(:teamId AS TEXT) AS INTEGER)) AS results")
	List<IResultOfQueryBarChart> getQuantityOfLevelForBarChart(@Param("branchId") Integer branchId,
															   @Param("groupId") Integer groupId,
															   @Param("teamId") Integer teamId);

    /**
     * This method is used to get the details of the employee by the specified employee's hccId.
     *
     * @param hccId the employee hccId to retrieve employee details information from
     * @return employee details information
     * @author tminhto
     * @see IEmployeeDetails
     */
    @Query(value = "SELECT   " +
            "emp.id as id,   " +
            "emp.hcc_id as hccId,   " +
            "emp.name as name,  " +
            "emp.email as email,  " +
            "emp.ldap as ldap,   " +
            "emp.legal_entity_hire_date as legalEntityHireDate,  " +
            "br.name as branchName,  " +
            "center.name as centerOfExcellenceName,  " +
            "coeCoreTeam.name as coeCoreTeamName,  " +
            "coeCoreTeamSubLeader.name as coeCoreTeamSubLeaderName,  " +
            "bu.name as businessUnitName,  " +
            "le.code as levelCode,   " +
            "le.name as levelName,   " +
            "emp.coe_core_team_id as coeCoreTeamId,   " +
            "emp.branch_id as branchId,   " +
            "center.id as centerOfExcellenceId,   " +
            "empStatus.status as employeeStatus,   " +
            "string_agg(ss.name, ', ') as skillSetName, " +
            "t1.sum_ut as sumUtilization, " +
            "CASE  " +
            "    WHEN 0 = ANY(t1.employee_type_arr) AND t1.sum_ut >= 85 THEN 'Billable' " +
            "    WHEN 0 = ANY(t1.employee_type_arr) AND t1.sum_ut > 0 THEN 'Non-billable' " +
            "    WHEN 1 = ANY(t1.employee_type_arr) AND t1.sum_ut > 0 THEN 'Non-billable' " +
            "    ELSE 'On-bench' " +
            "END AS employeeWorkingStatus " +
            "FROM public.employee emp   " +
            "LEFT JOIN public.branch br ON br.id = emp.branch_id   " +
            "LEFT JOIN public.coe_core_team coeCoreTeam ON coeCoreTeam.id = emp.coe_core_team_id   " +
            "LEFT JOIN public.employee coeCoreTeamSubLeader ON coeCoreTeamSubLeader.id = coeCoreTeam.sub_leader_id   " +
            "LEFT JOIN public.center_of_excellence center ON center.id = coeCoreTeam.coe_id   " +
            "LEFT JOIN public.business_unit bu ON bu.id = emp.business_unit_id   " +
            "LEFT JOIN public.employee_level el ON el.employee_id = emp.id   " +
            "LEFT JOIN public.level le ON le.id = el.level_id   " +
            "LEFT JOIN public.employee_skill es ON es.employee_id = emp.id   " +
            "LEFT JOIN public.skill_set ss ON ss.id = es.skill_set_id   " +
            "LEFT JOIN public.employee_status empStatus ON empStatus.employee_id = emp.id   " +
            "LEFT JOIN ( " +
            "    SELECT  " +
            "        ep.employee_id, " +
            "        SUM(ep.utilization) AS sum_ut, " +
            "        array_agg(ep.employee_type) as employee_type_arr " +
            "    FROM public.employee_project ep " +
            "    WHERE now() < ep.release_date OR ep.release_date IS NULL  " +
            "    GROUP BY ep.employee_id " +
            ") t1 ON t1.employee_id = emp.id " +
            "WHERE emp.hcc_id = :hccId   " +
            "GROUP BY emp.id, emp.hcc_id, emp.name, emp.email, emp.ldap, emp.legal_entity_hire_date, br.name,   " +
            "center.name, coeCoreTeam.name, coeCoreTeamSubLeader.name, bu.name, le.code, le.name, el.level_date,   " +
            "emp.coe_core_team_id, emp.branch_id, center.id, el.level_date, empStatus.status_date, empStatus.status, " +
            "t1.sum_ut, t1.employee_type_arr " +
            "ORDER BY el.level_date DESC, empStatus.status_date DESC   " +
            "LIMIT 1", nativeQuery = true)
    IEmployeeDetails getEmployeeDetailsByHccId(@Param("hccId") String hccId);

    /**
     * Filters employees based on the provided conditions.
     *
     * @param keyword       The keyword to search for. If null, no keyword
     *                      filtering will be applied.
     * @param buId          The businessUnit id to filter by. If null, no filtering
     *                      by businessUnit name will be applied.
     * @param coeCoreTeamId The coeCoreTeamId id to filter by. If null, no filtering
     *                      *               by coeCoreTeamId name will be applied.
     * @param status        The status of employee. If null, no
     *                      filtering by COE core team name will be applied.
     * @return A List of employees that match the selected conditions.
     * @author tquangpham (created), tminhto (edited)
     * @modifier SangBui 30/11/2023 Fix bugs
     * @see IEmployeeSearchAdvance
     */
    @Query(value = "SELECT emp.id AS id, " +
            "emp.hcc_id AS hccId, " +
            "emp.ldap AS ldap, " +
            "emp.name AS name, " +
            "emp.email AS email, " +
            "emp.legal_entity_hire_date AS legalEntityHireDate, " +
            "bu.name AS businessUnitName, " +
            "bu.manager AS pm, " +
            "br.name AS branchName, " +
            "coeCoreTeam.name AS coeCoreTeamName, " +
            "CASE " +
            "   WHEN 0 = ANY(t1.employee_type_arr) AND t1.sum_ut >= 85 THEN 'Billable' " +
            "   WHEN 0 = ANY(t1.employee_type_arr) AND t1.sum_ut > 0 THEN 'Non-billable' " +
            "   WHEN 1 = ANY(t1.employee_type_arr) AND t1.sum_ut > 0 THEN 'Non-billable' " +
            "   ELSE 'On-bench' " +
            "END AS employeeWorkingStatus, " +
            "CASE " +
            "   WHEN t1.sum_ut IS NULL THEN FALSE " +
            "   ELSE TRUE " +
            "END AS isWorkingOnAnotherProject " +
            "FROM employee emp " +
            "LEFT JOIN public.branch br ON br.id = emp.branch_id " +
            "LEFT JOIN public.business_unit bu ON bu.id = emp.business_unit_id " +
            "LEFT JOIN public.coe_core_team coeCoreTeam ON coeCoreTeam.id = emp.coe_core_team_id " +
            "LEFT JOIN (    SELECT ep.employee_id, " +
            "                   SUM(ep.utilization) AS sum_ut, " +
            "                   array_agg(ep.employee_type) AS employee_type_arr " +
            "               FROM public.employee_project ep " +
            "               WHERE now() < ep.release_date OR ep.release_date IS NULL " +
            "               GROUP BY ep.employee_id   " +
            "          ) t1 ON t1.employee_id = emp.id " +
            "WHERE ( :keyword IS NULL " +
            "       OR emp.hcc_id = :keyword " +
            "       OR emp.email LIKE :keyword " +
            "       OR emp.name LIKE :keyword " +
            "       OR emp.ldap LIKE :keyword " +
            "      ) " +
            "AND ( :buId IS NULL OR bu.id = CAST(CAST(:buId AS TEXT) AS INTEGER) ) " +
            "AND ( :coeCoreTeamId IS NULL OR coeCoreTeam.id = CAST(CAST(:coeCoreTeamId AS TEXT) AS INTEGER) ) " +
            "AND ( :branchId IS NULL OR br.id = CAST(CAST(:branchId AS TEXT) AS INTEGER) ) " +
            "AND ( CAST(CAST(:fromDate AS TEXT) AS DATE) IS NULL OR emp.legal_entity_hire_date >= CAST(CAST(:fromDate AS TEXT) AS DATE) ) " +
            "AND ( CAST(CAST(:toDate AS TEXT) AS DATE) IS NULL OR emp.legal_entity_hire_date <= CAST(CAST(:toDate AS TEXT) AS DATE) ) " +
            "AND emp.id IN  " +
            "   (   SELECT es.employee_id " +
            "       FROM employee_status es " +
            "       WHERE ( CASE " +
            "           WHEN :status IS NULL THEN es.status <> 0 " +
            "           ELSE es.status = CAST(CAST(:status AS TEXT) AS INTEGER) END ) " +
            "       AND es.employee_id = emp.id " +
            "       GROUP BY es.employee_id " +
            "       HAVING MAX(es.status_date) = " +
            "           (  SELECT MAX(es2.status_date) " +
            "              FROM employee_status es2 " +
            "              WHERE es2.employee_id = es.employee_id " +
            "           ) " +
            "   )", nativeQuery = true)
    Page<IEmployeeSearchAdvance> searchEmployeeAdvance(@Param("keyword") String keyword,
                                                       @Param("buId") Integer buId,
                                                       @Param("coeCoreTeamId") Integer coeCoreTeamId,
                                                       @Param("branchId") Integer branchId,
                                                       @Param("status") Integer status,
                                                       @Param("fromDate") LocalDate fromDate,
                                                       @Param("toDate") LocalDate toDate,
                                                       Pageable pageable);

    /**
     * Finds an employee by LDAP without checking for latest status.
     *
     * @param ldap The LDAP of the employee.
     * @return The employee matching the given LDAP, or null if not
     * found.
     * @author ngocth (created), tminhto (last edited)
     */
    @Query(value = "select " +
            "    emp.id, " +
            "    emp.ldap, " +
            "    emp.hcc_id, " +
            "    emp.name, " +
            "    emp.email, " +
            "    emp.legal_entity_hire_date, " +
            "    emp.business_unit_id, " +
            "    emp.branch_id, " +
            "    emp.coe_core_team_id, " +
            "    emp.created_by, " +
            "    emp.created_date, " +
            "    emp.updated_by, " +
            "    emp.updated_date " +
            "FROM employee emp " +
            "WHERE emp.ldap = :ldap", nativeQuery = true)
    Optional<Employee> findByLdap(@Param("ldap") String ldap);

    /**
     * Get the list of quantity of employees and count of each skill level for each skill, based on the provided branch, coe, skillIDs.
     *
     * @param branchId (optional) the ID of the branch to retrieve the data
     * @param coeId    (optional) the ID of the Coe to retrieve the data
     * @param skillIds The list id of the skill to filter the data
     *                 Use format like as '1,2,...'
     * @return A list of objects containing the skill name, Business Unit name, and the
     * total count of employees have a skill, and the count of employees for each level(5 level) of that skill.
     * @author SangBui
     */
    @Query(value = "SELECT " +
            "    result.buName AS label, " +
            "    result.skillName AS skillName, " +
            "    result.total AS total, " +
            "    result.level_1 AS level1,  " +
            "    result.level_2 AS level2, " +
            "    result.level_3 AS level3, " +
            "    result.level_4 AS level4, " +
            "    result.level_5 AS level5 " +
            "FROM getQuantityOfEmployeesForEachSkill( " +
            "        CAST(CAST(:branchId AS TEXT) AS INTEGER), " +
            "        CAST(CAST(:coeId AS TEXT) AS INTEGER),  " +
            "        CAST(:skillIds AS TEXT) " +
            "     ) AS result", nativeQuery = true)
    List<IResultOfQuerySkillAndLevel> getQuantityOfEmployeesForEachSkill(@Param("branchId") Integer branchId,
                                                                         @Param("coeId") Integer coeId,
                                                                         @Param("skillIds") String skillIds);

	/**
	 * Get the list of employees based on teamId.
	 * @param teamId coe core team id
	 * @return list of employees by teamId @param
	 * @author SangBui
	 */
	List<Employee> findByCoeCoreTeamId(Integer teamId);

	/**
	 * Get the list of specific employees infos based on teamId.
	 * @param teamId from coe core team entity
	 * @return list specific employees infos by teamId @param
	 * @author ThuyTrinhThanhLe
	 */
	@Query(value = "SELECT e.id AS id, e.ldap AS ldap, e.hcc_id AS hccId, e.name AS name, e.email AS email, e.legal_entity_hire_date as legalEntityHireDate " +
			"FROM employee e " +
			"WHERE e.coe_core_team_id = :teamId " +
			"GROUP BY e.id, e.ldap, e.hcc_id, e.name, e.email , e.legal_entity_hire_date", nativeQuery = true)
	List<IEmployeeSpecificField> getEmployeeSpecificFieldByTeam(@Param("teamId") Integer teamId);

    /**
     * Get the count of total employees, employees on projects, employees on bench and new employees
     * of current month and last month
     *
     * @return a list of count of employees in each category of current month and last month
     * @author SangBui
     * @author ThuyTrinhThanhLe edited in 27/10/2023
     */
    @Query(value = "SELECT "
            + "     COUNT(DISTINCT e.id) AS totalEmployees, "
            + "     COUNT (DISTINCT CASE WHEN ((now() < empj.release_date OR empj.release_date IS NULL)  "
            + "                             AND (pj.status = 5)) "
            + "                          THEN e.id ELSE NULL END) AS onProjectEmployees, "
            + "     (SELECT COUNT(DISTINCT e.id) " +
            "           FROM public.employee e JOIN public.employee_on_bench_detail eobd ON eobd.employee_id = e.id " +
            "               AND eobd.employee_on_bench_id = (SELECT eob.id " +
            "                                     FROM employee_on_bench eob " +
            "                                     WHERE " +
            "                                     (eob.start_date <= CURRENT_DATE) AND (eob.end_date >= CURRENT_DATE) " +
            "                                     ORDER BY eob.created_date DESC " +
            "                                     LIMIT 1)) AS onBenchEmployees, "
            + "     COUNT(DISTINCT CASE WHEN (EXTRACT(MONTH FROM e.legal_entity_hire_date) = EXTRACT(MONTH FROM CURRENT_DATE)  "
            + "                             AND EXTRACT(YEAR FROM e.legal_entity_hire_date) = EXTRACT(YEAR FROM CURRENT_DATE)) "
            + "                         THEN e.id ELSE NULL END) AS newEmployees, "
            + "     'New' AS status "
            + "FROM public.employee e "
            + "LEFT JOIN public.employee_status emsta ON e.id = emsta.employee_id "
            + "LEFT JOIN public.employee_project empj ON empj.employee_id = e.id "
            + "LEFT JOIN public.project pj ON pj.id = empj.project_id "
            + "WHERE "
            + "     emsta.id IN ( "
            + "             SELECT es.id  "
            + "             FROM employee_status es  "
            + "             JOIN ( "
            + "                     SELECT DISTINCT es.employee_id, MAX(es.status_date) as latestDate  "
            + "                     FROM employee_status es  "
            + "                     JOIN employee e ON es.employee_id = e.id "
            + "                     GROUP BY es.employee_id "
            + "             ) newEmpls ON newEmpls.latestDate = es.status_date AND es.employee_id = newEmpls.employee_id "
            + "             WHERE es.status <> 0 "
            + "     ) "
            + "UNION ALL "
            + "SELECT "
            + "     COUNT(DISTINCT e.id) AS totalEmployees, "
            + "     COUNT(DISTINCT CASE WHEN ((date_trunc('month', now()) < empj.release_date OR empj.release_date IS NULL)  "
            + "                             AND (pj.status = 5))  "
            + "                         THEN e.id ELSE NULL END) AS onProjectEmployees, "
            + "     (SELECT COUNT(DISTINCT e.id) " +
            "           FROM public.employee e JOIN public.employee_on_bench_detail eobd ON eobd.employee_id = e.id " +
            "               AND eobd.employee_on_bench_id = (SELECT eob.id " +
            "                                     FROM employee_on_bench eob " +
            "                                     WHERE " +
            "                                     (eob.start_date <= (CURRENT_DATE - INTERVAL '1 MONTH')) " +
            "                                           AND (eob.end_date >= (CURRENT_DATE - INTERVAL '1 MONTH')) " +
            "                                     ORDER BY eob.created_date DESC " +
            "                                     LIMIT 1)) AS onBenchEmployees, "
            + "     COUNT(DISTINCT CASE WHEN (EXTRACT(MONTH FROM e.legal_entity_hire_date) = (EXTRACT(MONTH FROM CURRENT_DATE) - 1) "
            + "                             AND EXTRACT(YEAR FROM e.legal_entity_hire_date) = EXTRACT(YEAR FROM CURRENT_DATE)) "
            + "                         THEN e.id ELSE NULL END) AS newEmployees, "
            + "     'Old' AS status "
            + "FROM public.employee e "
            + "LEFT JOIN public.employee_status emsta ON e.id = emsta.employee_id "
            + "LEFT JOIN public.employee_project empj ON empj.employee_id = e.id "
            + "LEFT JOIN public.project pj ON pj.id = empj.project_id  "
            + "WHERE "
            + "     emsta.id IN ( "
            + "             SELECT es.id  "
            + "             FROM employee_status es  "
            + "             JOIN ( "
            + "                     SELECT DISTINCT es.employee_id, MAX(es.status_date) as latestDate  "
            + "                     FROM employee_status es  "
            + "                     JOIN employee e ON es.employee_id = e.id "
            + "                     WHERE EXTRACT(MONTH FROM es.status_date) < EXTRACT(MONTH FROM CURRENT_DATE) "
            + "                     GROUP BY es.employee_id "
            + "             ) newEmpls ON newEmpls.latestDate = es.status_date AND es.employee_id = newEmpls.employee_id "
            + "             WHERE es.status <> 0 "
            + "     ) ", nativeQuery = true)
    List<IResultOfQueryCountEmployees> getCountOfEmployeesCurrentAndLastMonth();

    /**
     * Filters employees based on the provided conditions.
     *
     * @param nameString       The name string to search for. If null, no name string
     *                      filtering will be applied.
     * @param coeId          The center of excellence id to filter by. If null, no filtering
     *                      by coeId will be applied.
     * @return A List of employees that match the selected conditions.
     * @author SangBui
     * @see Employee
     */
    @Query(value = "SELECT " +
                "emp.id AS id, " +
                "emp.name AS name, " +
                "emp.email AS email, " +
                "team.id AS coeCoreTeamId, " +
                "team.name AS coeCoreTeamName " +
                "FROM employee emp " +
                "LEFT JOIN public.employee_status emsta ON emp.id = emsta.employee_id " +
                "LEFT JOIN coe_core_team team ON team.id = emp.coe_core_team_id " +
                "LEFT JOIN center_of_excellence coe ON coe.id = team.coe_id " +
                "WHERE " +
                "       emsta.id IN ( " +
                "       SELECT es.id " +
                "       FROM employee_status es " +
                "       JOIN ( " +
                "               SELECT DISTINCT es.employee_id, MAX(es.status_date) as latestDate " +
                "               FROM employee_status es " +
                "               JOIN employee e ON es.employee_id = e.id " +
                "               GROUP BY es.employee_id " +
                "               ) newEmpls ON newEmpls.latestDate = es.status_date AND es.employee_id = newEmpls.employee_id " +
                "       WHERE es.status <> 0 " +
                "       ) " +
                "       AND ((:coeId IS NULL) OR (team.coe_id = CAST(CAST(:coeId AS TEXT) AS INTEGER))) " +
                "       AND ((:nameString IS NULL) OR (emp.name ILIKE CONCAT('%', :nameString, '%'))) " , nativeQuery = true)
    List<IEmployeeSearchForTeam> searchEmployeesByCoeAndNameString(@Param("coeId") Integer coeId,
                                                       @Param("nameString") String nameString);
}
