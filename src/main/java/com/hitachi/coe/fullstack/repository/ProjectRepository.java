package com.hitachi.coe.fullstack.repository;

import com.hitachi.coe.fullstack.entity.Project;
import com.hitachi.coe.fullstack.entity.ProjectStatus;
import com.hitachi.coe.fullstack.model.IProjectModel;
import com.hitachi.coe.fullstack.model.IResultOfCountProject;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query("SELECT e FROM Project e WHERE e.code = :code")
    Project findByCode(String code);
	/**
	 * Filters projects based on the provided conditions.
	 * Project Manager, Business Domain, Status, Start Date, End Date
	 * @author ngocnv
	 * @param keyword         		The keyword to search for. If null, no keyword
	 *                        		filtering will be applied.
	 *
	 * @param fromDate        		The start date for filtering. If null, no filtering by
	 *                        		start date will be applied.
	 *
	 * @param toDate          		The end date for filtering. If null, no filtering by
	 *                        		end date will be applied.
	 *
	 * @param page            		The pagination information.
	 * @exception Throw exception when input invalid time stamp format
	 * @return A page of projects that match the selected conditions.
	 */
	@Query("SELECT p FROM Project p " + "JOIN p.businessDomain bd JOIN p.projectType pt"
			+ " WHERE ((:keyword is null) or (p.code like %:keyword%) or (p.name like %:keyword%))"
			+ " AND ((:bdName is null) or (bd.name like %:bdName%))"
			+ " AND ((:ptName is null) or (pt.name like %:ptName%))"
			+ " AND ((:projectManager is null) or (p.projectManager like %:projectManager%))"
			+ " AND (cast(:status as int) is null or (p.status =:status))"
			+ " AND (cast(:fromDate as timestamp) is null or (p.startDate >= :fromDate))"
			+ " AND (cast(:toDate as timestamp) is null or (p.endDate <= :toDate))"
			)

	Page<Project> filterProjects(@Param("keyword") String keyword, @Param("bdName") String bdName, @Param("ptName") String ptName,
			@Param("projectManager") String projectManager, @Param("status") ProjectStatus status,
			@Param("fromDate") Timestamp fromDate, @Param("toDate") Timestamp toDate, Pageable page);

	/**
	 * Get Detail of project by project id
	 * @author tquangpham
	 * @param id the project id
	 * @return A List of IProjectModel that match the project id.
	 */
	@Query(value = "SELECT p.id AS projectId, p.code, p.name AS projectName, ptype.id AS projectTypeId, "
			+ "p.start_date AS startDate, p.end_date AS endDate, "
			+ "p.project_manager AS projectManager, p.status, bd.id AS businessDomainId, "
			+ "ss.id AS skillSetId, p.description , p.customer_name AS customerName, p.business_unit_id AS businessUnitId "
			+ "FROM project p LEFT JOIN project_tech ptech ON ptech.project_id = p.id "
			+ "LEFT JOIN skill_set ss ON ss.id = ptech.skill_set_id "
			+ "LEFT JOIN project_type ptype ON ptype.id = p.project_type_id "
			+ "LEFT JOIN business_domain bd ON bd.id = p.business_domain_id "
			+ "WHERE p.id = :id", nativeQuery = true)
	List<IProjectModel> getProjectDetailById(@Param("id") Integer id);

	/**
	 * Return a list of newly created project based on buId
	 *
	 * @param buId (optional) 	The ID of the business unit to filter the data (optional).
	 * @return A list of projects that is newly created
	 * @author ThuyTrinhThanhLe
	 */
	@Query("SELECT p FROM Project p WHERE (p.businessUnit.id = :buId OR :buId IS NULL) " +
			"AND (CURRENT_TIMESTAMP - p.startDate <= '30 days') ")
	List<Project> getNewlyCreatedProject(@Param("buId") Integer buId);

	/**
	 * Return total number of newly created project based on buId
	 *
	 * @param buId (optional) 	The ID of the business unit to filter the data (optional).
	 * @return number of projects that is newly created
	 * @author ThuyTrinhThanhLe
	 */
	@Query("SELECT COUNT(*) FROM Project p WHERE (p.businessUnit.id = :buId OR :buId IS NULL) " +
			"AND (CURRENT_TIMESTAMP - p.startDate <= '30 days') ")
	Integer countNewlyCreatedProject(@Param("buId") Integer buId);

	/**
	 * Return a list of expired project based on buId
	 *
	 * @param buId (optional)   The ID of the business unit to filter the data (optional).
	 * @return A list of projects that is expired
	 * @author ThuyTrinhThanhLe
	 */
	@Query("SELECT p FROM Project p WHERE (p.businessUnit.id = :buId OR :buId IS NULL) " +
			"AND p.status = 3")
	List<Project> getExpiredProject(@Param("buId") Integer buId);

	/**
	 * Return total number of expired project based on buId
	 *
	 * @param buId (optional)   The ID of the business unit to filter the data (optional).
	 * @return number of projects that is expired
	 * @author ThuyTrinhThanhLe
	 */
	@Query("SELECT COUNT(*) FROM Project p WHERE (p.businessUnit.id = :buId OR :buId IS NULL) " +
			"AND p.status = 3 ")
	Integer countExpiredProject(@Param("buId") Integer buId);

	/**
	 * Return total number of expired project based on buId
	 *
	 * @param buId   The ID of the business unit to filter the data (optional).
	 * @return number of projects that is expired
	 * @author ThuyTrinhThanhLe
	 */
	@Query(value = "SELECT COUNT(*) FROM project p WHERE p.business_unit_id = :buId " +
			"AND p.status = :status ", nativeQuery = true)
	Integer countProjectByBuIdAndStatus(@Param("buId") Integer buId, @Param("status") Integer status);


	/**
	 * Return total number of project with conditions
	 *
	 * @param buId (optional)  The ID of the team to filter the data (optional).
	 * @return total number of project with conditions
	 * @author ThuyTrinhThanhLe
	 */
	@Query("SELECT DISTINCT bu.id AS buId, bu.name AS buName, " +
			"(SELECT COUNT(*) FROM Project p WHERE p.businessUnit.id = bu.id) AS totalProject, " +
			"(SELECT COUNT(*) FROM Project p JOIN BusinessUnit bu ON p.businessUnit.id = bu.id " +
			"WHERE current_timestamp - p.startDate <= '30 days' AND bu.id = :buId) AS totalNewProject, " +
			"(SELECT COUNT(*) FROM Project p JOIN BusinessUnit bu ON p.businessUnit.id = bu.id " +
			"WHERE p.status = 3 AND bu.id = :buId) AS totalCloseProject " +
			"FROM Project p JOIN BusinessUnit bu ON p.businessUnit.id = bu.id WHERE bu.id = :buId")
	List<IResultOfCountProject> countProjectsForPieChart(@Param("buId") Integer buId);
}
