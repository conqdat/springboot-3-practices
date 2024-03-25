package com.hitachi.coe.fullstack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import com.hitachi.coe.fullstack.entity.EmployeeStatus;

import java.util.Date;
import java.util.Optional;

@Repository
public interface EmployeeStatusRepository extends JpaRepository<EmployeeStatus, Integer> {

    /**
     * Finds the first EmployeeStatus entity for a given employee ID, ordered by
     * statusDate in descending order.
     *
     * @param employeeId the ID of the employee
     * @return an {@link Optional} containing the first EmployeeStatus entity for
     * the employee ID,
     * ordered by statusDate in descending order, or an empty Optional if no
     * matching entity is found.
     * @throws IllegalArgumentException if employeeId is null
     * @author tminhto
     */
    Optional<EmployeeStatus> findFirstByEmployeeIdOrderByStatusDateDesc(@Param("employeeId") Integer employeeId);

    /**
     * This query is used to check if employee status exist by given conditions
     *
     * @param statusDate a statusDate to check exist
     * @return a boolean to dictate if employee statuses exists by given conditions
     * @author tminhto
     */
    boolean existsByEmployeeIdAndStatusDate(@Param("employeeId") Integer employeeId, 
                                            @Param("statusDate") Date statusDate);

    /**
     * This query is used to insert into employee_status table
     *
     * @param id the employee id to insert to employee_status table
     * @return an inserted EmployeeStatus entity
     * @author loita
     */
    @Query(value = "INSERT INTO public.employee_status( status_date, employee_id, status) VALUES (now(), ?1, 0) RETURNING *;", nativeQuery = true)
    EmployeeStatus createDeleteStatusEmployee(@PathVariable("id") Integer id);

    /**
     * This query is used to insert into employee_status table
     *
     * @param employeeId  the id of employee
     * @param description describe extra description
     * @param status      the status to insert to
     * @param leave_date  the leave date of type Date
     * @return an inserted EmployeeStatus entity
     * @author ngocth
     * @see com.hitachi.coe.fullstack.constant.StatusConstant
     */
    @Query(value = "INSERT INTO public.employee_status( status_date, employee_id, status, description) VALUES (:leave_date, :employeeId, :status, :description) RETURNING *;", nativeQuery = true)
    EmployeeStatus createEmployeeStatus(@PathVariable("employee_id") Integer employeeId,
                                        @PathVariable("description") String description, @PathVariable("status") Integer status,
                                        @PathVariable("leave_date") Date leave_date);
}
