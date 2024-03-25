package com.hitachi.coe.fullstack.service;

import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.entity.BusinessDomain;
import com.hitachi.coe.fullstack.entity.BusinessUnit;
import com.hitachi.coe.fullstack.entity.Employee;
import com.hitachi.coe.fullstack.entity.EmployeeProject;
import com.hitachi.coe.fullstack.entity.EmployeeRole;
import com.hitachi.coe.fullstack.entity.Project;
import com.hitachi.coe.fullstack.entity.ProjectStatus;
import com.hitachi.coe.fullstack.entity.ProjectType;
import com.hitachi.coe.fullstack.enums.EmployeeType;
import com.hitachi.coe.fullstack.exceptions.CoEException;
import com.hitachi.coe.fullstack.model.EmployeeModel;
import com.hitachi.coe.fullstack.model.EmployeeProjectAddModel;
import com.hitachi.coe.fullstack.model.EmployeeProjectModel;
import com.hitachi.coe.fullstack.model.IEmployeeProjectDetails;
import com.hitachi.coe.fullstack.model.IEmployeeProjectModel;
import com.hitachi.coe.fullstack.model.ProjectModel;
import com.hitachi.coe.fullstack.repository.EmployeeProjectRepository;
import com.hitachi.coe.fullstack.repository.EmployeeRepository;
import com.hitachi.coe.fullstack.repository.EmployeeRoleRepository;
import com.hitachi.coe.fullstack.repository.ProjectRepository;
import com.hitachi.coe.fullstack.transformation.EmployeeProjectTransformer;
import com.hitachi.coe.fullstack.util.DateFormatUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class EmployeeProjectServiceTest {

    @Autowired
    EmployeeProjectService employeeProjectService;

    @MockBean
    EmployeeProjectRepository employeeProjectRepository;

    @MockBean
    EmployeeRepository employeeRepository;

    @MockBean
    EmployeeRoleRepository employeeRoleRepository;

    @MockBean
    ProjectRepository projectRepository;

    @MockBean
    EmployeeProjectTransformer employeeProjectTransformer;

    Timestamp date;
    Timestamp otherDate;
    Timestamp startDate;
    Timestamp endDate;
    Integer no;
    Integer limit;
    String sortBy;
    Boolean desc;
    Employee employee;
    Project project;
    EmployeeProject employeeProject;
    List<EmployeeProject> employeeProjects;
    EmployeeProjectModel employeeProjectModel;
    EmployeeModel employeeModel;
    ProjectModel projectModel;
    EmployeeProjectAddModel employeeProjectAddModel;
    List<EmployeeProjectAddModel> employeeProjectAddModelList;
    IEmployeeProjectModel iEmployeeProjectModelOne;
    IEmployeeProjectModel iEmployeeProjectModelTwo;
    IEmployeeProjectDetails iEmployeeProjectDetails;
    EmployeeRole employeeRole;
    ProjectType projectType;
    BusinessDomain businessDomain;
    BusinessUnit businessUnit;
    DateFormat dateFormat;
    Calendar calendar;

    Date currentDate;

    @BeforeEach
    void setUp(){
        date = DateFormatUtils.convertTimestampFromString("2023-07-01");
        otherDate = DateFormatUtils.convertTimestampFromString("2023-10-01");
        startDate = DateFormatUtils.convertTimestampFromString("2023-07-12");
        endDate = DateFormatUtils.convertTimestampFromString("2023-09-20");
        employee = new Employee();
        employee.setId(1);
        employee.setName("nguyenhai");
        employee.setHccId("123456");
        employee.setLdap("78910");

        employeeModel = new EmployeeModel();
        employeeModel.setId(1);
        employeeModel.setName("nguyenhai");
        employeeModel.setHccId("123456");
        employeeModel.setLdap("78910");

        projectType = new ProjectType();
        projectType.setId(1);
        projectType.setCode("project-type-code");
        projectType.setName("project-type-name");

        businessDomain = new BusinessDomain();
        businessDomain.setId(1);
        businessDomain.setCode("business-domain-code");
        businessDomain.setName("business-domain-name");

        businessUnit = new BusinessUnit();
        businessUnit.setId(1);
        businessUnit.setCode("business-unit-code");
        businessUnit.setName("business-unit-name");

        project = new Project();
        project.setId(1);
        project.setCode("project-code");
        project.setName("project-test-name");
        project.setProjectType(projectType);
        project.setProjectManager("pm");
        project.setBusinessDomain(businessDomain);
        project.setBusinessUnit(businessUnit);
        project.setCustomerName("customer-name");
        project.setStatus(ProjectStatus.ONGOING);
        project.setDescription("description");
        project.setStartDate(date);
        project.setEndDate(otherDate);

        projectModel = new ProjectModel();
        projectModel.setId(1);
        projectModel.setName("FSCMS");
        projectModel.setStartDate(date);
        projectModel.setEndDate(otherDate);

        employeeProject = new EmployeeProject();
        employeeProject.setId(1);
        employeeProject.setEmployee(employee);
        employeeProject.setProject(project);
        employeeProject.setEmployeeType(1);
        employeeProject.setStartDate(date);
        employeeProject.setEndDate(otherDate);

        employeeProjects = List.of(employeeProject);

        employeeProjectModel = new EmployeeProjectModel();
        employeeProjectModel.setId(1);
        employeeProjectModel.setEmployee(employeeModel);
        employeeProjectModel.setProject(projectModel);
        employeeProjectModel.setEmployeeType(EmployeeType.SHADOW);
        employeeProjectModel.setStartDate(date);
        employeeProjectModel.setEndDate(otherDate);

        employeeProjectAddModel = new EmployeeProjectAddModel();
        employeeProjectAddModel.setEmployeeId(1);
        employeeProjectAddModel.setEmployeeRoleId(1);
        employeeProjectAddModel.setEmployeeType("Shadow");
        employeeProjectAddModel.setUtilization(1);
        employeeProjectAddModel.setStartDate("2023-01-01 00:00:00.321");
        employeeProjectAddModel.setEndDate("2023-02-02 00:00:00.123");

        employeeProjectAddModelList = new ArrayList<>();
        employeeProjectAddModelList.add(employeeProjectAddModel);

        no = 0;
        limit = 10;
        sortBy = "name";
        desc = true;

        iEmployeeProjectModelOne = mock(IEmployeeProjectModel.class);
        iEmployeeProjectModelTwo = mock(IEmployeeProjectModel.class);

        iEmployeeProjectDetails = mock(IEmployeeProjectDetails.class);

        employeeRole = new EmployeeRole();
        employeeRole.setId(1);
        employeeRole.setCode("employee-code");
        employeeRole.setName("employee-name");
        employeeRole.setDescription("employee-description");

        currentDate = new Date(System.currentTimeMillis());
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
    }

    @Test
    public void testSearchEmployeesProjectWithStatus_whenValidData_thenSuccess(){

        when(iEmployeeProjectModelOne.getEmployeeProjectId()).thenReturn(1);
        when(iEmployeeProjectModelOne.getProjectId()).thenReturn(1);
        when(iEmployeeProjectModelOne.getEmployeeId()).thenReturn(1);
        when(iEmployeeProjectModelOne.getHccId()).thenReturn("Hcc1");
        when(iEmployeeProjectModelOne.getName()).thenReturn("Nguyen Van A");
        when(iEmployeeProjectModelOne.getEmail()).thenReturn("a.1@example.com");
        when(iEmployeeProjectModelOne.getEmployeeType()).thenReturn(1);
        when(iEmployeeProjectModelOne.getStartDate()).thenReturn(date);
        when(iEmployeeProjectModelOne.getEndDate()).thenReturn(date);
        when(iEmployeeProjectModelOne.getPmName()).thenReturn("Pm A");

        when(iEmployeeProjectModelTwo.getEmployeeProjectId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getProjectId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getEmployeeId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getHccId()).thenReturn("Hcc2");
        when(iEmployeeProjectModelTwo.getName()).thenReturn("Nguyen Van B");
        when(iEmployeeProjectModelTwo.getEmail()).thenReturn("a.2@example.com");
        when(iEmployeeProjectModelTwo.getEmployeeType()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getStartDate()).thenReturn(date);
        when(iEmployeeProjectModelTwo.getEndDate()).thenReturn(date);
        when(iEmployeeProjectModelTwo.getPmName()).thenReturn("Pm B");


        List<IEmployeeProjectModel> iEmployeeProjectModels = Arrays.asList(iEmployeeProjectModelOne,iEmployeeProjectModelTwo);
        Page<IEmployeeProjectModel> mockPage = new PageImpl<>(iEmployeeProjectModels);

        when(employeeProjectRepository.searchEmployeesProjectWithStatus(any(Integer.class), any(String.class), any(String.class), any(Boolean.class), any(PageRequest.class))).thenReturn(mockPage);

        Page<IEmployeeProjectModel> iEmployeeProjectModelPageActual = employeeProjectService.searchEmployeesProjectWithStatus(1, "Hcc", "release", false, no, limit, sortBy, desc);

        //Verify
        assertNotNull(iEmployeeProjectModelPageActual);
        assertEquals(iEmployeeProjectModelPageActual.getSize(), iEmployeeProjectModels.size());
        assertEquals(iEmployeeProjectModelPageActual.getContent().get(0).getEmployeeId(), iEmployeeProjectModels.get(0).getEmployeeId());
        assertEquals(iEmployeeProjectModelPageActual.getContent().get(0).getProjectId(), iEmployeeProjectModels.get(0).getEmployeeId());
        assertEquals(iEmployeeProjectModelPageActual.getContent().get(0).getEmployeeType(), iEmployeeProjectModels.get(0).getProjectId());
        assertEquals(iEmployeeProjectModelPageActual.getContent().get(0).getEmployeeProjectId(), iEmployeeProjectModels.get(0).getEmployeeProjectId());
        assertEquals(iEmployeeProjectModelPageActual.getContent().get(0).getHccId(), iEmployeeProjectModels.get(0).getHccId());
        assertEquals(iEmployeeProjectModelPageActual.getContent().get(0).getName(), iEmployeeProjectModels.get(0).getName());
        assertEquals(iEmployeeProjectModelPageActual.getContent().get(0).getEmail(), iEmployeeProjectModels.get(0).getEmail());
        assertEquals(iEmployeeProjectModelPageActual.getContent().get(0).getStartDate(), iEmployeeProjectModels.get(0).getStartDate());
        assertEquals(iEmployeeProjectModelPageActual.getContent().get(0).getEndDate(), iEmployeeProjectModels.get(0).getEndDate());
        assertEquals(iEmployeeProjectModelPageActual.getContent().get(0).getPmName(), iEmployeeProjectModels.get(0).getPmName());

        assertEquals(iEmployeeProjectModelPageActual.getContent().get(1).getEmployeeId(), iEmployeeProjectModels.get(1).getEmployeeId());
        assertEquals(iEmployeeProjectModelPageActual.getContent().get(1).getProjectId(), iEmployeeProjectModels.get(1).getEmployeeId());
        assertEquals(iEmployeeProjectModelPageActual.getContent().get(1).getEmployeeType(), iEmployeeProjectModels.get(1).getProjectId());
        assertEquals(iEmployeeProjectModelPageActual.getContent().get(1).getEmployeeProjectId(), iEmployeeProjectModels.get(1).getEmployeeProjectId());
        assertEquals(iEmployeeProjectModelPageActual.getContent().get(1).getHccId(), iEmployeeProjectModels.get(1).getHccId());
        assertEquals(iEmployeeProjectModelPageActual.getContent().get(1).getName(), iEmployeeProjectModels.get(1).getName());
        assertEquals(iEmployeeProjectModelPageActual.getContent().get(1).getEmail(), iEmployeeProjectModels.get(1).getEmail());
        assertEquals(iEmployeeProjectModelPageActual.getContent().get(1).getStartDate(), iEmployeeProjectModels.get(1).getStartDate());
        assertEquals(iEmployeeProjectModelPageActual.getContent().get(1).getEndDate(), iEmployeeProjectModels.get(1).getEndDate());
        assertEquals(iEmployeeProjectModelPageActual.getContent().get(1).getPmName(), iEmployeeProjectModels.get(1).getPmName());

    }

    @Test
    public void testAssignEmployeeProjects_whenNotAssignToAnyProject_thenSuccess(){
        // prepare
        Page<IEmployeeProjectModel> mockPage = new PageImpl<>(Collections.emptyList());
        List<EmployeeProjectModel> employeeProjectModelListExpected = List.of(employeeProjectModel);
        calendar.clear();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        project.setStartDate(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        project.setEndDate(calendar.getTime());

        calendar.clear();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        employeeProjectAddModel.setStartDate(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        employeeProjectAddModel.setEndDate(dateFormat.format(calendar.getTime()));

        employeeProjectAddModelList.clear();
        employeeProjectAddModelList.add(employeeProjectAddModel);
        // when-then
        when(employeeRepository.findById(any(Integer.class))).thenReturn(Optional.of(employee));
        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.of(project));
        when(employeeProjectRepository.searchEmployeesProjectWithStatus(any(Integer.class), any(String.class), any(String.class), any(Boolean.class), any(PageRequest.class))).thenReturn(mockPage);
        when(employeeProjectRepository.findEmployeeAssignedExceptProjectById(any(Integer.class),any(Integer.class))).thenReturn(employeeProjects);
        when(employeeProjectTransformer.apply(any(EmployeeProject.class))).thenReturn(employeeProjectModel);
        when(employeeProjectRepository.saveAll(employeeProjects)).thenReturn(employeeProjects);
        when(employeeRoleRepository.findById(employeeProjectAddModel.getEmployeeRoleId())).thenReturn(Optional.ofNullable(employeeRole));
        // invoke
        List<EmployeeProjectModel> employeeProjectModelListActual = employeeProjectService.assignEmployeeProjects(employeeProjectAddModelList, project.getId());
        // assert
        assertNotNull(employeeProjectModelListActual);
        assertEquals(employeeProjectModelListExpected.size(), employeeProjectModelListActual.size());
        assertEquals(employeeProjectModelListExpected.get(0).getId(), employeeProjectModelListActual.get(0).getId());
        assertEquals(employeeProjectModelListExpected.get(0).getEmployee(), employeeProjectModelListActual.get(0).getEmployee());
        assertEquals(employeeProjectModelListExpected.get(0).getProject(), employeeProjectModelListActual.get(0).getProject());
        assertEquals(employeeProjectModelListExpected.get(0).getEmployeeType(), employeeProjectModelListActual.get(0).getEmployeeType());
        assertEquals(employeeProjectModelListExpected.get(0).getStartDate(), employeeProjectModelListActual.get(0).getStartDate());
        assertEquals(employeeProjectModelListExpected.get(0).getEndDate(), employeeProjectModelListActual.get(0).getEndDate());

    }

    @Test
    public void testAssignEmployeeProjects_whenAssigningToAnotherProject_thenSuccess(){
        // prepare
        List<IEmployeeProjectModel> iEmployeeProjectModels = List.of(iEmployeeProjectModelTwo);
        Page<IEmployeeProjectModel> mockPage = new PageImpl<>(iEmployeeProjectModels);
        calendar.clear();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        project.setStartDate(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        project.setEndDate(calendar.getTime());

        calendar.clear();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        employeeProjectAddModel.setStartDate(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        employeeProjectAddModel.setEndDate(dateFormat.format(calendar.getTime()));

        employeeProjectAddModelList.clear();
        employeeProjectAddModelList.add(employeeProjectAddModel);
        // when-then
        when(iEmployeeProjectModelTwo.getEmployeeProjectId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getProjectId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getEmployeeId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getHccId()).thenReturn("123456");
        when(iEmployeeProjectModelTwo.getName()).thenReturn("nguyenhai");
        when(iEmployeeProjectModelTwo.getEmail()).thenReturn("a.2@example.com");
        when(iEmployeeProjectModelTwo.getEmployeeType()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getStartDate()).thenReturn(startDate);
        when(iEmployeeProjectModelTwo.getEndDate()).thenReturn(endDate);
        when(iEmployeeProjectModelTwo.getPmName()).thenReturn("Pm B");
        when(employeeRepository.findById(any(Integer.class))).thenReturn(Optional.of(employee));
        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.of(project));
        when(employeeProjectRepository.searchEmployeesProjectWithStatus(any(Integer.class), any(String.class), any(String.class), any(Boolean.class), any(PageRequest.class))).thenReturn(mockPage);
        when(employeeRepository.findById(any(Integer.class))).thenReturn(Optional.of(employee));
        when(employeeRoleRepository.findById(employeeProjectAddModel.getEmployeeRoleId())).thenReturn(Optional.ofNullable(employeeRole));
        // invoke
        List<EmployeeProjectModel> employeeProjectModelListActual = employeeProjectService.assignEmployeeProjects(employeeProjectAddModelList, project.getId());
        // assert
        assertNotNull(employeeProjectModelListActual);
        assertTrue(employeeProjectModelListActual.isEmpty());
    }

    @Test
    public void testAssignEmployeeProjects_whenEmployeeNotFound_thenThrowCoEException(){
        // prepare
        List<IEmployeeProjectModel> iEmployeeProjectModels = List.of(iEmployeeProjectModelTwo);
        Page<IEmployeeProjectModel> mockPage = new PageImpl<>(iEmployeeProjectModels);
        calendar.clear();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        project.setStartDate(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        project.setEndDate(calendar.getTime());

        calendar.clear();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 5); // Set employeeProjectAddModel start date before project start date
        employeeProjectAddModel.setStartDate(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 20); // Set employeeProjectAddModel end date after project end date
        employeeProjectAddModel.setEndDate(dateFormat.format(calendar.getTime()));

        employeeProjectAddModelList.clear();
        employeeProjectAddModelList.add(employeeProjectAddModel);
        // when-then
        when(employeeRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.of(project));
        when(employeeProjectRepository.searchEmployeesProjectWithStatus(any(Integer.class), any(String.class), any(String.class), any(Boolean.class), any(PageRequest.class))).thenReturn(mockPage);
        when(employeeRoleRepository.findById(employeeProjectAddModel.getEmployeeRoleId())).thenReturn(Optional.ofNullable(employeeRole));
        // assert
        Throwable throwable = assertThrows(CoEException.class,
                () -> employeeProjectService.assignEmployeeProjects(employeeProjectAddModelList, project.getId()));
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_EMPLOYEE_DO_NOT_EXIST, throwable.getMessage());
    }

    @Test
    public void testAssignEmployeeProjects_whenProjectNotFound_thenThrowCoEException(){
        // when-then
        when(employeeRepository.findById(any(Integer.class))).thenReturn(Optional.of(employee));
        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        // assert
        Throwable throwable = assertThrows(CoEException.class,
                () -> employeeProjectService.assignEmployeeProjects(employeeProjectAddModelList, project.getId()));
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_PROJECT_NOT_FOUND, throwable.getMessage());
    }

    @Test
    public void testAssignEmployeeProjects_whenEndDateBeforeStartDate_thenThrowCoEException(){
        // prepare
        List<IEmployeeProjectModel> iEmployeeProjectModels = List.of(iEmployeeProjectModelTwo);
        Page<IEmployeeProjectModel> mockPage = new PageImpl<>(iEmployeeProjectModels);
        calendar.clear();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        project.setStartDate(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        project.setEndDate(calendar.getTime());

        calendar.clear();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        employeeProjectAddModel.setEndDate(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        employeeProjectAddModel.setStartDate(dateFormat.format(calendar.getTime()));

        employeeProjectAddModelList.clear();
        employeeProjectAddModelList.add(employeeProjectAddModel);
        // when-then
        when(iEmployeeProjectModelTwo.getEmployeeProjectId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getProjectId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getEmployeeId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getHccId()).thenReturn("123456");
        when(iEmployeeProjectModelTwo.getName()).thenReturn("nguyenhai");
        when(iEmployeeProjectModelTwo.getEmail()).thenReturn("a.2@example.com");
        when(iEmployeeProjectModelTwo.getEmployeeType()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getStartDate()).thenReturn(startDate);
        when(iEmployeeProjectModelTwo.getEndDate()).thenReturn(endDate);
        when(iEmployeeProjectModelTwo.getPmName()).thenReturn("Pm B");
        when(employeeRepository.findById(any(Integer.class))).thenReturn(Optional.of(employee));
        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.of(project));
        when(employeeProjectRepository.searchEmployeesProjectWithStatus(any(Integer.class), any(String.class), any(String.class), any(Boolean.class), any(PageRequest.class))).thenReturn(mockPage);
        when(employeeRepository.findById(any(Integer.class))).thenReturn(Optional.of(employee));
        when(employeeRoleRepository.findById(employeeProjectAddModel.getEmployeeRoleId())).thenReturn(Optional.ofNullable(employeeRole));
        // assert
        Throwable throwable = assertThrows(CoEException.class,
                () -> employeeProjectService.assignEmployeeProjects(employeeProjectAddModelList, project.getId()));
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_INVALID_START_DATE_END_DATE, throwable.getMessage());

    }

    @Test
    public void testAssignEmployeeProjects_whenEndDateBeforeCurrentDate_thenThrowCoEException(){
        // prepare
        List<IEmployeeProjectModel> iEmployeeProjectModels = List.of(iEmployeeProjectModelTwo);
        Page<IEmployeeProjectModel> mockPage = new PageImpl<>(iEmployeeProjectModels);
        calendar.clear();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        project.setStartDate(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        project.setEndDate(calendar.getTime());

        calendar.clear();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        employeeProjectAddModel.setStartDate(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, -10);
        employeeProjectAddModel.setEndDate(dateFormat.format(calendar.getTime()));

        employeeProjectAddModelList.clear();
        employeeProjectAddModelList.add(employeeProjectAddModel);
        // when-then
        when(iEmployeeProjectModelTwo.getEmployeeProjectId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getProjectId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getEmployeeId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getHccId()).thenReturn("123456");
        when(iEmployeeProjectModelTwo.getName()).thenReturn("nguyenhai");
        when(iEmployeeProjectModelTwo.getEmail()).thenReturn("a.2@example.com");
        when(iEmployeeProjectModelTwo.getEmployeeType()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getStartDate()).thenReturn(startDate);
        when(iEmployeeProjectModelTwo.getEndDate()).thenReturn(endDate);
        when(iEmployeeProjectModelTwo.getPmName()).thenReturn("Pm B");
        when(employeeRepository.findById(any(Integer.class))).thenReturn(Optional.of(employee));
        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.of(project));
        when(employeeProjectRepository.searchEmployeesProjectWithStatus(any(Integer.class), any(String.class), any(String.class), any(Boolean.class), any(PageRequest.class))).thenReturn(mockPage);
        when(employeeRepository.findById(any(Integer.class))).thenReturn(Optional.of(employee));
        when(employeeRoleRepository.findById(employeeProjectAddModel.getEmployeeRoleId())).thenReturn(Optional.ofNullable(employeeRole));
        // assert
        Throwable throwable = assertThrows(CoEException.class,
                () -> employeeProjectService.assignEmployeeProjects(employeeProjectAddModelList, project.getId()));
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_INVALID_END_DATE_GREATER_THAN_CURRENT, throwable.getMessage());
    }

    @Test
    public void testAssignEmployeeProjects_whenStartDateBeforeStartDateProject_thenThrowCoEException(){
        // prepare
        List<IEmployeeProjectModel> iEmployeeProjectModels = List.of(iEmployeeProjectModelTwo);
        Page<IEmployeeProjectModel> mockPage = new PageImpl<>(iEmployeeProjectModels);
        calendar.clear();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        project.setStartDate(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        project.setEndDate(calendar.getTime());

        calendar.clear();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, -5); // Set employeeProjectAddModel start date before project start date
        employeeProjectAddModel.setStartDate(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 20); // Set employeeProjectAddModel end date after project end date
        employeeProjectAddModel.setEndDate(dateFormat.format(calendar.getTime()));

        employeeProjectAddModelList.clear();
        employeeProjectAddModelList.add(employeeProjectAddModel);
        // when-then
        when(iEmployeeProjectModelTwo.getEmployeeProjectId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getProjectId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getEmployeeId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getHccId()).thenReturn("123456");
        when(iEmployeeProjectModelTwo.getName()).thenReturn("nguyenhai");
        when(iEmployeeProjectModelTwo.getEmail()).thenReturn("a.2@example.com");
        when(iEmployeeProjectModelTwo.getEmployeeType()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getStartDate()).thenReturn(startDate);
        when(iEmployeeProjectModelTwo.getEndDate()).thenReturn(endDate);
        when(iEmployeeProjectModelTwo.getPmName()).thenReturn("Pm B");
        when(employeeRepository.findById(any(Integer.class))).thenReturn(Optional.of(employee));
        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.of(project));
        when(employeeProjectRepository.searchEmployeesProjectWithStatus(any(Integer.class), any(String.class), any(String.class), any(Boolean.class), any(PageRequest.class))).thenReturn(mockPage);
        when(employeeRepository.findById(any(Integer.class))).thenReturn(Optional.of(employee));
        when(employeeRoleRepository.findById(employeeProjectAddModel.getEmployeeRoleId())).thenReturn(Optional.ofNullable(employeeRole));
        // assert
        Throwable throwable = assertThrows(CoEException.class,
                () -> employeeProjectService.assignEmployeeProjects(employeeProjectAddModelList, project.getId()));
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_INVALID_START_DATE, throwable.getMessage());
    }

    @Test
    public void testAssignEmployeeProjects_whenEndDateAfterEndDateProject_thenThrowCoEException()
            throws ParseException {
        // prepare
        List<IEmployeeProjectModel> iEmployeeProjectModels = List.of(iEmployeeProjectModelTwo);
        Page<IEmployeeProjectModel> mockPage = new PageImpl<>(iEmployeeProjectModels);
        calendar.clear();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        project.setStartDate(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        project.setEndDate(calendar.getTime());

        calendar.clear();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        employeeProjectAddModel.setStartDate(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 35);
        employeeProjectAddModel.setEndDate(dateFormat.format(calendar.getTime()));

        employeeProjectAddModelList.clear();
        employeeProjectAddModelList.add(employeeProjectAddModel);
        // when-then
        when(iEmployeeProjectModelTwo.getEmployeeProjectId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getProjectId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getEmployeeId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getHccId()).thenReturn("123456");
        when(iEmployeeProjectModelTwo.getName()).thenReturn("nguyenhai");
        when(iEmployeeProjectModelTwo.getEmail()).thenReturn("a.2@example.com");
        when(iEmployeeProjectModelTwo.getEmployeeType()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getStartDate()).thenReturn(startDate);
        when(iEmployeeProjectModelTwo.getEndDate()).thenReturn(endDate);
        when(iEmployeeProjectModelTwo.getPmName()).thenReturn("Pm B");
        when(employeeRepository.findById(any(Integer.class))).thenReturn(Optional.of(employee));
        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.of(project));
        when(employeeProjectRepository.searchEmployeesProjectWithStatus(any(Integer.class), any(String.class), any(String.class), any(Boolean.class), any(PageRequest.class))).thenReturn(mockPage);
        when(employeeRepository.findById(any(Integer.class))).thenReturn(Optional.of(employee));
        when(employeeRoleRepository.findById(employeeProjectAddModel.getEmployeeRoleId())).thenReturn(Optional.ofNullable(employeeRole));
        // assert
        Throwable throwable = assertThrows(CoEException.class,
                () -> employeeProjectService.assignEmployeeProjects(employeeProjectAddModelList, project.getId()));
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_INVALID_END_DATE, throwable.getMessage());
    }

    @Test
    public void testReleaseEmployeeProjects_whenValidData_thenSuccess(){
        // prepare
        List<IEmployeeProjectModel> iEmployeeProjectModels = List.of(iEmployeeProjectModelTwo);
        Page<IEmployeeProjectModel> mockPage = new PageImpl<>(iEmployeeProjectModels);
        // when-then
        when(iEmployeeProjectModelTwo.getEmployeeProjectId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getProjectId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getEmployeeId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getHccId()).thenReturn("123456");
        when(iEmployeeProjectModelTwo.getName()).thenReturn("nguyenhai");
        when(iEmployeeProjectModelTwo.getEmail()).thenReturn("a.2@example.com");
        when(iEmployeeProjectModelTwo.getEmployeeType()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getStartDate()).thenReturn(startDate);
        when(iEmployeeProjectModelTwo.getEndDate()).thenReturn(endDate);
        when(iEmployeeProjectModelTwo.getPmName()).thenReturn("Pm B");
        when(employeeRepository.findById(any(Integer.class))).thenReturn(Optional.of(employee));
        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.of(project));
        when(employeeProjectRepository.searchEmployeesProjectWithStatus(any(Integer.class), any(String.class), any(String.class), any(Boolean.class), any(PageRequest.class))).thenReturn(mockPage);
        when(employeeProjectRepository.findById(any(Integer.class))).thenReturn(Optional.of(employeeProject));
        when(employeeProjectRepository.save(any(EmployeeProject.class))).thenReturn(employeeProject);
        when(employeeProjectTransformer.apply(any(EmployeeProject.class))).thenReturn(employeeProjectModel);
        // invoke
        EmployeeProjectModel employeeProjectModelActual = employeeProjectService.releaseEmployeeProject(1, 1);
        EmployeeProjectModel employeeProjectModelExpected = employeeProjectModel;
        // assert
        assertNotNull(employeeProjectModelActual);
        assertEquals(employeeProjectModelExpected.getId(), employeeProjectModelActual.getId());
        assertEquals(employeeProjectModelExpected.getEmployee(), employeeProjectModelActual.getEmployee());
        assertEquals(employeeProjectModelExpected.getProject(), employeeProjectModelActual.getProject());
        assertEquals(employeeProjectModelExpected.getEmployeeType(), employeeProjectModelActual.getEmployeeType());
        assertEquals(employeeProjectModelExpected.getStartDate(), employeeProjectModelActual.getStartDate());
        assertEquals(employeeProjectModelExpected.getEndDate(), employeeProjectModelActual.getEndDate());
    }

    @Test
    public void testReleaseEmployeeProjects_whenEmployeeNotFound_thenThrowCoEException(){
        // when-then
        when(employeeRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        // invoke
        Throwable throwable = assertThrows(CoEException.class,
                () -> employeeProjectService.releaseEmployeeProject(1,1));
        // assert
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_EMPLOYEE_DO_NOT_EXIST, throwable.getMessage());
    }

    @Test
    public void testReleaseEmployeeProjects_whenProjectNotFound_thenThrowCoEException(){
        // when-then
        when(employeeRepository.findById(any(Integer.class))).thenReturn(Optional.of(employee));
        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        // invoke
        Throwable throwable = assertThrows(CoEException.class,
                () -> employeeProjectService.releaseEmployeeProject(1,1));
        // assert
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_PROJECT_NOT_FOUND, throwable.getMessage());
    }

    @Test
    public void testReleaseEmployeeProjects_whenEndDateBeforeCurrentDate_thenThrowCoEException(){
        // prepare
        List<IEmployeeProjectModel> iEmployeeProjectModels = List.of(iEmployeeProjectModelTwo);
        Page<IEmployeeProjectModel> mockPage = new PageImpl<>(iEmployeeProjectModels);
        employeeProject.setStartDate(DateFormatUtils.convertTimestampFromString("9999-07-26"));
        // when-then
        when(iEmployeeProjectModelTwo.getEmployeeProjectId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getProjectId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getEmployeeId()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getHccId()).thenReturn("123456");
        when(iEmployeeProjectModelTwo.getName()).thenReturn("nguyenhai");
        when(iEmployeeProjectModelTwo.getEmail()).thenReturn("a.2@example.com");
        when(iEmployeeProjectModelTwo.getEmployeeType()).thenReturn(1);
        when(iEmployeeProjectModelTwo.getStartDate()).thenReturn(startDate);
        when(iEmployeeProjectModelTwo.getEndDate()).thenReturn(endDate);
        when(iEmployeeProjectModelTwo.getPmName()).thenReturn("Pm B");
        when(employeeRepository.findById(any(Integer.class))).thenReturn(Optional.of(employee));
        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.of(project));
        when(employeeProjectRepository.searchEmployeesProjectWithStatus(any(Integer.class), any(String.class), any(String.class), any(Boolean.class), any(PageRequest.class))).thenReturn(mockPage);
        when(employeeProjectRepository.findById(any(Integer.class))).thenReturn(Optional.of(employeeProject));
        // invoke
        Throwable throwable = assertThrows(CoEException.class,
                () -> employeeProjectService.releaseEmployeeProject(1,1));
        // assert
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_EMPLOYEE_PROJECT_NOT_START, throwable.getMessage());

    }

    @Test
    public void testReleaseEmployeeProjects_whenAlreadyReleaseToProject_thenThrowCoEException(){
        // prepare
        Page<IEmployeeProjectModel> mockPage = new PageImpl<>(Collections.emptyList());
        // when-then
        when(employeeRepository.findById(any(Integer.class))).thenReturn(Optional.of(employee));
        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.of(project));
        when(employeeProjectRepository.searchEmployeesProjectWithStatus(any(Integer.class), any(String.class), any(String.class), any(Boolean.class), any(PageRequest.class))).thenReturn(mockPage);
        // invoke
        Throwable throwable = assertThrows(CoEException.class,
                () -> employeeProjectService.releaseEmployeeProject(1,1));
        // assert
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_EMPLOYEE_PROJECT_ALREADY_RELEASE, throwable.getMessage());
    }

    @Test
    void testGetEmployeeProjectDetailsByEmployeeHccId_whenValidHccId_thenReturnListOfIEmployeeProjectDetails() {
        // prepare
        String testHccId = "test_hccId";
        Employee testEmployee = new Employee();
        testEmployee.setHccId("9999");
        testEmployee.setName("test_employee");
        testEmployee.setEmail("test_employee@gmail.com");
        List<IEmployeeProjectDetails> iEmployeeProjectDetailsList = List.of(iEmployeeProjectDetails);
        // when - then
        when(employeeRepository.findByHccId(anyString())).thenReturn(testEmployee);
        when(employeeProjectRepository.getEmployeeProjectDetailsByEmployeeHccId(anyString(), anyBoolean())).thenReturn(iEmployeeProjectDetailsList);
        // invoke
        List<IEmployeeProjectDetails> result = employeeProjectService.getEmployeeProjectDetailsByEmployeeHccId(testHccId, false);
        // assert
        assertNotNull(result);
        assertNotNull(testEmployee);
        assertNotNull(iEmployeeProjectDetailsList);
        // verify
        verify(employeeRepository).findByHccId(anyString());
        verify(employeeProjectRepository).getEmployeeProjectDetailsByEmployeeHccId(anyString(), anyBoolean());
    }

    @Test
    void testGetEmployeeProjectDetailsByEmployeeHccId_whenInvalidHccId_thenThrowCoEException() {
        // prepare
        String testHccId = "test_hccId";
        // when - then
        when(employeeRepository.findByHccId(anyString())).thenReturn(null);
        // assert
        assertThrows(CoEException.class, () -> employeeProjectService.getEmployeeProjectDetailsByEmployeeHccId(testHccId, false));
        // verify
        verify(employeeRepository).findByHccId(testHccId);
    }
}
