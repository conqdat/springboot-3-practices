package com.hitachi.coe.fullstack.service.impl;

import com.hitachi.coe.fullstack.constant.Constants;
import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.constant.StatusConstant;
import com.hitachi.coe.fullstack.entity.Branch;
import com.hitachi.coe.fullstack.entity.BusinessUnit;
import com.hitachi.coe.fullstack.entity.CoeCoreTeam;
import com.hitachi.coe.fullstack.entity.Employee;
import com.hitachi.coe.fullstack.entity.EmployeeImport;
import com.hitachi.coe.fullstack.entity.EmployeeImportDetail;
import com.hitachi.coe.fullstack.entity.EmployeeOnBench;
import com.hitachi.coe.fullstack.entity.EmployeeOnBenchDetail;
import com.hitachi.coe.fullstack.entity.EmployeeSkill;
import com.hitachi.coe.fullstack.entity.EmployeeStatus;
import com.hitachi.coe.fullstack.entity.Level;
import com.hitachi.coe.fullstack.entity.SkillSet;
import com.hitachi.coe.fullstack.exceptions.CoEException;
import com.hitachi.coe.fullstack.exceptions.InvalidDataException;
import com.hitachi.coe.fullstack.model.BarChartModel;
import com.hitachi.coe.fullstack.model.BranchModel;
import com.hitachi.coe.fullstack.model.BusinessUnitModel;
import com.hitachi.coe.fullstack.model.ChartSkillAndLevelModel;
import com.hitachi.coe.fullstack.model.CoeCoreTeamModel;
import com.hitachi.coe.fullstack.model.CountEmployeeModel;
import com.hitachi.coe.fullstack.model.DataSetBarChart;
import com.hitachi.coe.fullstack.model.DataSetSkillAndLevelChart;
import com.hitachi.coe.fullstack.model.EmployeeImportDetailModel;
import com.hitachi.coe.fullstack.model.EmployeeImportModel;
import com.hitachi.coe.fullstack.model.EmployeeUpdateImportModel;
import com.hitachi.coe.fullstack.model.EmployeeInsertModel;
import com.hitachi.coe.fullstack.model.EmployeeModel;
import com.hitachi.coe.fullstack.model.EmployeeOnBenchDetailModel;
import com.hitachi.coe.fullstack.model.EmployeeOnBenchImportModel;
import com.hitachi.coe.fullstack.model.EmployeeOnBenchModel;
import com.hitachi.coe.fullstack.model.EmployeeSkillModel;
import com.hitachi.coe.fullstack.model.ExcelErrorDetail;
import com.hitachi.coe.fullstack.model.ExcelResponseModel;
import com.hitachi.coe.fullstack.model.IBarChartDepartmentModel;
import com.hitachi.coe.fullstack.model.IEmployeeDetails;
import com.hitachi.coe.fullstack.model.IEmployeeSearchAdvance;
import com.hitachi.coe.fullstack.model.IEmployeeSearchForTeam;
import com.hitachi.coe.fullstack.model.IEmployeeSpecificField;
import com.hitachi.coe.fullstack.model.IPieChartModel;
import com.hitachi.coe.fullstack.model.IResultOfQueryBarChart;
import com.hitachi.coe.fullstack.model.IResultOfQueryCountEmployees;
import com.hitachi.coe.fullstack.model.IResultOfQuerySkillAndLevel;
import com.hitachi.coe.fullstack.model.ImportOperationStatus;
import com.hitachi.coe.fullstack.model.ImportOperationType;
import com.hitachi.coe.fullstack.model.ImportResponse;
import com.hitachi.coe.fullstack.model.SkillLevelDataSet;
import com.hitachi.coe.fullstack.model.SkillSetModel;
import com.hitachi.coe.fullstack.model.common.ErrorLineModel;
import com.hitachi.coe.fullstack.model.common.ErrorModel;
import com.hitachi.coe.fullstack.repository.BranchRepository;
import com.hitachi.coe.fullstack.repository.BusinessUnitRepository;
import com.hitachi.coe.fullstack.repository.CoeCoreTeamRepository;
import com.hitachi.coe.fullstack.repository.EmployeeImportDetailRepository;
import com.hitachi.coe.fullstack.repository.EmployeeImportRepository;
import com.hitachi.coe.fullstack.repository.EmployeeOnBenchDetailRepository;
import com.hitachi.coe.fullstack.repository.EmployeeOnBenchRepository;
import com.hitachi.coe.fullstack.repository.EmployeeRepository;
import com.hitachi.coe.fullstack.repository.EmployeeSkillRepository;
import com.hitachi.coe.fullstack.repository.EmployeeStatusRepository;
import com.hitachi.coe.fullstack.repository.LevelRepository;
import com.hitachi.coe.fullstack.repository.SkillSetRepository;
import com.hitachi.coe.fullstack.service.EmployeeLevelService;
import com.hitachi.coe.fullstack.service.EmployeeService;
import com.hitachi.coe.fullstack.service.EmployeeSkillService;
import com.hitachi.coe.fullstack.transformation.EmployeeImportDetailModelTransformer;
import com.hitachi.coe.fullstack.transformation.EmployeeImportModelTransformer;
import com.hitachi.coe.fullstack.transformation.EmployeeModelTransformer;
import com.hitachi.coe.fullstack.transformation.EmployeeOnBenchDetailModelTransformer;
import com.hitachi.coe.fullstack.transformation.EmployeeOnBenchModelTransformer;
import com.hitachi.coe.fullstack.transformation.EmployeeTransformer;
import com.hitachi.coe.fullstack.util.CommonUtils;
import com.hitachi.coe.fullstack.util.DateUtils;
import com.hitachi.coe.fullstack.util.StringUtil;
import com.hitachi.coe.fullstack.util.ValidateUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.hitachi.coe.fullstack.constant.Constants.YEAR_MONTH_DAY_FORMAT;

/**
 * The Class EmployeeServiceImpl write Logic for employeeService.
 *
 * @author loita
 */
