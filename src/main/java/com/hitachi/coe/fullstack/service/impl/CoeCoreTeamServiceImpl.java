package com.hitachi.coe.fullstack.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.hitachi.coe.fullstack.entity.CoeCoreTeam;
import com.hitachi.coe.fullstack.entity.Employee;
import com.hitachi.coe.fullstack.entity.EmployeeStatus;
import com.hitachi.coe.fullstack.entity.CenterOfExcellence;
import com.hitachi.coe.fullstack.exceptions.CoEException;
import com.hitachi.coe.fullstack.model.ICoeCoreTeamSearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.constant.StatusConstant;
import com.hitachi.coe.fullstack.model.CoeCoreTeamModel;
import com.hitachi.coe.fullstack.repository.CenterOfExcellenceRepository;
import com.hitachi.coe.fullstack.repository.CoeCoreTeamRepository;
import com.hitachi.coe.fullstack.repository.EmployeeRepository;
import com.hitachi.coe.fullstack.repository.EmployeeStatusRepository;
import com.hitachi.coe.fullstack.service.CoeCoreTeamService;
import com.hitachi.coe.fullstack.transformation.CoeCoreTeamModelTransformer;
import com.hitachi.coe.fullstack.transformation.CoeCoreTeamTransformer;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CoeCoreTeamServiceImpl implements CoeCoreTeamService {
	private final CoeCoreTeamTransformer coeCoreTeamTransformer;

	private final CoeCoreTeamModelTransformer coeCoreTeamModelTransformer;

	private final CoeCoreTeamRepository coeCoreTeamRepository;

	private final EmployeeRepository employeeRepository;

	private final CenterOfExcellenceRepository centerOfExcellenceRepository;

	private final EmployeeStatusRepository employeeStatusRepository;

    /**
	 * Check center of excellence whether it exists.
	 * 
	 * @param coeId the id of Center of excellence to check.
	 * @return The existing Center of excellence.
     * @throws CoEException if the provided CoE ID doesn't exist in the database.
	 * @author SangBui
	 */
	private CenterOfExcellence checkCenterOfExcellenceExist(Integer coeId) {
		Optional<CenterOfExcellence> centerOfExcellence = centerOfExcellenceRepository.findById(coeId);
		if (!centerOfExcellence.isPresent()) {
			throw new CoEException(ErrorConstant.CODE_CENTER_OF_EXCELLENCE_NOT_FOUND,
					ErrorConstant.MESSAGE_CENTER_OF_EXCELLENCE_NOT_FOUND);
		}
		return centerOfExcellence.get();
	}

    /**
	 * Check Coe Core Team whether it exists.
	 * 
	 * @param coeCoreTeamId the id of Coe Core Team to check.
	 * @return The existing Coe Core Team.
     * @throws CoEException if the provided Coe Core Team ID doesn't exist in the database.
	 * @author SangBui
	 */
	private CoeCoreTeam checkCoeTeamExist(Integer coeCoreTeamId) {
		Optional<CoeCoreTeam> coeCoreTeam = coeCoreTeamRepository.findById(coeCoreTeamId);
		if (!coeCoreTeam.isPresent()) {
			throw new CoEException(ErrorConstant.CODE_COE_TEAM_NOT_FOUND,
					ErrorConstant.MESSAGE_COE_CORE_TEAM_NOT_FOUND);
		}
		return coeCoreTeam.get();
	}

    /**
	 * Check Coe Core Team name whether it already exists.
	 * 
	 * @param teamName the name of Coe Core Team to check.
	 * @return .
     * @throws CoEException if the provided Team name already exists in the database.
	 * @author SangBui
	 */
	private void checkTeamNameExist(String teamName) {
		boolean isTeamNameExisted = coeCoreTeamRepository.existsByNameIgnoreCase(teamName.trim());
		if (isTeamNameExisted) {
			throw new CoEException(ErrorConstant.CODE_COE_CORE_TEAM_NAME_EXISTED,
					ErrorConstant.MESSAGE_COE_CORE_TEAM_NAME_EXISTED);
		}
	}

    /**
	 * Check SubLeader whether that employee is already assigned to any team.
	 * 
	 * @param teamName the ID of subLeader to check.
	 * @return .
     * @throws CoEException if the provided subLeader is already assigned to another team.
	 * @author SangBui
	 */
	private void checkAnyTeamExistsBySubLeaderId(Integer subLeaderId) {
		boolean isAnyTeamExistsBySubLeaderId = coeCoreTeamRepository.existsBySubLeaderId(subLeaderId);
		if (isAnyTeamExistsBySubLeaderId) {
			throw new CoEException(ErrorConstant.CODE_SUBLEADER_UNAVAILABLE,
					ErrorConstant.MESSAGE_SUBLEADER_UNAVAILABLE);
		}
	}

    /**
	 * Check SubLeader whether it exists in the database.
	 * 
	 * @param teamName the ID of subLeader to check.
	 * @return The existing employee.
     * @throws CoEException if the provided subLeader doesn't exist in the database.
	 * @author SangBui
	 */
	private Employee checkSubLeaderExist(Integer subLeaderId) {
		Optional<Employee> empSubLeader = employeeRepository.findById(subLeaderId);
		if (!empSubLeader.isPresent()) {
			throw new CoEException(ErrorConstant.CODE_EMPLOYEE_DO_NOT_EXIST,
					ErrorConstant.MESSAGE_EMPLOYEE_DO_NOT_EXIST);
		}
		return empSubLeader.get();
	}

	/**
	 * Add a new CoE core team.
	 * 
	 * @param coeCoreTeam the model of them coe core team.
	 * @return id of newly created CoE Core Team.
	 * @author PhanNguyen
	 * @modifier SangBui 30/11/2023 Fix bugs
	 */
	@Override
	public Integer createCoeCoreTeam(CoeCoreTeamModel coeCoreTeamModel) {
		// Validate CoeCoreTeamModel
		CoeCoreTeam existingTeamList = coeCoreTeamRepository.findByCode(coeCoreTeamModel.getCode());
		if (!ObjectUtils.isEmpty(existingTeamList)) {
			throw new CoEException(ErrorConstant.CODE_COE_CORE_TEAM_ALREADY_EXISTS,
					ErrorConstant.MESSAGE_COE_CORE_TEAM_ALREADY_EXISTS);
		}

		checkCenterOfExcellenceExist(coeCoreTeamModel.getCenterOfExcellence().getId());
		checkTeamNameExist(coeCoreTeamModel.getName());
		coeCoreTeamModel.setName(coeCoreTeamModel.getName().trim()); // trim spaces in name and update to the model
		coeCoreTeamModel.setStatus(StatusConstant.STATUS_ACTIVE);
		checkSubLeaderExist(coeCoreTeamModel.getSubLeader().getId());
		Optional<EmployeeStatus> lastStatusOptional = employeeStatusRepository
                        .findFirstByEmployeeIdOrderByStatusDateDesc(coeCoreTeamModel.getSubLeader().getId());
		if(lastStatusOptional.get().getStatus() == StatusConstant.STATUS_DELETED) {
			throw new CoEException(ErrorConstant.CODE_SUBLEADER_INACTIVE,
					ErrorConstant.MESSAGE_SUBLEADER_INACTIVE);
		}
		checkAnyTeamExistsBySubLeaderId(coeCoreTeamModel.getSubLeader().getId());
		
		CoeCoreTeam em = coeCoreTeamModelTransformer.apply(coeCoreTeamModel);
		return coeCoreTeamRepository.save(em).getId();
	}

	/**
	 * Add members to a CoE core team.
	 * 
	 * @param coeCoreTeamId the coe core team id to add members to.
	 * @param employeeIds   the list of employee ids will be added.
	 * @return The number of employees have been added.
	 * @author PhanNguyen
	 * @modifier SangBui 30/11/2023 Fix bugs
	 */
	@Override
	public Integer addMembersToCoeCoreTeam(Integer coeCoreTeamId, List<Integer> employeeIds) {
		if (ObjectUtils.isEmpty(employeeIds)) {
			throw new CoEException(ErrorConstant.CODE_EMPLOYEE_LIST_ID_NOT_EMPTY,
					ErrorConstant.MESSAGE_EMPLOYEE_LIST_ID_NOT_EMPTY);
		}
		List<Employee> employeeList = employeeRepository.findAllById(employeeIds);
		if (ObjectUtils.isEmpty(employeeList)) {
			throw new CoEException(ErrorConstant.CODE_EMPLOYEE_NOT_FOUND, ErrorConstant.MESSAGE_EMPLOYEE_NOT_FOUND);
		}

		CoeCoreTeam existingCoeCoreTeam = checkCoeTeamExist(coeCoreTeamId);

		if(existingCoeCoreTeam.getStatus() == StatusConstant.STATUS_DELETED) {
			throw new CoEException(ErrorConstant.CODE_COE_CORE_TEAM_DELETED,
					ErrorConstant.MESSAGE_COE_CORE_TEAM_CODE_DELETED);
		}
		employeeList.forEach(emp -> emp.setCoeCoreTeam(existingCoeCoreTeam));

		return employeeRepository.saveAll(employeeList).size();
	}

	/**
	 * Update related information of a CoE core team.
	 * 
	 * @param coeCoreTeamModel the coe core team id.
	 * @return id of updated CoE Core Team.
	 * @author PhanNguyen
	 * @modifier SangBui 30/11/2023 Fix bugs
	 */
	@Override
	public Integer updateCoeCoreTeam(CoeCoreTeamModel coeCoreTeamModel) {
		// Validate CoeCoreTeamModel
		if (Objects.equals(coeCoreTeamModel.getId(), coeCoreTeamRepository.findByCode("DEF").getId())) {
			throw new CoEException(ErrorConstant.CODE_COE_CORE_TEAM_INVALID,
					ErrorConstant.MESSAGE_COE_CORE_TEAM_INVALID);
		}
		
		CoeCoreTeam existingCoeCoreTeam = checkCoeTeamExist(coeCoreTeamModel.getId());
		CenterOfExcellence centerOfExcellence = checkCenterOfExcellenceExist(
				coeCoreTeamModel.getCenterOfExcellence().getId());

		String existingCoeCoreTeamName = existingCoeCoreTeam.getName();
		String updateCoeCoreTeamName = coeCoreTeamModel.getName().trim();
		Integer existingSubLeaderId = existingCoeCoreTeam.getSubLeaderId();
		Integer updateSubLeaderId = coeCoreTeamModel.getSubLeader().getId();

		// Validate new subLeader
		if (!Objects.equals(existingSubLeaderId, updateSubLeaderId)) {
			// Validate whether new subLeader is existed
			checkSubLeaderExist(updateSubLeaderId);
			// Validate whether new subLeader is assigned to another team
			checkAnyTeamExistsBySubLeaderId(updateSubLeaderId);
			// Validate whether new subLeader is inactive
			Optional<EmployeeStatus> lastStatusOptional = employeeStatusRepository
                        .findFirstByEmployeeIdOrderByStatusDateDesc(updateSubLeaderId);
			if(lastStatusOptional.get().getStatus() == StatusConstant.STATUS_DELETED) {
				throw new CoEException(ErrorConstant.CODE_SUBLEADER_INACTIVE,
						ErrorConstant.MESSAGE_SUBLEADER_INACTIVE);
			}
			existingCoeCoreTeam.setSubLeaderId(updateSubLeaderId);
		}
		// Validate whether new team name is duplicated with other team name
		if (!(existingCoeCoreTeamName.equals(updateCoeCoreTeamName))) {
			checkTeamNameExist(updateCoeCoreTeamName);
			existingCoeCoreTeam.setName(updateCoeCoreTeamName);
		}

		existingCoeCoreTeam.setCenterOfExcellence(centerOfExcellence);

		existingCoeCoreTeam.setStatus(coeCoreTeamModel.getStatus());

		return coeCoreTeamRepository.save(existingCoeCoreTeam).getId();
	}

	/**
	 * Remove members from a CoE core team.
	 * 
	 * @param employeeIds the list of employee ids will be removed.
	 * @return The number of employees have been removed.
	 * @author PhanNguyen
	 * @modifier SangBui 30/11/2023 Fix bugs
	 */
	@Override
	public Integer removeMembersFromCoeCoreTeam(List<Integer> employeeIds) {
		List<Employee> employees = employeeRepository.findAllById(employeeIds);
		if (ObjectUtils.isEmpty(employees)) {
			throw new CoEException(ErrorConstant.CODE_EMPLOYEE_NOT_FOUND, ErrorConstant.MESSAGE_EMPLOYEE_NOT_FOUND);
		}
		CoeCoreTeam defaultCoeCoreTeam = coeCoreTeamRepository.findByCode("DEF");

		employees.forEach(emp -> emp.setCoeCoreTeam(defaultCoeCoreTeam));

		return employeeRepository.saveAll(employees).size();
	}

	/**
	 * Delete a CoE core team.
	 * 
	 * @param deleteId the coe core team id to be deleted.
	 * @return Result of deleting operation.
	 * @author PhanNguyen
	 * @modifier SangBui 30/11/2023 Fix bugs
	 */
	@Override
	public boolean deleteCoeCoreTeam(Integer deleteId) {
		CoeCoreTeam existingCoeCoreTeam = checkCoeTeamExist(deleteId);
		if (existingCoeCoreTeam.getStatus() == StatusConstant.STATUS_ACTIVE) {
			existingCoeCoreTeam.setStatus(StatusConstant.STATUS_DELETED);
			existingCoeCoreTeam.setSubLeaderId(0);	//Set default employee as the subleader
			coeCoreTeamRepository.save(existingCoeCoreTeam);
			List<Employee> employees = employeeRepository.findByCoeCoreTeamId(deleteId);
			CoeCoreTeam defaultCoeCoreTeam = coeCoreTeamRepository.findByCode("DEF");
			employees.forEach(emp -> emp.setCoeCoreTeam(defaultCoeCoreTeam));	//Move all current members to the default team
			return true;
		} else {
			throw new CoEException(ErrorConstant.CODE_COE_CORE_TEAM_STATUS_DUPLICATE,
					ErrorConstant.MESSAGE_COE_CORE_TEAM_STATUS_DUPLICATE);
		}
	}

	/**
	 * Get all coe core teams that belong to a Center of Excellence.
	 * 
	 * @param coeId center of excellence id.
	 * @param status status of team to get.
	 * @return list of CoE core team model that belongs to the given center of
	 *         excellence.
	 * @author PhanNguyen
	 * @modifier SangBui 30/11/2023 Add param
	 */

	@Override
	public List<CoeCoreTeamModel> getAllCoeTeamByCoeIdAndStatus(Integer coeId, Integer status) {
		CenterOfExcellence coe = centerOfExcellenceRepository.getCenterOfExcellencesById(coeId);
		if (Objects.isNull(coe)) {
			return new ArrayList<>();
		}
		return coeCoreTeamTransformer.applyList(coeCoreTeamRepository.getAllByCenterOfExcellenceIdAndStatus(coeId, status));
	}

	/**
	 * Get all coe core teams that exist in the database by status.
	 * 
	 * @return list of CoE core team model
	 * @author PhanNguyen
	 * @modifier SangBui 30/11/2023 Add param
	 */
	@Override
	public List<CoeCoreTeamModel> getAllCoeTeamByStatus(Integer status) {
		List<CoeCoreTeam> existingCoeCoreTeams = coeCoreTeamRepository.findAllByStatus(status);
		if (ObjectUtils.isEmpty(existingCoeCoreTeams)) {
			return new ArrayList<>();
		}
		return coeCoreTeamTransformer.applyList(existingCoeCoreTeams);
	}

	/**
	 * Search a CoE Core team according to its id.
	 * 
	 * @param teamId the id of coe core team to get.
	 * @return CoE Core Team model.
	 * @author SangBui
	 * @modifier SangBui 30/11/2023 Fix bugs
	 */
	@Override
	public CoeCoreTeamModel getCoeCoreTeamByTeamId(Integer teamId) {
		CoeCoreTeam existingCoeCoreTeam = checkCoeTeamExist(teamId);
		return coeCoreTeamTransformer.apply(existingCoeCoreTeam);
	}

	/**
	 * Search for coe core team based on the provided conditions.
	 *
	 * @param keyword the keyword to search for. If null, no keyword filtering will
	 *                be applied.
	 * @param coeId   the id of coe of excellence to filter by. If null, no keyword
	 *                filtering will be applied.
	 * @param no      the page number to be retrieved.
	 * @param limit   the maximum of results to return per page.
	 * @param sortBy  the field to sort the results by.
	 * @param desc    the method to sort (desc or asc).
	 * @return A page of relevant coe core team fields that matches the given
	 *         information.
	 * @author ThuyTrinhThanhLe
	 * @modifier SangBui 30/11/2023 Add param
	 */
	@Override
	public Page<ICoeCoreTeamSearch> searchCoeCoreTeam(String keyword, Integer coeId, Integer status, Integer no, Integer limit,
			String sortBy, Boolean desc) {
		Sort sort = Sort.by(sortBy);
		if (Objects.nonNull(desc)) {
			sort = sort.descending();
		}
		final Pageable pageable = PageRequest.of(no, limit, sort);
		List<ICoeCoreTeamSearch> searchCoeCoreTeam = coeCoreTeamRepository.searchCoeCoreTeam(keyword, coeId, status, pageable);

		return new PageImpl<>(searchCoeCoreTeam, pageable, searchCoeCoreTeam.size());
	}

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
	@Override
	public List<String> validateCoeCoreTeam(String teamName, Integer teamLeaderId, String code) {
		List<String> finalResult = new ArrayList<>();
		if (Objects.nonNull(teamName)) {
			boolean isTeamNameExisted = coeCoreTeamRepository.existsByNameIgnoreCase(teamName.trim());
			if (Boolean.FALSE.equals(isTeamNameExisted)) {
				finalResult.add(null);
			} else {
				finalResult.add(ErrorConstant.MESSAGE_COE_CORE_TEAM_NAME_EXISTED);
			}
		} else {
			finalResult.add(null);
		}

		if (Objects.nonNull(teamLeaderId)) {
			boolean isAnyTeamExistsBySubLeaderId = coeCoreTeamRepository.existsBySubLeaderId(teamLeaderId);
			if (Boolean.FALSE.equals(isAnyTeamExistsBySubLeaderId)) {
				Optional<Employee> empSubLeader = employeeRepository.findById(teamLeaderId);
				if (empSubLeader.isPresent()) {
					finalResult.add(null);
				} else {
					finalResult.add(ErrorConstant.MESSAGE_EMPLOYEE_DO_NOT_EXIST);
				}
			} else {
				finalResult.add(ErrorConstant.MESSAGE_SUBLEADER_UNAVAILABLE);
			}
		} else {
			finalResult.add(null);
		}

		if (Objects.nonNull(code)) {
			boolean isTeamCodeExisted = coeCoreTeamRepository.existsByCodeIgnoreCase(code.trim());
			if (Boolean.FALSE.equals(isTeamCodeExisted)) {
				finalResult.add(null);
			} else {
				finalResult.add(ErrorConstant.MESSAGE_COE_CORE_TEAM_CODE_EXISTED);
			}
		} else {
			finalResult.add(null);
		}

		return finalResult;
	}

}
