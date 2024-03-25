package com.hitachi.coe.fullstack.service.impl;

import com.hitachi.coe.fullstack.constant.CommonConstant;
import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.entity.Employee;
import com.hitachi.coe.fullstack.entity.EmployeeProject;
import com.hitachi.coe.fullstack.entity.EmployeeRole;
import com.hitachi.coe.fullstack.entity.Project;
import com.hitachi.coe.fullstack.enums.EmployeeType;
import com.hitachi.coe.fullstack.exceptions.CoEException;
import com.hitachi.coe.fullstack.model.EmployeeModel;
import com.hitachi.coe.fullstack.model.EmployeeProjectAddModel;
import com.hitachi.coe.fullstack.model.EmployeeProjectModel;
import com.hitachi.coe.fullstack.model.EmployeeRoleModel;
import com.hitachi.coe.fullstack.model.IEmployeeProjectDetails;
import com.hitachi.coe.fullstack.model.IEmployeeProjectModel;
import com.hitachi.coe.fullstack.model.ProjectModel;
import com.hitachi.coe.fullstack.repository.EmployeeProjectRepository;
import com.hitachi.coe.fullstack.repository.EmployeeRepository;
import com.hitachi.coe.fullstack.repository.EmployeeRoleRepository;
import com.hitachi.coe.fullstack.repository.ProjectRepository;
import com.hitachi.coe.fullstack.service.EmployeeProjectService;
import com.hitachi.coe.fullstack.transformation.EmployeeProjectModelTransformer;
import com.hitachi.coe.fullstack.transformation.EmployeeProjectTransformer;
import com.hitachi.coe.fullstack.transformation.EmployeeRoleTransformer;
import com.hitachi.coe.fullstack.transformation.EmployeeTransformer;
import com.hitachi.coe.fullstack.transformation.ProjectTransformer;
import com.hitachi.coe.fullstack.util.CommonUtils;
import com.hitachi.coe.fullstack.util.DateUtils;
import com.hitachi.coe.fullstack.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeProjectServiceImpl implements EmployeeProjectService {

    @Autowired
    private EmployeeProjectRepository employeeProjectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeRoleRepository employeeRoleRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeProjectTransformer employeeProjectTransformer;

    @Autowired
    private EmployeeProjectModelTransformer employeeProjectModelTransformer;

    @Autowired
    private EmployeeRoleTransformer employeeRoleTransformer;

    @Autowired
    private EmployeeTransformer employeeTransformer;

    @Autowired
    private ProjectTransformer projectTransformer;

    @Override
    public List<EmployeeProjectModel> assignEmployeeProjects(
            final List<EmployeeProjectAddModel> employeeProjectAddModelList, final Integer projectId) {
        final Project validProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new CoEException(ErrorConstant.CODE_PROJECT_NOT_FOUND,
                        ErrorConstant.MESSAGE_PROJECT_NOT_FOUND));
        final ProjectModel validProjectModel = projectTransformer.apply(validProject);
        final List<EmployeeProject> employeeProjectList = new ArrayList<>();
        final List<EmployeeProjectModel> employeeProjectModelList = new ArrayList<>();
        for (EmployeeProjectAddModel employeeProjectAddModel : employeeProjectAddModelList) {
            final int type = EmployeeType.getEmployeeTypeByName(employeeProjectAddModel.getEmployeeType());
            final List<EmployeeProject> anotherProjects;
            final Timestamp startDate = Objects
                    .requireNonNull(CommonUtils.convertStringToTimestamp(employeeProjectAddModel.getStartDate()));
            final Timestamp endDate = Objects
                    .requireNonNull(CommonUtils.convertStringToTimestamp(employeeProjectAddModel.getEndDate()));
            final Employee employee = employeeRepository.findById(employeeProjectAddModel.getEmployeeId())
                    .orElseThrow(() -> new CoEException(ErrorConstant.CODE_EMPLOYEE_DO_NOT_EXIST,
                            ErrorConstant.MESSAGE_EMPLOYEE_DO_NOT_EXIST));
            final EmployeeRole employeeRole = employeeRoleRepository
                    .findById(employeeProjectAddModel.getEmployeeRoleId())
                    .orElseThrow(() -> new CoEException(ErrorConstant.CODE_EMPLOYEE_ROLE_DO_NOT_EXIST,
                            ErrorConstant.MESSAGE_EMPLOYEE_ROLE_DO_NOT_EXIST));
            if (DateUtils.isBefore(endDate, new Date(System.currentTimeMillis()))) {
                throw new CoEException(ErrorConstant.CODE_INVALID_END_DATE_GREATER_THAN_CURRENT,
                        ErrorConstant.MESSAGE_INVALID_END_DATE_GREATER_THAN_CURRENT);
            }
            if (DateUtils.isAfter(startDate, endDate)) {
                throw new CoEException(ErrorConstant.CODE_INVALID_START_DATE_END_DATE,
                        ErrorConstant.MESSAGE_INVALID_START_DATE_END_DATE);
            }
            if (!DateUtils.isBetween(startDate, validProject.getStartDate(), validProject.getEndDate())
                    && (!DateUtils.isEqual(startDate, validProject.getStartDate()))) {
                throw new CoEException(ErrorConstant.CODE_INVALID_START_DATE, ErrorConstant.MESSAGE_INVALID_START_DATE);
            }
            if (!DateUtils.isBetween(endDate, validProject.getStartDate(), validProject.getEndDate())
                    && (!DateUtils.isEqual(endDate, validProject.getEndDate()))) {
                throw new CoEException(ErrorConstant.CODE_INVALID_END_DATE, ErrorConstant.MESSAGE_INVALID_END_DATE);
            }

            // Find all project that employee is assigned to except projectId
            anotherProjects = employeeProjectRepository
                    .findEmployeeAssignedExceptProjectById(employeeProjectAddModel.getEmployeeId(), projectId);
            if (!ObjectUtils.isEmpty(anotherProjects)) {
                anotherProjects.forEach(anotherProject -> employeeProjectModelList
                        .add(employeeProjectTransformer.apply(anotherProject)));
            }

            final EmployeeProjectModel employeeProjectModel = new EmployeeProjectModel();
            final EmployeeProject employeeProject;
            final EmployeeModel employeeModel = employeeTransformer.apply(employee);
            final EmployeeRoleModel employeeRoleModel = employeeRoleTransformer.apply(employeeRole);
            final Integer utilization = employeeProjectAddModel.getUtilization();
            employeeProjectModel.setProject(validProjectModel);
            employeeProjectModel.setEmployee(employeeModel);
            employeeProjectModel.setStartDate(startDate);
            employeeProjectModel.setEndDate(endDate);
            employeeProjectModel.setEmployeeType(EmployeeType.values()[type]);
            employeeProjectModel.setUtilization(utilization);
            employeeProjectModel.setEmployeeRole(employeeRoleModel);
            employeeProjectModel.setCreatedBy(CommonConstant.CREATED_BY_ADMINISTRATOR);
            // I plus 1ms in createdDate for data integrity, ensure createdDate of different
            // rows won't duplicate
            employeeProjectModel.setCreated(new Date(System.currentTimeMillis() + 1));
            employeeProject = employeeProjectModelTransformer.apply(employeeProjectModel);
            employeeProjectList.add(employeeProject);
        }
        employeeProjectRepository.saveAll(employeeProjectList);
        return employeeProjectModelList;
    }

    @Override
    public EmployeeProjectModel releaseEmployeeProject(final Integer employeeId, final Integer projectId) {
        final Optional<Employee> employee = employeeRepository.findById(employeeId);
        final Optional<Project> project = projectRepository.findById(projectId);
        EmployeeProject releaseEmployeeProject = new EmployeeProject();
        final Date currentDate = new Date(System.currentTimeMillis());

        if (employee.isEmpty()) {
            throw new CoEException(ErrorConstant.CODE_EMPLOYEE_DO_NOT_EXIST,
                    ErrorConstant.MESSAGE_EMPLOYEE_DO_NOT_EXIST);
        }

        if (project.isEmpty()) {
            throw new CoEException(ErrorConstant.CODE_PROJECT_NOT_FOUND, ErrorConstant.MESSAGE_PROJECT_NOT_FOUND);
        }

        // Assign
        final Page<IEmployeeProjectModel> isAssignedProject = employeeProjectRepository
                .searchEmployeesProjectWithStatus(projectId, employee.get().getHccId(), "working", true,
                        PageRequest.of(0, 10, Sort.by("createdDate").descending()));

        if (isAssignedProject.getContent().isEmpty()) {
            throw new CoEException(ErrorConstant.CODE_EMPLOYEE_PROJECT_ALREADY_RELEASE,
                    ErrorConstant.MESSAGE_EMPLOYEE_PROJECT_ALREADY_RELEASE);
        }

        final Optional<EmployeeProject> employeeProject = employeeProjectRepository
                .findById(isAssignedProject.getContent().get(0).getEmployeeProjectId());
        if (employeeProject.isPresent()) {
            releaseEmployeeProject = employeeProject.get();
            if (DateUtils.isBefore(currentDate, releaseEmployeeProject.getStartDate())) {
                throw new CoEException(ErrorConstant.CODE_EMPLOYEE_PROJECT_NOT_START,
                        ErrorConstant.MESSAGE_EMPLOYEE_PROJECT_NOT_START);
            }
            releaseEmployeeProject.setReleaseDate(currentDate);
            employeeProjectRepository.save(releaseEmployeeProject);
        }
        return employeeProjectTransformer.apply(releaseEmployeeProject);
    }

    @Override
    public Page<IEmployeeProjectModel> searchEmployeesProjectWithStatus(Integer projectId, String keyword,
                                                                        String status, Boolean isCreatedDateLatest, Integer no, Integer limit, String sortBy, Boolean desc) {
        String searchKeyword = "%";
        try {
            if (keyword != null) {
                Integer.parseInt(keyword);
                searchKeyword = StringUtil.removeUnknownSymbol(keyword);
            }
        } catch (NumberFormatException nfe) {
            searchKeyword = StringUtil.combineString(StringUtil.removeUnknownSymbol(keyword), "%");
        }

        Sort sort = Sort.by(sortBy);
        if (desc != null && desc) {
            sort = sort.descending();
        }

        if (isCreatedDateLatest == null) {
            isCreatedDateLatest = false;
        }

        return employeeProjectRepository.searchEmployeesProjectWithStatus(projectId, searchKeyword, status,
                isCreatedDateLatest, PageRequest.of(no, limit, sort));
    }

    @Override
    public List<IEmployeeProjectDetails> getEmployeeProjectDetailsByEmployeeHccId(String hccId, Boolean isCreatedDateLatest) {
        // Check if hccId is null or empty
        if (ObjectUtils.isEmpty(hccId)) {
            throw new CoEException(ErrorConstant.CODE_HCC_ID_REQUIRED, ErrorConstant.MESSAGE_HCC_ID_REQUIRED);
        }
        // Check if employee exist by hccId
        if (ObjectUtils.isEmpty(employeeRepository.findByHccId(hccId))) {
            throw new CoEException(ErrorConstant.CODE_EMPLOYEE_NOT_FOUND, ErrorConstant.MESSAGE_EMPLOYEE_NOT_FOUND);
        }
        if (isCreatedDateLatest == null) {
            isCreatedDateLatest = false;
        }
        return employeeProjectRepository.getEmployeeProjectDetailsByEmployeeHccId(hccId, isCreatedDateLatest);
    }
}
