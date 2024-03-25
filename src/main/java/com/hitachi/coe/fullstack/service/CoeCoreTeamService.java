package com.hitachi.coe.fullstack.service;

import java.util.List;

import com.hitachi.coe.fullstack.model.CoeCoreTeamModel;
import com.hitachi.coe.fullstack.model.ICoeCoreTeamSearch;
import org.springframework.data.domain.Page;

public interface CoeCoreTeamService {

	/**
	 * Add a new CoE core team.
	 * 
	 * @param coeCoreTeam the model of them coe core team.
	 * @return id of newly created CoE Core Team.
	 * @author PhanNguyen
	 */
	Integer createCoeCoreTeam(CoeCoreTeamModel coeCoreTeam);

	/**
	 * Delete a CoE core team.
	 * 
	 * @param deleteId the coe core team id to be deleted.
	 * @return Result of deleting operation.
	 * @author PhanNguyen
	 */
	boolean deleteCoeCoreTeam(Integer deleteId);

	/**
	 * Add members to a CoE core team.
	 * 
	 * @param coeCoreTeamId the coe core team id to add members to.
	 * @param employeeIds   the list of employee ids will be added.
	 * @return The number of employees have been added.
	 * @author PhanNguyen
	 */
	Integer addMembersToCoeCoreTeam(Integer coeCoreTeamId, List<Integer> employeeIds);

	/**
	 * Update related information of a CoE core team.
	 * 
	 * @param coeCoreTeamModel the coe core team id.
	 * @return id of updated CoE Core Team.
	 * @author PhanNguyen
	 */
	Integer updateCoeCoreTeam(CoeCoreTeamModel coeCoreTeamModel);

	/**
	 * Remove members from a CoE core team.
	 * 
	 * @param employeeIds the list of employee ids will be removed.
	 * @return The number of employees have been removed.
	 * @author PhanNguyen
	 */
	Integer removeMembersFromCoeCoreTeam(List<Integer> employeeIds);

	/**
	 * Get all coe core teams that belong to a Center of Excellence.
	 * 
	 * @param coeId center of excellence id.
	 * @param status status of team to get.
	 * @return list of CoE core team model that belongs to the given center of
	 *         excellence.
	 * @author PhanNguyen
	 * @modifier SangBui 30/11/2023 Add param and change name
	 */
	List<CoeCoreTeamModel> getAllCoeTeamByCoeIdAndStatus(Integer coeId, Integer status);

	/**
	 * Get all coe core teams that exist in the database by status.
	 * 
	 * @return list of CoE core team model
	 * @author PhanNguyen
	 * @modifier SangBui 30/11/2023 Add param and change name
	 */
	List<CoeCoreTeamModel> getAllCoeTeamByStatus(Integer status);

	/**
	 * Search a CoE Core team according to its id.
	 * 
	 * @param teamId the id of coe core team to get.
	 * @return CoE Core Team model.
	 * @author Phan Nguyen
	 */
	CoeCoreTeamModel getCoeCoreTeamByTeamId(Integer teamId);

	/**
	 * Search for coe core team based on the provided conditions.
	 *
	 * @param keyword the keyword to search for. If null, no keyword filtering will
	 *                be applied.
	 * @param coeId   the id of coe of excellence to filter by. If null, no keyword
	 *                filtering will be applied.
	 * @param status the status of teams to get.
	 * @param no      the page number to be retrieved.
	 * @param limit   the maximum of results to return per page.
	 * @param sortBy  the field to sort the results by.
	 * @param desc    the method to sort (desc or asc).
	 * @return A page of relevant coe core team fields that matches the given
	 *         information.
	 * @author ThuyTrinhThanhLe
	 * @modifier SangBui 30/11/2023 Add param
	 */
	Page<ICoeCoreTeamSearch> searchCoeCoreTeam(String keyword, Integer coeId, Integer status, Integer no, Integer limit, String sortBy,
			Boolean desc);

	/**
	 * Validate new coe core team that are requested to be added.
	 * 
	 * @param teamName     The name of the newly added CoE core team.
	 * @param teamLeaderId The id of Team leader will be assigned to the new coe
	 *                     core team.
	 * @param code     The code of the newly added CoE core team.
	 * @return list of result messages related to the validation of team name,
	 *         team leader Id, and team code that are requested to be added.
	 * @author SangBui
	 */
	List<String> validateCoeCoreTeam(String teamName, Integer teamLeaderId, String code);
}
