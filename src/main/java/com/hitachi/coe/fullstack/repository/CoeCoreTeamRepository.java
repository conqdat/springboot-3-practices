package com.hitachi.coe.fullstack.repository;

import com.hitachi.coe.fullstack.entity.CoeCoreTeam;

import java.util.List;

import com.hitachi.coe.fullstack.model.ICoeCoreTeamSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoeCoreTeamRepository extends JpaRepository<CoeCoreTeam, Integer> {
	/**
	 * This query is used to find a Coe core team by its code
	 * 
	 * @param code the unique code used to find the team
	 * @return the coe core team by code
	 * @author SangBui
	 */
	CoeCoreTeam findByCode(String code);

	/**
	 * This query is used to find a Coe core team by its status
	 * 
	 * @param status the status of team used to find the team
	 * @return the list of coe core team by status
	 * @author SangBui
	 */
	List<CoeCoreTeam> findAllByStatus(Integer status);

	/**
	 * This query is used to check whether at least 1 team is exist by subLeaderId
	 * 
	 * @param subLeaderId the id of employee acting as sub leader to check exist
	 * @return a boolean to check exist
	 * @author triminhto
	 */
	boolean existsBySubLeaderId(Integer subLeaderId);

	/**
	 * This query is used to check whether at least 1 team is exist by coe core team
	 * name (ignore case)
	 * 
	 * @param name the name of team to check exist
	 * @return a boolean to check exist
	 * @author SangBui
	 */
	boolean existsByNameIgnoreCase(String name);

	/**
	 * This query is used to check whether at least 1 team is exist by coe core team
	 * code (ignore case)
	 * 
	 * @param code the code of team to check exist
	 * @return a boolean to check exist
	 * @author SangBui
	 */
	boolean existsByCodeIgnoreCase(String code);

	/**
	 * This query is used to get all coe core teams that belong to the same center
	 * of excellence by its id
	 * 
	 * @param coeId the center of excellence id
	 * @param status the status of teams to get
	 * @return list of coe core team by coeId
	 * @author SangBui
	 */
	List<CoeCoreTeam> getAllByCenterOfExcellenceIdAndStatus(Integer coeId, Integer status);

	/**
	 * This query is used to search and return relevant coe core team fields that
	 * match the given information.
	 * 
	 * @param keyword the keyword to search for. If null, no keyword filtering will
	 *                be applied.
	 * @param coeId   the center of excellence id to be searched.
	 * @param status the status of teams to get.
	 * @param page    the pagination information.
	 * @return a page of coe core team that match the selected conditions.
	 * @author ThuyTrinhThanhLe
	 * @modifier SangBui 30/11/2023 Add param and fix bugs
	 */
	@Query(value="SELECT c.id AS id, " +
			"c.code AS code, " +
			"c.name AS name, " +
			"c.coe_id AS coeId, " +
			"c.status AS status, " +
			"q1.employeeName AS leaderName, " +
			"q1.employeeEmail AS leaderEmail, " +
			"coalesce(q2.employeeNo, 0) AS countEmployee " +
			"FROM public.coe_core_team c " +
			"LEFT JOIN public.employee e ON c.sub_leader_id = e.id " +
			"LEFT JOIN (" +
			"SELECT DISTINCT subLeader.id, subLeader.name AS employeeName, subLeader.email AS employeeEmail " +
			"FROM public.coe_core_team c1 " +
			"JOIN public.employee subLeader ON c1.sub_leader_id = subLeader.id " +
			"GROUP BY subLeader.id, subLeader.name, subLeader.email) q1 ON c.sub_leader_id = q1.id " +
			"LEFT JOIN (SELECT c2.id, COUNT(DISTINCT e2) AS employeeNo " +
			"FROM public.coe_core_team c2 " +
			"JOIN public.employee e2 ON c2.id = e2.coe_core_team_id " +
			"GROUP BY c2.id) q2 ON c.id = q2.id " +
			"WHERE ((:keyword IS NULL) " +
			"OR (c.name ILIKE CONCAT('%', :keyword, '%'))  " +
			"OR (e.name ILIKE CONCAT('%', :keyword, '%')) " +
			"OR (e.email ILIKE CONCAT('%', :keyword, '%'))) " +
			"AND ((CAST(CAST(:coeId AS text) AS INTEGER) IS NULL) " +
			"OR (c.coe_id = CAST(CAST(:coeId AS text) AS INTEGER))) " +
			"AND (c.status = CAST(CAST(:status AS text) AS INTEGER)) " +
			"GROUP BY c.id, c.code, c.name, c.coe_id, c.status, q1.employeeName, q1.employeeEmail, q2.employeeNo " +
			"ORDER BY c.created_date DESC", nativeQuery = true )
	List<ICoeCoreTeamSearch> searchCoeCoreTeam(@Param("keyword") String keyword, @Param("coeId") Integer coeId, @Param("status") Integer status, Pageable page);

}
