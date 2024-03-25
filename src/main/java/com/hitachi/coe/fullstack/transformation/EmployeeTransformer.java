package com.hitachi.coe.fullstack.transformation;

import com.hitachi.coe.fullstack.entity.Employee;
import com.hitachi.coe.fullstack.entity.EmployeeLevel;
import com.hitachi.coe.fullstack.model.EmployeeExportModel;
import com.hitachi.coe.fullstack.model.EmployeeModel;
import com.hitachi.coe.fullstack.model.EmployeeSkillModel;
import com.hitachi.coe.fullstack.transformation.base.AbstractCopyPropertiesTransformer;
import com.hitachi.coe.fullstack.transformation.base.EntityToModelTransformer;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class EmployeeTransformer is convert entity to DTO.
 *
 * @author lphanhoangle
 */
@Slf4j
@Component
public class EmployeeTransformer extends AbstractCopyPropertiesTransformer<Employee, EmployeeModel>
		implements EntityToModelTransformer<Employee, EmployeeModel, Integer> {

	@Autowired
	PracticeTransformer practiceTransformer;

	@Autowired
	BranchTransformer branchTransformer;

	@Autowired
	CoeCoreTeamTransformer coeCoreTeamTransformer;

	@Autowired
	BusinessUnitTransformer businessUnitTransformer;

	@Autowired
	EmployeeSkillTransformer employeeSkillTransformer;

	/**
	 * Transformer array entities to array DTO.
	 *
	 * @param entities {@link List} of {@link Employee}
	 * @return {@link List} of {@link EmployeeModel}
	 */
	public List<EmployeeModel> applyList(List<Employee> entities) {
		if (null == entities || entities.isEmpty()) {
			return Collections.emptyList();
		}

		return entities.stream().map(this::apply).collect(Collectors.toList());
	}

	public EmployeeExportModel convertEmployeeToExportModel(Employee employee) {
		return EmployeeExportModel.builder().branch(employee.getBranch().getName())
				.practice(employee.getBusinessUnit().getName())
				.level(employee.getEmployeeLevels().stream().findFirst().orElse(new EmployeeLevel()).getLevel()
						.getName())
				.hccId(employee.getHccId()).email(employee.getEmail()).name(employee.getName()).ldap(employee.getLdap())
				.legalEntityHireDate(employee.getLegalEntityHireDate()).build();
	}

	@Override
	public EmployeeModel apply(Employee employee) {
		if (employee == null) {
			log.debug("EmployeeTransformer::apply -> Employee is null");
			return null;
		}

		EmployeeModel model = new EmployeeModel();
		model.setId(employee.getId());
		model.setHccId(employee.getHccId());
		model.setEmail(employee.getEmail());
		model.setLdap(employee.getLdap());
		model.setName(employee.getName());
		model.setLegalEntityHireDate(employee.getLegalEntityHireDate());
		model.setBranch(employee.getBranch() != null
				? branchTransformer.apply(employee.getBranch())
				: null);
		model.setBusinessUnit(employee.getBusinessUnit() != null
				? businessUnitTransformer.apply(employee.getBusinessUnit())
				: null);
		model.setCoeCoreTeam(employee.getCoeCoreTeam() != null
				? coeCoreTeamTransformer.apply(employee.getCoeCoreTeam())
				: null);
		model.setCreated(employee.getCreated());
		model.setCreatedBy(employee.getCreatedBy());
		model.setUpdated(employee.getUpdated());
		model.setUpdatedBy(employee.getUpdatedBy());
		if (employee.getEmployeeSkills() != null && !employee.getEmployeeSkills().isEmpty()) {
			List<EmployeeSkillModel> empSkillModel = employee.getEmployeeSkills()
					.stream()
					.map(employeeSkillTransformer)
					.collect(Collectors.toList());
			model.setEmployeeSkills(empSkillModel);
		}
		return model;
	}
}
