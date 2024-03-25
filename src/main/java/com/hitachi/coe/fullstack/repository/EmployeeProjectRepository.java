package com.hitachi.coe.fullstack.repository;

import com.hitachi.coe.fullstack.entity.EmployeeProject;
import com.hitachi.coe.fullstack.model.IEmployeeProjectDetails;
import com.hitachi.coe.fullstack.model.IEmployeeProjectModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeProjectRepository extends JpaRepository<EmployeeProject, Integer> {

    /**
     * Filters employees in project with status.
     *
     * @param keyword   The keyword to search for.
     * @param projectId The project id of Project.
     * @param status    The status including assign or release
     * @return A list of employees that match the selected conditions.
     * @author tquangpham
     */
    @Query(value = "SELECT " +
            "    ep.id AS employeeProjectId, " +
            "    e.id AS employeeId, " +
            "    ep.project_id AS projectId, " +
            "    e.hcc_id AS hccId, " +
            "    e.name AS name, " +
            "    e.email AS email, " +
            "    ep.employee_type AS employeeType, " +
            "    ep.start_date AS startDate, " +
            "    ep.end_date AS endDate, " +
            "    ep.created_date AS createdDate, " +
            "    ep.release_date AS releaseDate, " +
            "    ep.utilization AS utilization, " +
            "    bu.manager AS pmName " +
            "FROM employee_project ep " +
            "JOIN employee e ON e.id = ep.employee_id " +
            "JOIN business_unit bu ON bu.id = e.business_unit_id " +
            "WHERE " +
            "    (:keyword IS NULL " +
            "    OR e.hcc_id = :keyword " +
            "    OR e.email LIKE :keyword " +
            "    OR e.name LIKE :keyword) " +
            "    AND ep.project_id = :projectId " +
            "    AND ( " +
            "        (:status IS NULL " +
            "        OR ( " +
            "            (NOW() < ep.release_date OR ep.release_date IS NULL) " +
            "            AND :status = 'working' " +
            "        )) " +
            "        OR ( " +
            "            (ep.release_date < NOW() AND ep.release_date IS NOT NULL) " +
            "            AND :status = 'released' " +
            "        ) " +
            "    OR :status IS NULL) " +
            "    AND (:isCreatedDateLatest = false OR ep.created_date = ( " +
            "        SELECT MAX(created_date) " +
            "        FROM employee_project " +
            "        WHERE employee_id = e.id " +
            "    ))",
            nativeQuery = true)
    Page<IEmployeeProjectModel> searchEmployeesProjectWithStatus(@Param("projectId") Integer projectId,
                                                                 @Param("keyword") String keyword,
                                                                 @Param("status") String status,
                                                                 @Param("isCreatedDateLatest") Boolean isCreatedDateLatest,
                                                                 Pageable page);

    /**
     * Filters projects that an employee is assigning except projects with projectId.
     *
     * @param employeeId The employee id of Employee.
     * @param projectId  The project id of Project.
     * @return A list of projects that match the selected conditions.
     * @author tquangpham
     */
    @Query("select ep from EmployeeProject ep where ep.employee.id = :employeeId and ep.project.id != :projectId and (ep.releaseDate > now() OR ep.releaseDate IS NULL) ")
    List<EmployeeProject> findEmployeeAssignedExceptProjectById(Integer employeeId, Integer projectId);

    /**
     * This method is used to find the project details by a given employee's hccId
     *
     * @param hccId the employee's hccId to retrieve the project details from
     * @return the project details information
     * @author tminhto
     * @see IEmployeeProjectDetails
     */
    @Query(value = "SELECT  " +
            "    ep.employee_type AS employeeType, " +
            "    ep.start_date AS startDate, " +
            "    ep.end_date AS endDate, " +
            "    ep.created_date AS createdDate, " +
            "    ep.release_date AS releaseDate, " +
            "    ep.utilization AS utilization, " +
            "    pro.code AS projectCode, " +
            "    pro.name AS projectName, " +
            "    pro.project_manager AS projectManager, " +
            "    pro.status AS projectStatus, " +
            "    pt.name AS projectTypeName " +
            "FROM  " +
            "    public.employee_project ep " +
            "    LEFT JOIN public.project pro ON pro.id = ep.project_id " +
            "    LEFT JOIN public.project_type pt ON pro.project_type_id = pt.id " +
            "    LEFT JOIN public.employee emp ON emp.id = ep.employee_id " +
            "WHERE  " +
            "    emp.hcc_id = :hccId " +
            "    AND (:isCreatedDateLatest = false OR ep.created_date = ( " +
            "        SELECT MAX(ep2.created_date) " +
            "        FROM public.employee_project ep2" +
            "        WHERE ep2.employee_id = emp.id " +
            "    ))" +
            "ORDER BY ep.created_date DESC",
            nativeQuery = true)
    List<IEmployeeProjectDetails> getEmployeeProjectDetailsByEmployeeHccId(@Param("hccId") String hccId, @Param("isCreatedDateLatest") Boolean isCreatedDateLatest);
}
