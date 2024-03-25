package com.hitachi.coe.fullstack.controller;

import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.exceptions.CoEException;
import com.hitachi.coe.fullstack.model.BusinessDomainModel;
import com.hitachi.coe.fullstack.model.BusinessUnitModel;
import com.hitachi.coe.fullstack.model.ChartSkillAndLevelModel;
import com.hitachi.coe.fullstack.model.ProjectCountModel;
import com.hitachi.coe.fullstack.model.ProjectModel;
import com.hitachi.coe.fullstack.model.ProjectTypeModel;
import com.hitachi.coe.fullstack.model.ProjectUpdateModel;
import com.hitachi.coe.fullstack.model.SkillSetModel;
import com.hitachi.coe.fullstack.model.common.BaseResponse;
import com.hitachi.coe.fullstack.service.ProjectService;
import com.hitachi.coe.fullstack.util.DateFormatUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ProjectControllerTest {

    @Autowired
    private ProjectController projectController;

    @MockBean
    private ProjectService projectService;

    Date date;

    Date currentDate;

    Date endDate;

    BusinessDomainModel businessDomain;

    BusinessUnitModel businessUnit;

    ProjectTypeModel projectType;

    SkillSetModel skillSet1;

    SkillSetModel skillSet2;

    ProjectModel projectModel;

    ProjectModel projectModelOne;

    ProjectModel projectModelTwo;

    ProjectUpdateModel projectUpdate;

    @BeforeEach
    void setUp() {

        date = DateFormatUtils.convertTimestampFromString("2023-05-23");
        currentDate = DateFormatUtils.convertTimestampFromString("2023-09-23");
        endDate = DateFormatUtils.convertTimestampFromString("2023-10-23");

        businessDomain = new BusinessDomainModel();
        businessDomain.setId(1);
        businessDomain.setName("Retail");

        businessUnit = new BusinessUnitModel();
        businessUnit.setId(1);
        businessUnit.setName("IoT Deliveries Leader");

        projectType = new ProjectTypeModel();
        projectType.setId(1);
        projectType.setName("OnSemi");

        skillSet1 = new SkillSetModel();
        skillSet1.setId(1);
        skillSet1.setName("Java");

        skillSet2 = new SkillSetModel();
        skillSet2.setId(2);
        skillSet2.setName("C");

        projectModel = new ProjectModel();
        projectModel.setCode("1");
        projectModel.setCustomerName("Nguyen A");
        projectModel.setDescription("description");
        projectModel.setEndDate(date);
        projectModel.setName("OnSemi");
        projectModel.setProjectManager("Pm A");
        projectModel.setStartDate(date);
        projectModel.setStatus(1);
        projectModel.setBusinessDomain(businessDomain);
        projectModel.setProjectType(projectType);
        projectModel.setSkillSets(Arrays.asList(skillSet1, skillSet2));

        projectModelOne = new ProjectModel();
        projectModelOne.setCode("1");
        projectModelOne.setCustomerName("Nguyen A");
        projectModelOne.setDescription("description");
        projectModelOne.setEndDate(endDate);
        projectModelOne.setName("OnSemi");
        projectModelOne.setProjectManager("Pm A");
        projectModelOne.setStartDate(currentDate);
        projectModelOne.setStatus(1);
        projectModelOne.setBusinessDomain(businessDomain);
        projectModelOne.setProjectType(projectType);
        projectModelOne.setSkillSets(Arrays.asList(skillSet1, skillSet2));

        projectModelTwo = new ProjectModel();
        projectModelTwo.setCode("1");
        projectModelTwo.setCustomerName("Nguyen A");
        projectModelTwo.setDescription("description");
        projectModelTwo.setEndDate(endDate);
        projectModelTwo.setName("OnSemi");
        projectModelTwo.setProjectManager("Pm A");
        projectModelTwo.setStartDate(currentDate);
        projectModelTwo.setStatus(1);
        projectModelTwo.setBusinessDomain(businessDomain);
        projectModelTwo.setProjectType(projectType);
        projectModelTwo.setSkillSets(Arrays.asList(skillSet1, skillSet2));

        projectUpdate = new ProjectUpdateModel();
        projectUpdate.setProjectId(5);
        projectUpdate.setName("OnSemi");
        projectUpdate.setCode("1");
        projectUpdate.setCustomerName("Nguyen A");
        projectUpdate.setDescription("description");
        projectUpdate.setStartDate("2023-05-23");
        projectUpdate.setEndDate("2023-06-23");
        projectUpdate.setProjectManager("Pm a");
        projectUpdate.setStatus(3);
        projectUpdate.setBusinessDomainId(2);
        projectUpdate.setProjectTypeId(1);
        projectUpdate.setProjectsTech(Arrays.asList(1, 2, 3));

        projectModelOne = mock(ProjectModel.class);
        projectModelTwo = mock(ProjectModel.class);
    }

    @Test
    public void testGetProjectDetailById_whenValidData_thenSuccess() {

        when(projectService.getProjectDetailById(1)).thenReturn(projectModel);

        BaseResponse<ProjectModel> result = projectController.getProjectDetailById(1);

        assertNotNull(result);
        assertEquals(HttpStatus.OK.value(), result.getStatus());
        assertNull(result.getMessage());
        assertNotNull(result.getData());
        assertEquals(projectModel, result.getData());
    }

    @Test
    public void testGetProjectDetailById_whenIsNull_thenThrowCoEException() {

        when(projectService.getProjectDetailById(1))
                .thenThrow(new CoEException(ErrorConstant.CODE_PROJECT_NOT_FOUND, ErrorConstant.MESSAGE_PROJECT_NOT_FOUND));

        CoEException exception = assertThrows(CoEException.class, () -> {
            projectController.getProjectDetailById(1);
        });
        assertEquals(CoEException.class, exception.getClass());
        assertEquals(ErrorConstant.MESSAGE_PROJECT_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void testUpdateProject_whenValidData_thenSuccess() {

        when(projectService.update(projectUpdate)).thenReturn(projectModel);

        BaseResponse<ProjectModel> result = projectController.updateProject(projectUpdate);

        assertNotNull(result);
        assertEquals(HttpStatus.OK.value(), result.getStatus());
        assertNull(result.getMessage());
        assertNotNull(result.getData());
        assertEquals(projectModel, result.getData());
    }

    @Test
    public void testUpdateProjectStatus_whenValidData_thenSuccess() {

        when(projectService.updateProjectStatus(any(Integer.class), any(Integer.class))).thenReturn(projectModel);

        BaseResponse<ProjectModel> result = projectController.updateProjectStatus(1, 3);

        assertNotNull(result);
        assertEquals(HttpStatus.OK.value(), result.getStatus());
        assertNull(result.getMessage());
        assertNotNull(result.getData());
        assertEquals(projectModel, result.getData());
    }

    @Test
    public void testCloseProject_whenValidData_thenSuccess() {

        when(projectService.closeProject(any(Integer.class))).thenReturn(projectModel);

        BaseResponse<ProjectModel> result = projectController.closeProject(1);

        assertNotNull(result);
        assertEquals(HttpStatus.OK.value(), result.getStatus());
        assertNull(result.getMessage());
        assertNotNull(result.getData());
        assertEquals(projectModel, result.getData());
    }

    @Test
    void testGetNewlyCreatedProject_whenEmptyBuId_thenReturnSucess() {
        List<ProjectModel> mockProject = new ArrayList<>();
        mockProject.add(projectModelOne);
        mockProject.add(projectModelTwo);

        when(projectService.getNewlyCreatedProject(eq(null))).thenReturn(mockProject);

        BaseResponse<List<ProjectModel>> result = projectController.getNewlyCreatedProject(null);

        assertEquals(200, result.getStatus());
        assertEquals(mockProject, result.getData());
    }

    @Test
    void testGetNewlyCreatedProject_whenEmptyResponse_thenReturnEmptyResult() {
        List<ProjectModel> mockProject = new ArrayList<>(Collections.emptyList());

        when(projectService.getNewlyCreatedProject(anyInt())).thenReturn(mockProject);

        BaseResponse<List<ProjectModel>> result = projectController.getNewlyCreatedProject(1);

        assertNotNull(result);
        assertTrue(result.getData().isEmpty());
    }

    @Test
    void testGetNewlyCreatedProject_whenValidData_thenReturnSuccess() {
        List<ProjectModel> mockProject = new ArrayList<>();
        mockProject.add(projectModelOne);
        mockProject.add(projectModelTwo);

        when(projectService.getNewlyCreatedProject(anyInt())).thenReturn(mockProject);

        BaseResponse<List<ProjectModel>> result = projectController.getNewlyCreatedProject(1);

        assertEquals(200, result.getStatus());
        assertEquals(mockProject, result.getData());
    }

    @Test
    void testCountNewlyCreatedProject_whenEmptyBuId_thenReturnSuccess() {
        List<ProjectModel> mockProject = new ArrayList<>();
        mockProject.add(projectModelOne);
        mockProject.add(projectModelTwo);

        when(projectService.countNewlyCreatedProject(eq(null))).thenReturn(mockProject.size());

        ResponseEntity<Integer> result = projectController.countNewlyCreatedProject(null);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody());
    }

    @Test
    void testCountNewlyCreatedProject_whenValidData_thenReturnSuccess() {
        List<ProjectModel> mockProject = new ArrayList<>();
        mockProject.add(projectModelOne);
        mockProject.add(projectModelTwo);

        when(projectService.countNewlyCreatedProject(anyInt())).thenReturn(mockProject.size());

        ResponseEntity<Integer> result = projectController.countNewlyCreatedProject(1);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody());
    }

    @Test
    void testGetExpiredProject_whenEmptyBuId_thenReturnResults() {
        List<ProjectModel> mockProject = new ArrayList<>();
        mockProject.add(projectModelOne);
        mockProject.add(projectModelTwo);

        when(projectService.getExpiredProject(eq(null))).thenReturn(mockProject);

        BaseResponse<List<ProjectModel>> result = projectController.getExpiredProject(null);

        assertEquals(200, result.getStatus());
        assertEquals(mockProject, result.getData());
    }

    @Test
    void testGetExpiredProject_whenEmptyResponse_thenReturnEmptyResult() {
        List<ProjectModel> mockProject = new ArrayList<>(Collections.emptyList());

        when(projectService.getExpiredProject(anyInt())).thenReturn(mockProject);

        BaseResponse<List<ProjectModel>> result = projectController.getExpiredProject(anyInt());

        assertNotNull(result);
        assertTrue(result.getData().isEmpty());
    }

    @Test
    void testGetExpiredProject_whenValidData_thenReturnSuccess() {
        List<ProjectModel> mockProject = new ArrayList<>();
        mockProject.add(projectModelOne);
        mockProject.add(projectModelTwo);

        when(projectService.getExpiredProject(anyInt())).thenReturn(mockProject);

        BaseResponse<List<ProjectModel>> result = projectController.getExpiredProject(1);

        assertEquals(200, result.getStatus());
        assertEquals(mockProject, result.getData());
    }

    @Test
    void testCountExpiredProject_whenEmptyBuId_thenReturnResults() {
        List<ProjectModel> mockProject = new ArrayList<>();
        mockProject.add(projectModelOne);
        mockProject.add(projectModelTwo);

        when(projectService.countExpiredProject(eq(null))).thenReturn(mockProject.size());

        ResponseEntity<Integer> result = projectController.countExpiredProject(null);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody());
    }

    @Test
    void testCountExpiredProject_whenValidData_thenReturnSuccess() {
        List<ProjectModel> mockProject = new ArrayList<>();
        mockProject.add(projectModelOne);
        mockProject.add(projectModelTwo);

        when(projectService.countExpiredProject(anyInt())).thenReturn(mockProject.size());

        ResponseEntity<Integer> result = projectController.countExpiredProject(1);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody());
    }

    @Test
    void testCountProjectByBuIdAndStatus_whenEmptyBuIdAndStatus_thenThrowException() {
        List<ProjectModel> mockProject = new ArrayList<>();
        mockProject.add(projectModelOne);
        mockProject.add(projectModelTwo);

        when(projectService.countProjectByBuIdAndStatus(eq(null), eq(null)))
                .thenThrow(new CoEException(ErrorConstant.CODE_BUSINESS_UNIT_ID_REQUIRED,
                        ErrorConstant.MESSAGE_BUSINESS_UNIT_ID_REQUIRED));

        Throwable throwable = Assertions.assertThrows(CoEException.class, () -> {
            projectService.countProjectByBuIdAndStatus(null, null);
        });

        Assertions.assertEquals(CoEException.class, throwable.getClass());
    }

    @Test
    void testCountProjectByBuIdAndStatus_whenEmptyBuId_thenThrowException() {
        List<ProjectModel> mockProject = new ArrayList<>();
        mockProject.add(projectModelOne);
        mockProject.add(projectModelTwo);

        when(projectService.countProjectByBuIdAndStatus(eq(null), anyInt()))
                .thenThrow(new CoEException(ErrorConstant.CODE_BUSINESS_UNIT_ID_REQUIRED,
                        ErrorConstant.MESSAGE_BUSINESS_UNIT_ID_REQUIRED));

        Throwable throwable = Assertions.assertThrows(CoEException.class, () -> {
            projectService.countProjectByBuIdAndStatus(null, 1);
        });

        Assertions.assertEquals(CoEException.class, throwable.getClass());
    }

    @Test
    void testCountProjectByBuIdAndStatus_whenEmptyStatus_thenThrowException() {
        List<ProjectModel> mockProject = new ArrayList<>();
        mockProject.add(projectModelOne);
        mockProject.add(projectModelTwo);

        when(projectService.countProjectByBuIdAndStatus(anyInt(), eq(null)))
                .thenThrow(new CoEException(ErrorConstant.CODE_PROJECT_STATUS_REQUIRED,
                        ErrorConstant.MESSAGE_PROJECT_STATUS_REQUIRED));

        Throwable throwable = Assertions.assertThrows(CoEException.class, () -> {
            projectService.countProjectByBuIdAndStatus(1, null);
        });

        Assertions.assertEquals(CoEException.class, throwable.getClass());
    }

    @Test
    void testCountProjectByBuIdAndStatus_whenValidData_thenThrowException() {
        List<ProjectModel> mockProject = new ArrayList<>();
        mockProject.add(projectModelOne);
        mockProject.add(projectModelTwo);

        when(projectService.countExpiredProject(anyInt())).thenReturn(mockProject.size());

        ResponseEntity<Integer> result = projectController.countExpiredProject(1);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody());
    }

    @Test
    void testCountProjectsForPieChart_whenEmptyBuId_thenReturnSuccess(){
        Integer buId = null;

        List<ProjectCountModel> countProject = new ArrayList<>();

        Mockito.when(projectService.countProjectsForPieChart(buId))
                .thenReturn(countProject);

        ResponseEntity<List<ProjectCountModel>> response = projectController.countProjectsForPieChart(buId);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(countProject, response.getBody());
    }

    @Test
    void testCountProjectsForPieChart_whenValidData_thenReturnSuccess(){
        Integer buId = 1;

        List<ProjectCountModel> countProject = new ArrayList<>();

        Mockito.when(projectService.countProjectsForPieChart(buId))
                .thenReturn(countProject);

        ResponseEntity<List<ProjectCountModel>> response = projectController.countProjectsForPieChart(buId);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(countProject, response.getBody());
    }
    @Test
    void testCountProjectsForPieChart_whenEmptyResponse_thenReturnEmptyResult(){
        Integer buId = 1;

        List<ProjectCountModel> countProject = Collections.emptyList();

        Mockito.when(projectService.countProjectsForPieChart(buId)).thenReturn(countProject);

        ResponseEntity<List<ProjectCountModel>> response = projectController.countProjectsForPieChart(buId);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(countProject, response.getBody());
    }
}