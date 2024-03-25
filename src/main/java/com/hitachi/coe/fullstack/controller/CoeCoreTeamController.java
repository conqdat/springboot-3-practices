package com.hitachi.coe.fullstack.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.hitachi.coe.fullstack.model.ICoeCoreTeamSearch;
import com.hitachi.coe.fullstack.model.common.BaseResponse;
import com.hitachi.coe.fullstack.model.common.ErrorLineModel;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.hitachi.coe.fullstack.constant.Constants;
import com.hitachi.coe.fullstack.model.CoeCoreTeamModel;
import com.hitachi.coe.fullstack.service.CoeCoreTeamService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/coe-core-team")
@RequiredArgsConstructor
public class CoeCoreTeamController {
	private final CoeCoreTeamService coeCoreTeamService;

	/**
	 * Create new CoE core team
	 *
	 * @param coeCoreTeam model of the CoE core team
	 * @return a ResponseEntity containing the status and the new created CoE core
	 *         team id
	 * @author ThuyTrinhThanhLe
	 * @modifier SangBui 30/11/2023 Change response data
	 */
	@PostMapping("/create")
	@ApiOperation("This api will create a new coe-core-team and return the new created coe-core-team id")
	public ResponseEntity<Object> createCoeCoreTeam(@Validated @RequestBody CoeCoreTeamModel coeCoreTeamModel) {
		Map<String, Object> response = new HashMap<>();
		response.put(Constants.ID, coeCoreTeamService.createCoeCoreTeam(coeCoreTeamModel));
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	/**
	 * Update CoE core team
	 *
	 * @param coeCoreTeamModel model of the CoE core team
	 * @return a ResponseEntity containing the status and the new updated CoE core
	 *         team id
	 * @modifier SangBui 30/11/2023 Change response data
	 */
	@PutMapping("/update")
	@ApiOperation("This api will update coe-core-team and return status and coe-core-team id")
	public ResponseEntity<Object> updateCoeCoreTeam(@Validated @RequestBody CoeCoreTeamModel coeCoreTeamModel) {

		Map<String, Object> response = new HashMap<>();
		response.put(Constants.ID, coeCoreTeamService.updateCoeCoreTeam(coeCoreTeamModel));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Add new member to CoE core team
	 *
	 * @param coeCoreTeamId the id of the CoE core team
	 * @param employeeIds   the list id of employees
	 * @return a ResponseEntity containing the status and the newly created member
	 *         of the CoE core team
	 * @modifier SangBui 30/11/2023 Change response data
	 */
	@PutMapping("/add-members/{coeCoreTeamId}")
	@ApiOperation("This api will add members to a coe-core-team")
	public ResponseEntity<Object> addMembersToCoeCoreTeam(@PathVariable Integer coeCoreTeamId,
			@RequestBody List<Integer> employeeIds) {
		Map<String, Object> response = new HashMap<>();
		response.put(Constants.SIZE, coeCoreTeamService.addMembersToCoeCoreTeam(coeCoreTeamId, employeeIds));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Remove member from CoE core team
	 *
	 * @param employeeIds the list id of employees
	 * @return a ResponseEntity containing the status and the size of the CoE core
	 *         team
	 * @modifier SangBui 30/11/2023 Change response data
	 */
	@PutMapping("/remove-members")
	@ApiOperation("This api will remove members from multiple coe-core-team")
	public ResponseEntity<Object> removeMembersFromCoeCoreTeam(@RequestBody List<Integer> employeeIds) {
		Map<String, Object> response = new HashMap<>();
		response.put(Constants.SIZE, coeCoreTeamService.removeMembersFromCoeCoreTeam(employeeIds));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Delete CoE core team according to teamId
	 *
	 * @param id the id of the deleted team
	 * @return a ResponseEntity containing the status and deleted CoE core team Id
	 * @modifier SangBui 30/11/2023 Change response data
	 */
	@DeleteMapping("/delete/{id}")
	@ApiOperation("This api update coe-core-team will return status and coe-core-team id")
	public ResponseEntity<Object> deleteCoeCoreTeam(@PathVariable Integer id) {
		Map<String, Object> response = new HashMap<>();
		response.put(Constants.RESULT, coeCoreTeamService.deleteCoeCoreTeam(id));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * @param id is center of excellence id from center of excellence on database
	 * @param status team status to get.
	 * 				status = 1: active team (default status)
	 * 				status = 0: inactive team (deleted team + team default)
	 * @return a ResponseEntity containing the status and the list of CoE core team
	 *         by center of excellence id
	 * @author PhanNguyen
	 * @modifier SangBui 30/11/2023 Add param and change method name
	 */
	@GetMapping("/list-coe-team/{coeId}")
	@ApiOperation("This api will return list of coe core team by coe id ")
	public ResponseEntity<List<CoeCoreTeamModel>> getCoeCoreTeamByCoreId(@PathVariable("coeId") Integer id,
			@RequestParam(defaultValue = "1") Integer status) {
		return new ResponseEntity<>(coeCoreTeamService.getAllCoeTeamByCoeIdAndStatus(id, status), HttpStatus.OK);
	}

	/**
	 * @param status team status to get.
	 * 				status = 1: active team (default status)
	 * 				status = 0: inactive team (deleted team + team default)
	 * @return a ResponseEntity containing the status and the status and the list of
	 *         CoE core team
	 * @author PhanNguyen
	 * @modifier SangBui 30/11/2023 Add param and change method name
	 */
	@GetMapping("/list-coe-team")
	@ApiOperation("This api will return list of all coe core teams by status")
	public ResponseEntity<List<CoeCoreTeamModel>> getCoeCoreTeam(
			@RequestParam(defaultValue = "1") Integer status) {
		return new ResponseEntity<>(coeCoreTeamService.getAllCoeTeamByStatus(status), HttpStatus.OK);
	}

	/**
	 * @param teamId The id of CoE core team from coe core team.
	 * @return a BaseReponse containing the status and the list of CoE core team by
	 *         coe team id.
	 * @author ThuyTrinhThanhLe
	 */
	@GetMapping("/search-coe-team/{teamId}")
	@ApiOperation("This api will return list of coe core team by coe team id ")
	public BaseResponse<CoeCoreTeamModel> getCoeCoreTeamByTeamId(@PathVariable(name = "teamId") Integer teamId) {
		return new BaseResponse<>(HttpStatus.OK.value(), null, coeCoreTeamService.getCoeCoreTeamByTeamId(teamId));
	}

	/**
	 * @param keyword the keyword to search for. If null, no keyword filtering will
	 *                be applied.
	 * @param coeId   The id of center of excellence from center of excellence.
	 * @param status the status of teams to get.
	 * 				status = 1: active team (default status)
	 * 				status = 0: inactive team (deleted team + team default)
	 * @param no      the page number to be retrieved.
	 * @param limit   the maximum of results to return per page.
	 * @param sortBy  the field to sort the results by.
	 * @param desc    the method to sort (desc or asc).
	 * @return a BaseReponse containing the status and the list of CoE core team by
	 *         keywords
	 * @author ThuyTrinhThanhLe
	 * @modifier SangBui 30/11/2023 Add param
	 */
	@GetMapping("/search-by-conditions")
	@ApiOperation("This api will return list of coe core team matches with one or many conditions ")
	public BaseResponse<Page<ICoeCoreTeamSearch>> searchCoeCoreTeam(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Integer coeId, @RequestParam(defaultValue = "1") Integer status,
			@RequestParam(defaultValue = "0") Integer no, @RequestParam(defaultValue = "10") Integer limit,
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(required = false) Boolean desc) {
		return new BaseResponse<>(HttpStatus.OK.value(), null,
				coeCoreTeamService.searchCoeCoreTeam(keyword, coeId, status, no, limit, sortBy, desc));
	}

	/**
	 * @param teamName     The name of the newly added CoE core team.
	 * @param teamLeaderId The id of Team leader will be assigned to the new coe
	 *                     core team.
	 * @param code     The code of the newly added CoE core team.
	 * @return a BaseReponse containing the status and the messages relate to the
	 *         validation of team name, team leader Id and team code that are requested to be
	 *         added.
	 * @author SangBui
	 */
	@GetMapping("/validate")
	@ApiOperation("This api will return the result of the validation for new coe core team that are requested to be added")
	public BaseResponse<Object> validateCoeCoreTeam(@RequestParam(required = false) String teamName,
			@RequestParam(required = false) Integer teamLeaderId,
			@RequestParam(required = false) String code) {
		if (Objects.isNull(teamName) && Objects.isNull(teamLeaderId) && Objects.isNull(code)) {
			return BaseResponse.badRequest("Nothing to validate!");
		}
		List<String> result = coeCoreTeamService.validateCoeCoreTeam(teamName, teamLeaderId, code);
		List<ErrorLineModel> errorLineModelList = new ArrayList<>();
		errorLineModelList.add(new ErrorLineModel(Constants.TEAMNAME, result.get(0)));
		errorLineModelList.add(new ErrorLineModel(Constants.TEAMLEADER, result.get(1)));
		errorLineModelList.add(new ErrorLineModel(Constants.TEAMCODE, result.get(2)));
		return new BaseResponse<>(HttpStatus.OK.value(), null, errorLineModelList);
	}
}