@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

	private final LevelRepository levelRepository;

	private final BranchRepository branchRepository;

	private final EmployeeRepository employeeRepository;

	private final EmployeeTransformer employeeTransformer;

	private final CoeCoreTeamRepository coeCoreTeamRepository;

	private final EmployeeModelTransformer employeeModelTransformer;

	private final EmployeeLevelService employeeLevelService;

	private final EmployeeStatusRepository employeeStatusRepository;

	private final EmployeeSkillRepository employeeSkillRepository;

	private final SkillSetRepository skillSetRepository;

	private final EmployeeSkillService employeeSkillService;

	private final BusinessUnitRepository businessUnitRepository;

	private final EmployeeOnBenchRepository employeeOnBenchRepository;

	private final EmployeeOnBenchDetailRepository employeeOnBenchDetailRepository;

	private final EmployeeImportRepository employeeImportRepository;

	private final EmployeeImportDetailRepository employeeImportDetailRepository;

	private final EmployeeOnBenchModelTransformer employeeOnBenchModelTransformer;

	private final EmployeeOnBenchDetailModelTransformer employeeOnBenchDetailModelTransformer;

	private final EmployeeImportModelTransformer employeeImportModelTransformer;

	private final EmployeeImportDetailModelTransformer employeeImportDetailModelTransformer;

	private final PlatformTransactionManager txManager;

	@Override
	public EmployeeModel deleteEmployeeById(Integer id) {
		Optional<Employee> employeeOption = employeeRepository.findById(id);
		if (!employeeOption.isPresent()) {
			throw new CoEException(ErrorConstant.CODE_EMPLOYEE_NULL, ErrorConstant.MESSAGE_EMPLOYEE_NULL);
		}
		Employee employeeRepo = employeeRepository.deleteEmployeeById(id);
		return employeeTransformer.apply(employeeRepo);
	}

	@Override
	public Page<EmployeeModel> searchEmployees(String keyword, String businessUnitName, String coeCoreTeamName,
											   String branchName,Integer status, String fromDateStr, String toDateStr, Integer no, Integer limit, String sortBy,
											   Boolean desc) {

		// sort by field of employee
		Sort sort = Sort.by(sortBy);
		if (desc != null) {
			sort = sort.descending();
		}
		Date fromDate = CommonUtils.convertStringToDate(fromDateStr, YEAR_MONTH_DAY_FORMAT);
		Date toDate = CommonUtils.convertStringToDate(toDateStr, YEAR_MONTH_DAY_FORMAT);
		// return page of list employees
		Page<Employee> pageEmployee = employeeRepository.filterEmployees(keyword, businessUnitName, coeCoreTeamName,
				branchName, status, fromDate, toDate,
				PageRequest.of(no, limit, sort));
		List<EmployeeModel> employeeModels = pageEmployee.getContent().stream()
				.map(employeeTransformer::apply)
				.collect(Collectors.toList());
		return new PageImpl<>(employeeModels, pageEmployee.getPageable(), pageEmployee.getTotalElements());
	}

	@Override
	public ImportResponse importUpdateEmployee(ExcelResponseModel listOfEmployee) {

		ImportResponse importResponse = new ImportResponse();
		int totalSuccess = 0;
		HashMap<Integer, Object> dataList = listOfEmployee.getData();
		List<ExcelErrorDetail> errorList = listOfEmployee.getErrorDetails();
		List<ErrorModel> errorModelList = ErrorModel.importErrorDetails(errorList);
		List<Branch> branchList = branchRepository.findAll();
		List<BusinessUnit> businessUnitList = businessUnitRepository.findAll();
		List<Level> levelList = levelRepository.findAll();
		List<String> branchesName = branchList.stream().map(Branch::getName).collect(Collectors.toList());
		List<String> businessUnitsName = businessUnitList.stream().map(BusinessUnit::getName).collect(Collectors.toList());
		List<String> levelsName = levelList.stream().map(Level::getName).collect(Collectors.toList());

		for (Map.Entry<Integer, Object> entry : dataList.entrySet()) {
			EmployeeUpdateImportModel em = (EmployeeUpdateImportModel) entry.getValue();
			Employee employee = employeeRepository.findByHccIdAndLdap(em.getHccId(), em.getLdap());

			if (!ObjectUtils.isEmpty(employee) && branchesName.contains(em.getBranch())
					&& businessUnitsName.contains(em.getBusinessUnit()) && levelsName.contains(em.getLevel())) {

				if (!employee.getBranch().getName().equals(em.getBranch())) {
					employee.setBranch(branchList.get(branchesName.indexOf(em.getBranch())));
				}
				if (!employee.getBusinessUnit().getName().equals(em.getBusinessUnit())) {
					employee.setBusinessUnit(businessUnitList.get(businessUnitsName.indexOf(em.getBusinessUnit())));
				}
				totalSuccess++;
				employeeLevelService.saveEmployeeLevel(employee, levelList.get(levelsName.indexOf(em.getLevel())));
				employeeRepository.save(employee);
			} else {
				errorModelList.add(new ErrorModel(entry.getKey(), ErrorLineModel.importEmployeeErrorDetails(employee,
						branchesName, businessUnitsName, levelsName, em.getBranch(), em.getBusinessUnit(), em.getLevel())));
			}
		}
		ErrorModel.sortModelsByLine(errorModelList);
		importResponse.setTotalRows(listOfEmployee.getTotalRows());
		importResponse.setErrorRows(errorModelList.size());
		importResponse.setSuccessRows(totalSuccess);
		importResponse.setErrorList(errorModelList);
		return importResponse;
	}

	@Override
	public EmployeeModel getEmployeeById(Integer employeeId) {

		Optional<Employee> employee = employeeRepository.findById(employeeId);
		if (employee.isEmpty()) {
			throw new InvalidDataException(ErrorConstant.CODE_EMPLOYEE_NOT_FOUND,
					ErrorConstant.MESSAGE_EMPLOYEE_NOT_FOUND);
		}
		EmployeeModel employeeModel = employeeTransformer.apply(employee.get());
		employeeModel.setEmployeeLatestStatus(searchLatestStatus(employee.get()));
		return employeeModel;
	}

	@Override
	public Integer add(EmployeeModel employeeModel) {
		boolean emailSuffixMatched = Constants.EMAIL_HITACHI_SUFFIX_LIST.stream()
				.anyMatch(suffix -> employeeModel.getEmail().trim().endsWith(suffix));
		if (!emailSuffixMatched) {
			throw new CoEException(
					ErrorConstant.CODE_EMAIL_SUFFIX_INVALID,
					ErrorConstant.MESSAGE_EMAIL_SUFFIX_INVALID);
		}
		// Validate HccID and LDAP format is valid
		ValidateUtils.checkStringNumericWithSpecificDigits(employeeModel.getHccId(), true, 8, "hccId");
		ValidateUtils.checkStringNumericWithSpecificDigits(employeeModel.getLdap(), true, 8, "Ldap");

		List<Employee> existingEmployeeList = employeeRepository.findByEmailOrLdapOrHccId(employeeModel.getEmail(),
				employeeModel.getLdap(), employeeModel.getHccId());
		// already exists at least 1 employee by email, ldap or hccid
		if (!existingEmployeeList.isEmpty()) {
			throw new InvalidDataException(
					ErrorConstant.CODE_EMPLOYEE_LDAP_OR_EMAIL_OR_HCCID_EXIST,
					ErrorConstant.MESSAGE_EMPLOYEE_LDAP_OR_EMAIL_OR_HCCID_EXIST);
		}
		branchRepository.findById(employeeModel.getBranch().getId())
				.orElseThrow(() -> new InvalidDataException(ErrorConstant.CODE_BRANCH_NOT_NULL,
						ErrorConstant.MESSAGE_BRANCH_NOT_NULL));

		businessUnitRepository.findById(employeeModel.getBusinessUnit().getId())
				.orElseThrow(() -> new InvalidDataException(ErrorConstant.CODE_BUSINESSUNIT_NULL,
						ErrorConstant.MESSAGE_BUSINESSUNIT_NULL));

		coeCoreTeamRepository.findById(employeeModel.getCoeCoreTeam().getId())
				.orElseThrow(() -> new InvalidDataException(ErrorConstant.CODE_COE_TEAM_NOT_FOUND,
						ErrorConstant.MESSAGE_COE_CORE_TEAM_NOT_FOUND));
		Employee em = employeeModelTransformer.apply(employeeModel);
		if (em == null) {
			throw new InvalidDataException(ErrorConstant.CODE_EMPLOYEE_NULL, ErrorConstant.MESSAGE_EMPLOYEE_NULL);
		}
		EmployeeStatus employeeStatus = new EmployeeStatus();
		employeeStatus.setStatus(employeeModel.getEmployeeLatestStatus());
		em.setEmployeeStatuses(List.of(employeeStatus));
		Employee savedEmployee = employeeRepository.save(em);
		employeeModel.getEmployeeSkills().forEach(
				employeeSkill -> employeeSkillService.addEmployeeSkill(savedEmployee, employeeSkill.getSkillSet()));
		employeeStatus.setEmployee(savedEmployee);
		employeeStatus.setStatusDate(new Timestamp(System.currentTimeMillis()));
		employeeStatusRepository.save(employeeStatus);
		return savedEmployee.getId();
	}

	/**
	 * Non-JavaDoc.
	 * 11/23/2023 - tminhto: Refactor/Rework to fix bugs.
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Integer updateEmployee(EmployeeModel employeeModel) {
		// Validate EmployeeModel
		Optional<EmployeeModel> optEmployeeModel = Optional.ofNullable(employeeModel);
		if (optEmployeeModel.isEmpty()) {
			throw new CoEException(
					ErrorConstant.CODE_EMPLOYEE_MODEL_NULL,
					ErrorConstant.MESSAGE_EMPLOYEE_MODEL_NULL);
		}

		// Validate Employee.
		Optional<Employee> findEmployee = employeeRepository.findById(employeeModel.getId());
		if (findEmployee.isEmpty()) {
			throw new CoEException(
					ErrorConstant.CODE_EMPLOYEE_NOT_FOUND,
					ErrorConstant.MESSAGE_EMPLOYEE_NOT_FOUND);
		}
		if (Objects.equals("999999", findEmployee.get().getHccId())) {
			throw new CoEException(
					ErrorConstant.CODE_DEFAULT_EMPLOYEE_CAN_NOT_CHANGE,
					ErrorConstant.MESSAGE_DEFAULT_EMPLOYEE_CAN_NOT_CHANGE);
		}
		Employee existingEmployee = findEmployee.get();

		// Validate Business Unit
		Optional<BusinessUnitModel> optBusinessUnit = optEmployeeModel.map(EmployeeModel::getBusinessUnit);
		Optional<Integer> optBusinessUnitId = optBusinessUnit.map(BusinessUnitModel::getId);
		if (optBusinessUnit.isEmpty() || optBusinessUnitId.isEmpty()) {
			throw new CoEException(
					ErrorConstant.CODE_BUSINESSUNIT_NULL,
					ErrorConstant.MESSAGE_BUSINESSUNIT_NULL);
		}
		Optional<BusinessUnit> optExistedBusinessUnit = businessUnitRepository.findById(employeeModel.getBusinessUnit().getId());
		if(optExistedBusinessUnit.isEmpty()) {
			throw new CoEException(
					ErrorConstant.CODE_BUSINESS_UNIT_DO_NOT_EXIST,
					ErrorConstant.MESSAGE_BUSINESS_UNIT_DO_NOT_EXIST);
		}
		existingEmployee.setBusinessUnit(optExistedBusinessUnit.get());

		// Validate Branch
		Optional<BranchModel> optBranch = optEmployeeModel.map(EmployeeModel::getBranch);
		Optional<Integer> optBranchId = optBranch.map(BranchModel::getId);
		if (optBranch.isEmpty() || optBranchId.isEmpty()) {
			throw new CoEException(
					ErrorConstant.CODE_BRANCH_NULL,
					ErrorConstant.MESSAGE_BRANCH_NULL);
		}
		Optional<Branch> optExistedBranch = branchRepository.findById(optBranchId.get());
		if (optExistedBranch.isEmpty()) {
			throw new CoEException(
					ErrorConstant.CODE_BRANCH_NOT_FOUND,
					ErrorConstant.MESSAGE_BRANCH_NOT_FOUND);
		}
		existingEmployee.setBranch(optExistedBranch.get());

		// Validate Skill
		if (!ObjectUtils.isEmpty(employeeModel.getEmployeeSkills())) {
			List<EmployeeSkill> updatedEmployeeSkills = new ArrayList<>();
			for (EmployeeSkillModel employeeSkillModel : employeeModel.getEmployeeSkills()) {
				SkillSetModel skillSetModel = Optional.ofNullable(employeeSkillModel.getSkillSet())
						.orElseThrow(() -> new CoEException(
								ErrorConstant.CODE_SKILL_SET_OR_ID_NULL,
								ErrorConstant.MESSAGE_SKILL_SET_OR_ID_NULL));
				if (skillSetModel.getId() == null) {
					throw new CoEException(
							ErrorConstant.CODE_SKILL_SET_OR_ID_NULL,
							ErrorConstant.MESSAGE_SKILL_SET_OR_ID_NULL);
				}
				Optional<SkillSet> optSkillSet = skillSetRepository.findById(skillSetModel.getId());
				if (optSkillSet.isEmpty()) {
					throw new CoEException(
							ErrorConstant.CODE_SKILL_SET_NOT_FOUND,
							ErrorConstant.MESSAGE_SKILL_SET_NOT_FOUND);
				}
				EmployeeSkill employeeSkill = new EmployeeSkill();
				employeeSkill.setEmployee(existingEmployee);
				employeeSkill.setSkillSet(optSkillSet.get());
				employeeSkill.setSkillSetDate(new Timestamp(System.currentTimeMillis()));
				employeeSkill.setSkillLevel(1);
				updatedEmployeeSkills.add(employeeSkill);
			}
			employeeSkillRepository.deleteByEmployeeId(findEmployee.get().getId());
			List<EmployeeSkill> savedEmployeeSkills = employeeSkillRepository.saveAll(updatedEmployeeSkills);
			existingEmployee.setEmployeeSkills(savedEmployeeSkills);
		} else {
			employeeSkillRepository.deleteByEmployeeId(findEmployee.get().getId());
		}

		// Update Employee Information.
		EmployeeStatus employeeStatus = new EmployeeStatus();
		employeeStatus.setEmployee(existingEmployee);
		employeeStatus.setStatusDate(new Date());
		employeeStatus.setStatus(employeeModel.getEmployeeLatestStatus());
		employeeStatusRepository.save(employeeStatus);
		existingEmployee.setName(employeeModel.getName().trim());
		return employeeRepository.save(existingEmployee).getId();
	}

	/**
	 *
	 * Get the percentage values for the skill bar chart for a specific branch,
	 * group, and team.
	 *
	 * @param branchId the ID of the branch to get the data
	 * @param groupId  the ID of the group within the branch to retrieve the data
	 * @param teamId   the ID of the team within the group to retrieve the data
	 * @param skillIds the list Id of the skill to filter the data (optional).
	 * @return a list of Object arrays representing the percentage values for the
	 *         skill bar chart
	 */
	@Override
	public BarChartModel getQuantityOfEachSkillForBarChart(Integer branchId, Integer groupId, Integer teamId,
														   List<Integer> skillIds) {
		if (groupId == null && teamId != null) {
			throw new CoEException(ErrorConstant.CODE_CENTER_OF_EXCELLENCE_NULL,
					ErrorConstant.MESSAGE_CENTER_OF_EXCELLENCE_NULL);
		}

		String listId = CommonUtils.convertListIntegertoString(skillIds);
		List<IResultOfQueryBarChart> results = employeeRepository.getQuantityOfEachSkillForBarChart(branchId, groupId,
				teamId, listId);

		BarChartModel barChartModel = new BarChartModel();
		List<String> labelSkills = new ArrayList<>();
		List<DataSetBarChart> datasets = new ArrayList<>();
		List<String> labels = new ArrayList<>();

		for (IResultOfQueryBarChart result : results) {
			String skillName = result.getFieldName();
			String label = result.getLabel();

			if (!labelSkills.contains(skillName)) {
				labelSkills.add(skillName);
			}

			if (!labels.contains(label)) {
				labels.add(label);
			}
		}

		for (String label : labels) {
			DataSetBarChart dataSetBarChart = new DataSetBarChart();
			dataSetBarChart.setLabel(label);
			List<Long> data = new ArrayList<>();

			for (String skill : labelSkills) {
				boolean skillExists = false;
				for (IResultOfQueryBarChart result : results) {
					if (result.getLabel().equals(label) && result.getFieldName().equals(skill)) {
						data.add(result.getTotal());
						skillExists = true;
						break;
					}
				}

				if (!skillExists) {
					data.add(0L);
				}
			}

			dataSetBarChart.setData(data);
			datasets.add(dataSetBarChart);
		}

		barChartModel.setLabels(labelSkills);
		barChartModel.setDatasets(datasets);
		return barChartModel;
	}

	/**
	 * Retrieves the quantity of levels for employees based on the provided branch,
	 * group, and team IDs.
	 *
	 * @param branchId The ID of the branch. Pass null to retrieve data for all
	 *                 branches.
	 * @param groupId  The ID of the group. Pass null to retrieve data for all
	 *                 groups.
	 * @param teamId   The ID of the team. Pass null to retrieve data for all teams.
	 * @return The BarChartModel representing the quantity of levels for employees.
	 * @throws CoEException if the provided team ID is not null and the group ID is
	 *                      null, indicating an invalid combination.
	 */
	@Override
	public BarChartModel getQuantityOfLevelForBarChart(Integer branchId, Integer groupId, Integer teamId) {
		if (groupId == null && teamId != null) {
			throw new CoEException(ErrorConstant.CODE_CENTER_OF_EXCELLENCE_NULL,
					ErrorConstant.MESSAGE_CENTER_OF_EXCELLENCE_NULL);
		}
		List<IResultOfQueryBarChart> results = employeeRepository.getQuantityOfLevelForBarChart(branchId, groupId,
				teamId);
		BarChartModel barChartModel = new BarChartModel();
		List<String> labelLevels = new ArrayList<>();
		List<DataSetBarChart> datasets = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		for (IResultOfQueryBarChart result : results) {
			String levelName = result.getFieldName();
			String label = result.getLabel();
			if (!labelLevels.contains(levelName)) {
				labelLevels.add(levelName);
			}
			if (!labels.contains(label)) {
				labels.add(label);
			}
		}
		for (String label : labels) {
			DataSetBarChart dataSetBarChart = new DataSetBarChart();
			dataSetBarChart.setLabel(label);
			List<Long> data = new ArrayList<>();
			for (String level : labelLevels) {
				boolean levelExists = false;
				for (IResultOfQueryBarChart result : results) {
					if (result.getLabel().equals(label) && result.getFieldName().equals(level)) {
						data.add(result.getTotal());
						levelExists = true;
						break;
					}
				}
				if (!levelExists) {
					data.add(0L);
				}
			}
			dataSetBarChart.setData(data);
			datasets.add(dataSetBarChart);

		}
		barChartModel.setLabels(labelLevels);
		barChartModel.setDatasets(datasets);
		return barChartModel;
	}

	/**
	 * Retrieves the quantity of employees in each department at a certain level
	 * within an organization.
	 *
	 * @author hchantran
	 * @param branchId (optional) The ID of the branch to filter by.
	 * @param groupId  (optional) The ID of the group to filter by.
	 * @param teamId   (optional) The ID of the team to filter by.
	 * @return A data contains the quantity of employees in each department at a
	 *         certain level.
	 */
	@Override
	public List<IBarChartDepartmentModel> getQuantityEmployeeOfLevel(Integer branchId, Integer groupId,
																	 Integer teamId) {
		return employeeRepository.getQuantityEmployeeOfLevel(branchId, groupId, teamId);
	}

	/**
	 * Gets data for a pie chart on the level of employees in a CoE team.
	 *
	 * @param branchId      The ID of the branch.
	 * @param coeCoreTeamId The ID of the CoE team.
	 * @param coeId        The ID of the CoE.
	 * @return Data for a pie chart.
	 */
	@Override
	public List<IPieChartModel> getLevelPieChart(Integer branchId, Integer coeCoreTeamId, Integer coeId) {
		List<IPieChartModel> pieChartModels = levelRepository.piechartlevel(branchId, coeCoreTeamId, coeId);
		if(pieChartModels.isEmpty()) {
			throw new InvalidDataException(ErrorConstant.CODE_DATA_IS_EMPTY,
					ErrorConstant.MESSAGE_DATA_IS_EMPTY);
		}
		return pieChartModels;

	}

	@Override
	public ImportResponse importInsertEmployee(ExcelResponseModel listOfEmployeeInsert) {
		ImportResponse importResponse = new ImportResponse();
		int totalSuccess = 0;
		HashMap<Integer, Object> dataList = listOfEmployeeInsert.getData();
		List<ExcelErrorDetail> errorList = listOfEmployeeInsert.getErrorDetails();
		List<ErrorModel> errorModelList = ErrorModel.importErrorDetails(errorList);
		List<Branch> branchList = branchRepository.findAll();
		List<BusinessUnit> businessUnitList = businessUnitRepository.findAll();
		List<Level> levelList = levelRepository.findAll();
		List<CoeCoreTeam> teamList = coeCoreTeamRepository.findAll();
		List<String> branchesName = branchList.stream().map(Branch::getName).collect(Collectors.toList());
		List<String> businessUnitsName = businessUnitList.stream().map(BusinessUnit::getName).collect(Collectors.toList());
		List<String> levelsName = levelList.stream().map(Level::getName).collect(Collectors.toList());
		List<String> teamsName = teamList.stream().map(CoeCoreTeam::getName).collect(Collectors.toList());


		for (Map.Entry<Integer, Object> entry : dataList.entrySet()) {
			EmployeeInsertModel em = (EmployeeInsertModel) entry.getValue();
			List<Employee> employeeList = employeeRepository.findByEmailOrLdapOrHccId(em.getEmail(), em.getLdap(), em.getHccId());
			EmployeeStatus employeeStatus = new EmployeeStatus();
			employeeStatus.setStatus(1);
			if (ObjectUtils.isEmpty(employeeList) && branchesName.contains(em.getLocation())
					&& businessUnitsName.contains(em.getBusinessUnit()) && levelsName.contains(em.getLevel()) && teamsName.contains(em.getTeam())) {
				Employee employeeInsert = new Employee();
				employeeInsert.setLdap(em.getLdap());
				employeeInsert.setHccId(em.getHccId());
				employeeInsert.setName(em.getEmployeeName());
				employeeInsert.setLegalEntityHireDate(em.getLegalEntityHireDate());
				employeeInsert.setEmail(em.getEmail());
				employeeInsert.setBusinessUnit(businessUnitList.get(businessUnitsName.indexOf(em.getBusinessUnit())));
				employeeInsert.setBranch(branchList.get(branchesName.indexOf(em.getLocation())));
				employeeInsert.setCoeCoreTeam(teamList.get(teamsName.indexOf(em.getTeam())));
				employeeInsert.setCreated(new Date());
				employeeInsert.setCreatedBy("admin");

				employeeInsert.setEmployeeStatuses(List.of(employeeStatus));
				employeeStatus.setEmployee(employeeInsert);
				employeeStatus.setStatusDate(new Timestamp(System.currentTimeMillis()));

				employeeRepository.save(employeeInsert);
				employeeStatusRepository.save(employeeStatus);
				employeeLevelService.saveEmployeeLevel(employeeInsert, levelList.get(levelsName.indexOf(em.getLevel())));

				totalSuccess++;

			} else {
				errorModelList.add(new ErrorModel(entry.getKey(), ErrorLineModel.importInsertEmployeeErrorDetails(employeeList,
						branchesName, businessUnitsName, levelsName, teamsName, em.getLocation(), em.getBusinessUnit(), em.getLevel(), em.getTeam())));
			}
		}
		ErrorModel.sortModelsByLine(errorModelList);
		importResponse.setTotalRows(listOfEmployeeInsert.getTotalRows());
		importResponse.setErrorRows(errorModelList.size());
		importResponse.setSuccessRows(totalSuccess);
		importResponse.setErrorList(errorModelList);
		return importResponse;
	}

	@Override
	public IEmployeeDetails getEmployeeDetailsByHccId(String hccId) {
		// Check if hccId is null or empty
		if (ObjectUtils.isEmpty(hccId)) {
			throw new CoEException(ErrorConstant.CODE_HCC_ID_REQUIRED, ErrorConstant.MESSAGE_HCC_ID_REQUIRED);
		}
		// Check if employee exist by hccId
		if (ObjectUtils.isEmpty(employeeRepository.findByHccId(hccId))) {
			throw new CoEException(ErrorConstant.CODE_EMPLOYEE_NOT_FOUND, ErrorConstant.MESSAGE_EMPLOYEE_NOT_FOUND);
		}
		return employeeRepository.getEmployeeDetailsByHccId(hccId);
	}

	/**
	 *
	 * @param employee employee entity
	 * @return latest status of this employee
	 */
	private static int searchLatestStatus(Employee employee) {
		List<EmployeeStatus> employeeStatuses = employee.getEmployeeStatuses();
		Date maxDateStatus = employeeStatuses.stream().map(EmployeeStatus::getStatusDate).max(Date::compareTo).orElseThrow(RuntimeException::new);
		EmployeeStatus employeeLatestStatus = employeeStatuses.stream()
				.filter(employeeStatus -> employeeStatus.getStatusDate().equals(maxDateStatus)).findFirst().orElse(new EmployeeStatus());
		return employeeLatestStatus.getStatus();
	}

	/**
	 * Searches for employees based on the provided conditions.
	 *
	 * @param keyword         	The keyword to search for. If null, no keyword
	 *                        	filtering will be applied.
	 * @param businessUnitId    The businessUnit id to filter by. If null, no filtering
	 *                        	by businessUnit name will be applied.
	 * @param coeCoreTeamId 	the CoeCoreTeam id to filter by. If null, no filtering
	 *                          by CoeCoreTeam name will be applied.
	 * @param workingStatus 	The working status of employee
	 * @param no              	The page number to retrieve.
	 * @param limit           	The maximum employee of results to return per page.
	 * @param sortBy          	The field to sort the results by.
	 * @param desc            	The field to sort desc or asc the results.
	 * @return 					A page of employee models that match the selected conditions.
	 * @author 					tquangpham
	 */
	@Override
	public Page<IEmployeeSearchAdvance> searchEmployeesAdvance(
			String keyword,
			Integer businessUnitId,
			Integer coeCoreTeamId,
			Integer branchId,
			String workingStatus,
			Integer status,
			LocalDate fromDate,
			LocalDate toDate,
			Integer no,
			Integer limit,
			String sortBy,
			Boolean desc) {
		String searchKeyword = "%";
		List<String> symbolsToRemove = Arrays.asList("%", "`", "\"");
		try {
			if (keyword != null) {
				Integer.parseInt(keyword);
        		searchKeyword = StringUtil.removeSymbols(keyword, symbolsToRemove);
			}
		} catch (NumberFormatException nfe) {
			searchKeyword = StringUtil.combineString(StringUtil.removeSymbols(keyword, symbolsToRemove), "%");
		}
		Sort sort = Sort.by(sortBy);
		if (desc != null) {
			sort = sort.descending();
		}
		final Pageable pageable = PageRequest.of(no, limit, sort);
		final Page<IEmployeeSearchAdvance> pageResult = employeeRepository.searchEmployeeAdvance(
				searchKeyword,
				businessUnitId,
				coeCoreTeamId,
				branchId,
				status, 
				fromDate,
				toDate,
				pageable);
		List<IEmployeeSearchAdvance> pageResultListContent = pageResult.getContent();
		if (!ObjectUtils.isEmpty(workingStatus)) {
			pageResultListContent = pageResultListContent.stream()
					.filter(emp -> emp.getEmployeeWorkingStatus().equalsIgnoreCase(workingStatus))
					.collect(Collectors.toList());
		}
		return new PageImpl<>(pageResultListContent, pageable, pageResult.getTotalElements());
	}

	/**
	 *
	 * Get the quntity of employees that have a specific skill and the count of each level for that skill filtered by a specific branch,
	 * or coe.
	 *
	 * @param branchId the ID of the branch to get the data (optional)
	 * @param coeId  the ID of the Coe to retrieve the data (optional)
	 * @param skillIds the list Id of the skill to filter the data .
	 * @return a list of skill name and datasets of each skill, including total count, level count and BU name
	 * @author SangBui        
	 */
	@Override
	public ChartSkillAndLevelModel getQuantityOfEmployeesForEachSkill(Integer branchId, Integer coeId, 
			List<Integer> skillIds) {

		String listId = CommonUtils.convertListIntegertoString(skillIds);
		List<IResultOfQuerySkillAndLevel> results = employeeRepository.getQuantityOfEmployeesForEachSkill(branchId, coeId,
				listId);

		ChartSkillAndLevelModel chartSkillAndLevelModel = new ChartSkillAndLevelModel();
		List<String> labelSkills = new ArrayList<>();
		List<DataSetSkillAndLevelChart> datasets = new ArrayList<>();
		List<String> labels = new ArrayList<>();

		for (IResultOfQuerySkillAndLevel result : results) {
			String skillName = result.getSkillName();
			String label = result.getLabel();

			if (!labelSkills.contains(skillName)) {
				labelSkills.add(skillName);
			}

			if (!labels.contains(label)) {
				labels.add(label);
			}
		}

		for (String label : labels) {
			DataSetSkillAndLevelChart dataSkillAndLevelChart = new DataSetSkillAndLevelChart();
			dataSkillAndLevelChart.setLabel(label);
			List<SkillLevelDataSet> skillLevelDataSets = new ArrayList<>();

			for (String skill : labelSkills) {
				boolean skillExists = false;
				for (IResultOfQuerySkillAndLevel result : results) {
					if (result.getLabel().equals(label) && result.getSkillName().equals(skill)) {
						SkillLevelDataSet skillLevelDataSet = new SkillLevelDataSet();
						List<Long> levelCnt = new ArrayList<>();
						levelCnt.add(result.getLevel1());
						levelCnt.add(result.getLevel2());
						levelCnt.add(result.getLevel3());
						levelCnt.add(result.getLevel4());
						levelCnt.add(result.getLevel5());
						skillLevelDataSet.setLevelCnt(levelCnt);
						skillLevelDataSet.setTotal(result.getTotal());
						skillLevelDataSets.add(skillLevelDataSet);
						skillExists = true;
						break;
					}
				}

				if (!skillExists) {
					skillLevelDataSets.add(new SkillLevelDataSet());
				}
			}

			dataSkillAndLevelChart.setDatasets(skillLevelDataSets);
			dataSkillAndLevelChart.setLabel(label);
			datasets.add(dataSkillAndLevelChart);
		}

		chartSkillAndLevelModel.setSkills(labelSkills);
		chartSkillAndLevelModel.setDatasets(datasets);
		return chartSkillAndLevelModel;
	}

	/**
	 * Search employees based on teamId.
	 * @param teamId the id of team in coe core team to filter by. If null, no keyword filtering will be applied.
	 * @return employee object that match the given info.
	 * @author ThuyTrinhThanhLe
	 */
	@Override
	public List<EmployeeModel> getEmployeesByTeamId(Integer teamId) {
		Optional<CoeCoreTeam> team = coeCoreTeamRepository.findById(teamId);
		if (team.isEmpty()) {
			return new ArrayList<>();
		}
		return employeeTransformer.applyList(employeeRepository.findByCoeCoreTeamId(teamId));
	}

	/**
	 * Search employees infos based on teamId.
	 * @param teamId the id of team in coe core team to filter by. If null, no keyword filtering will be applied.
	 * @return list of employees specific infos that match the given info.
	 * @author ThuyTrinhThanhLe
	 */
	@Override
	public List<IEmployeeSpecificField> getEmployeeSpecificFieldByTeam(Integer teamId) {
		if (ObjectUtils.isEmpty(teamId)) {
			throw new CoEException(ErrorConstant.CODE_COE_CORE_TEAM_ID_REQUIRED, ErrorConstant.MESSAGE_COE_CORE_TEAM_ID_REQUIRED);
		}
		return employeeRepository.getEmployeeSpecificFieldByTeam(teamId);
	}

	/**
	 *
	 * Get the count of total employees, employees on projects, employees on bench and new employees and calculate
	 * their difference from the previous month in percentage
	 *
	 * @return a list of count of employees in each category and their different percentages
	 * @author SangBui    
	 */
	@Override
	public CountEmployeeModel getCountOfEmployeesForCurrentMonth() {
		CountEmployeeModel countEmployeeModel = new CountEmployeeModel();
		List<IResultOfQueryCountEmployees> results = employeeRepository.getCountOfEmployeesCurrentAndLastMonth();
		IResultOfQueryCountEmployees currentMonthResult = results.get(0);
		IResultOfQueryCountEmployees lastMonthResult = results.get(1);
		float diffTotal = CommonUtils.calculatePercentage(currentMonthResult.getTotalEmployees(), lastMonthResult.getTotalEmployees());
		float diffOnProject = CommonUtils.calculatePercentage(currentMonthResult.getOnProjectEmployees(), lastMonthResult.getOnProjectEmployees());
		float diffOnBench = CommonUtils.calculatePercentage(currentMonthResult.getOnBenchEmployees(),lastMonthResult.getOnBenchEmployees());
		
		float diffNew = (float)(currentMonthResult.getNewEmployees()*100)/(currentMonthResult.getTotalEmployees()-currentMonthResult.getNewEmployees());
		DecimalFormat df = new DecimalFormat("#.#");
		String formattedFloatStr = df.format(diffNew);
		
		countEmployeeModel.setTotalEmployees(currentMonthResult.getTotalEmployees());
		countEmployeeModel.setDiffTotal(diffTotal);
		countEmployeeModel.setOnProjectEmployees(currentMonthResult.getOnProjectEmployees());
		countEmployeeModel.setDiffOnProject(diffOnProject);
		countEmployeeModel.setOnBenchEmployees(currentMonthResult.getOnBenchEmployees());
		countEmployeeModel.setDiffOnBench(diffOnBench);
		countEmployeeModel.setNewEmployees(currentMonthResult.getNewEmployees());
		countEmployeeModel.setDiffNew(Float.parseFloat(formattedFloatStr));
		return countEmployeeModel; 
	}

	@Override
	public ImportResponse importOnBenchEmployee(ExcelResponseModel listOfEmployee, MultipartFile file) {
		final ImportResponse importResponse = new ImportResponse();
		final HashMap<Integer, Object> dataMap = listOfEmployee.getData();
		final List<ExcelErrorDetail> errorList = listOfEmployee.getErrorDetails();
		final List<ErrorModel> errorModelList = ErrorModel.importErrorDetails(errorList);
		final Map<Integer, EmployeeOnBenchModel> onBenchModelMap = new HashMap<>();
		final Map<Integer, EmployeeOnBenchDetailModel> onBenchDetailModelMap = new HashMap<>();
		final Map<Integer, EmployeeImportDetailModel> importDetailModelMap = new HashMap<>();
		final EmployeeImportModel importModel = new EmployeeImportModel();
		importModel.setType(ImportOperationType.EMPLOYEE_ON_BENCH);
		importModel.setStatus(ImportOperationStatus.valueOf(listOfEmployee.getStatus().name()));
		importModel.setMessage(listOfEmployee.getErrorDetails());
		final String JINZAIID_STAFFID = "JinzaiID/StaffID";
		// Using AtomicInteger instead of int to facilitate passing the value by
		// reference-like behavior and future multi-thread using if applicable.
		AtomicInteger totalSuccess = new AtomicInteger(0);

		// Ex: "ABCDEFG - 11Nov2020.xlsx" -> "11Nov2020"
		final String fileName = Optional.ofNullable(file.getOriginalFilename()).orElse("");
		String name = fileName;
		importModel.setName(fileName);
		if (Objects.nonNull(fileName)) {
			int hyphenIndex = fileName.lastIndexOf('-');
			if (hyphenIndex != -1) {
				name = CommonUtils.removeFileExtension(fileName.substring(hyphenIndex + 1).trim(), true);
			}
		}
		for (final Map.Entry<Integer, Object> entry : dataMap.entrySet()) {
			final Integer key = entry.getKey();
			final Object value = entry.getValue();
			final Optional<EmployeeOnBenchImportModel> empOpt = Optional.ofNullable((EmployeeOnBenchImportModel) value);
			final EmployeeImportDetailModel importDetailModel = new EmployeeImportDetailModel();
			importDetailModel.setLineNum(key);
			importDetailModel.setStatus(ImportOperationStatus.FAILED);
			importDetailModelMap.put(key, importDetailModel);
			// validating
			if (empOpt.isEmpty()) {
				final List<ErrorLineModel> tmpList = ErrorLineModel.createErrorDetails(JINZAIID_STAFFID,
				ErrorConstant.MESSAGE_DATA_IS_EMPTY);
				errorModelList.add(new ErrorModel(entry.getKey(), tmpList));
				importDetailModel.setMesssageLineList(tmpList);
				continue;
			}
			importDetailModel.setBody(empOpt.get());
			// first we find by jinzaiid if not found then find by staffid, if not found then continue
			final String jinzaiId = empOpt.get().getJinzaiId();
			final String staffId = empOpt.get().getStaffId();
			Optional<Employee> findEmpOpt = employeeRepository.findByLdapOrHccId(jinzaiId, jinzaiId);
			if (findEmpOpt.isEmpty()) {
				findEmpOpt = employeeRepository.findByLdapOrHccId(staffId, staffId);
			}
			if (findEmpOpt.isEmpty()) {
				final List<ErrorLineModel> tmpList = ErrorLineModel.createErrorDetails(JINZAIID_STAFFID,
						ErrorConstant.MESSAGE_EMPLOYEE_DO_NOT_EXIST);
				errorModelList.add(new ErrorModel(entry.getKey(), tmpList));
				importDetailModel.setMesssageLineList(tmpList);
				continue;
			}

			final Date startDate = DateUtils.getStartOfMonth();
			final Date endDate = DateUtils.getEndOfMonth();

			final EmployeeOnBenchModel onBenchModel = EmployeeOnBenchModel.builder()
					.name(name)
					.startDate(startDate)
					.endDate(endDate)
					.build();
			final Integer benchDays = empOpt.map(EmployeeOnBenchImportModel::getBenchDays)
					.filter(org.apache.commons.lang3.StringUtils::isNumeric)
					.map(Integer::parseInt)
					.orElse(null);
			final EmployeeOnBenchDetailModel onBenchDetailModel = EmployeeOnBenchDetailModel.builder()
					.benchDays(benchDays)
					.dateOfJoin(empOpt.map(EmployeeOnBenchImportModel::getDateOfJoin).orElse(null))
					.statusChangeDate(empOpt.map(EmployeeOnBenchImportModel::getStatusChangeDate).orElse(null))
					.categoryCode(empOpt.map(EmployeeOnBenchImportModel::getCategoryCode).orElse(null))
					.employeeId(findEmpOpt.map(Employee::getId).orElse(null))
					.employeeOnBenchModel(onBenchModel)
					.build();
			importDetailModel.setStatus(ImportOperationStatus.SUCCESS);
			onBenchModelMap.put(key, onBenchModel);
			onBenchDetailModelMap.put(key, onBenchDetailModel);
			importDetailModelMap.put(key, importDetailModel);
			totalSuccess.incrementAndGet();
		}
		// starting save Tx to database, I use PlatformTransactionManager to manually
		// control transaction individually, cuz I want to roll back all if only 1
		// exception occur, @Transactional top of the class behave weirdly in this case
		processEmployeeOnBenchTx(onBenchModelMap, onBenchDetailModelMap, importDetailModelMap, errorModelList,
				JINZAIID_STAFFID, totalSuccess);
		processEmployeeImportTx(importModel, importDetailModelMap);

		ErrorModel.sortModelsByLine(errorModelList);
		importResponse.setTotalRows(listOfEmployee.getTotalRows());
		importResponse.setErrorRows(errorModelList.size());
		importResponse.setSuccessRows(totalSuccess.get());
		importResponse.setErrorList(errorModelList);
		return importResponse;
	}

	/**
	 * Saves a map of {@link EmployeeOnBenchModel} and {@link EmployeeOnBenchDetailModel} to the
	 * database using a new transaction.
	 * If any exception occurs during the save operation, the transaction is rolled
	 * back and the error details are added to the errorModelList and the
	 * importDetailModelMap.
	 * The total of successful saves is tracked using the provided {@link AtomicInteger} totalSuccess.
	 * 
	 * @param onBenchModelMap       a map of {@link EmployeeOnBenchModel} objects, keyed by
	 *                              the row number
	 * @param onBenchDetailModelMap a map of {@link EmployeeOnBenchDetailModel} objects,
	 *                              keyed by the row number
	 * @param importDetailModelMap  a map of {@link EmployeeImportDetailModel} objects,
	 *                              keyed by the row number
	 * @param errorModelList        a list of {@link ErrorModel} objects to store the error
	 *                              details
	 * @param JINZAIID_STAFFID      a constant {@link String} representing the column name
	 *                              for JINZAIID_STAFFID
	 * @param totalSuccess          an {@link AtomicInteger} value representing the initial number of
	 *                              successful saves
	 * @author tminhto
	 */
	private void processEmployeeOnBenchTx(
			final Map<Integer, EmployeeOnBenchModel> onBenchModelMap,
			final Map<Integer, EmployeeOnBenchDetailModel> onBenchDetailModelMap,
			final Map<Integer, EmployeeImportDetailModel> importDetailModelMap,
			final List<ErrorModel> errorModelList, final String JINZAIID_STAFFID,
			AtomicInteger totalSuccess) {
		// init transaction
		final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("processEmployeeOnBenchTx");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		final TransactionStatus status = txManager.getTransaction(def);
		boolean isAtLeastOneErr = false;
		for (final Map.Entry<Integer, EmployeeOnBenchModel> entry : onBenchModelMap.entrySet()) {
			try {
				final EmployeeOnBenchModel onBenchModel = entry.getValue();
				final EmployeeOnBenchDetailModel onBenchDetailModel = onBenchDetailModelMap.get(entry.getKey());

				final EmployeeOnBench onBench = employeeOnBenchModelTransformer.apply(onBenchModel);
				final EmployeeOnBenchDetail onBenchDetail = employeeOnBenchDetailModelTransformer
						.apply(onBenchDetailModel);
				final EmployeeOnBench saved = employeeOnBenchRepository.save(onBench);
				onBenchDetail.setEmployeeOnBench(saved);
				employeeOnBenchDetailRepository.save(onBenchDetail);
			} catch (Exception ex) {
				totalSuccess.decrementAndGet();
				final List<ErrorLineModel> tmpList = ErrorLineModel.createErrorDetails(JINZAIID_STAFFID,
						ErrorConstant.MESSAGE_ERROR_WHEN_SAVE);
				errorModelList.add(new ErrorModel(entry.getKey(), tmpList));
				final EmployeeImportDetailModel detailModel = importDetailModelMap.get(entry.getKey());
				detailModel.setMesssageLineList(tmpList);
				detailModel.setStatus(ImportOperationStatus.FAILED);
				importDetailModelMap.put(entry.getKey(), detailModel);
				isAtLeastOneErr = true;
			}
		}
		// rollback or commit
		if (isAtLeastOneErr) {
			txManager.rollback(status);
			return;
		}
		txManager.commit(status);
	}

	/**
	 * Saves an EmployeeImportModel and a map of EmployeeImportDetailModel to the
	 * database using a new transaction.
	 * If any exception occurs during the save operation, the transaction is rolled
	 * back and the method returns without committing.
	 * The EmployeeImportModel and the EmployeeImportDetailModel are transformed to
	 * their corresponding entities using the employeeImportModelTransformer and the
	 * employeeImportDetailModelTransformer respectively.
	 * 
	 * @param importModel          an EmployeeImportModel object representing the
	 *                             import information
	 * @param importDetailModelMap a map of EmployeeImportDetailModel objects, keyed
	 *                             by the row number, representing the import
	 *                             details
	 * @see EmployeeImportModel
	 * @see EmployeeImportDetailModel
	 * @see EmployeeImport
	 * @see EmployeeImportDetail
	 * @see EmployeeImportModelTransformer
	 * @see EmployeeImportDetailModelTransformer
	 * @author tminhto
	 */
	private void processEmployeeImportTx(
			final EmployeeImportModel importModel,
			final Map<Integer, EmployeeImportDetailModel> importDetailModelMap) {
		// init transaction
		final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("processEmployeeImportTx");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		final TransactionStatus status = txManager.getTransaction(def);
		boolean isAtLeastOneErr = false;
		try {
			final EmployeeImport employeeImport = employeeImportModelTransformer.apply(importModel);
			final EmployeeImport saved = employeeImportRepository.save(employeeImport);
			importDetailModelMap.forEach((key, detailModel) -> {
				final EmployeeImportDetail detail = employeeImportDetailModelTransformer.apply(detailModel);
				detail.setEmployeeImport(saved);
				employeeImportDetailRepository.save(detail);
			});
		} catch (Exception ex) {
			isAtLeastOneErr = true;
		}
		// rollback or commit
		if (isAtLeastOneErr) {
			txManager.rollback(status);
			return;
		}
		txManager.commit(status);
	}

	/**
     * Filters employees based on the provided conditions.
     *
     * @param nameString       The name string to search for. If null, no name string
     *                      filtering will be applied.
     * @param coeId          The center of excellence id to filter by. If null, no filtering
     *                      by coeId will be applied.
     * @return a List of employee information that matches the selected conditions.
     * @author SangBui
     * @see IEmployeeSearchForTeam
     */
	public List<IEmployeeSearchForTeam> searchEmployeesByCoeAndNameString(Integer coeId, String nameString) {
		return employeeRepository.searchEmployeesByCoeAndNameString(coeId, nameString);
	}
}
