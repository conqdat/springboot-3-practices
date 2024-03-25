package com.hitachi.coe.fullstack.service;

import com.hitachi.coe.fullstack.constant.CommonConstant;
import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.entity.BusinessDomain;
import com.hitachi.coe.fullstack.entity.BusinessUnit;
import com.hitachi.coe.fullstack.entity.Employee;
import com.hitachi.coe.fullstack.entity.Project;
import com.hitachi.coe.fullstack.entity.ProjectStatus;
import com.hitachi.coe.fullstack.entity.ProjectTech;
import com.hitachi.coe.fullstack.entity.ProjectType;
import com.hitachi.coe.fullstack.entity.SkillSet;
import com.hitachi.coe.fullstack.exceptions.CoEException;
import com.hitachi.coe.fullstack.model.BusinessDomainModel;
import com.hitachi.coe.fullstack.model.ExcelResponseModel;
import com.hitachi.coe.fullstack.model.IProjectModel;
import com.hitachi.coe.fullstack.model.IResultOfCountProject;
import com.hitachi.coe.fullstack.model.ImportResponse;
import com.hitachi.coe.fullstack.model.ProjectCountModel;
import com.hitachi.coe.fullstack.model.ProjectImportModel;
import com.hitachi.coe.fullstack.model.ProjectModel;
import com.hitachi.coe.fullstack.model.ProjectTypeModel;
import com.hitachi.coe.fullstack.model.ProjectUpdateModel;
import com.hitachi.coe.fullstack.model.SkillSetModel;
import com.hitachi.coe.fullstack.repository.BusinessDomainRepository;
import com.hitachi.coe.fullstack.repository.BusinessUnitRepository;
import com.hitachi.coe.fullstack.repository.ProjectRepository;
import com.hitachi.coe.fullstack.repository.ProjectTypeRepository;
import com.hitachi.coe.fullstack.repository.SkillSetRepository;
import com.hitachi.coe.fullstack.transformation.BusinessDomainTransformer;
import com.hitachi.coe.fullstack.transformation.ProjectTransformer;
import com.hitachi.coe.fullstack.transformation.ProjectTypeTransformer;
import com.hitachi.coe.fullstack.transformation.SkillSetTransformer;
import com.hitachi.coe.fullstack.util.DateFormatUtils;
import com.hitachi.coe.fullstack.util.ExcelUtils;
import com.hitachi.coe.fullstack.util.JsonUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ProjectServiceTests {

    @Autowired
    ProjectService projectService;

    @MockBean
    ProjectRepository projectRepository;

    @MockBean
    BusinessDomainRepository businessDomainRepository;

    @MockBean
    BusinessUnitRepository businessUnitRepository;

    @MockBean
    ProjectTypeRepository projectTypeRepository;

    @MockBean
    ProjectTechService projectTechService;

    @MockBean
    SkillSetRepository skillSetRepository;

    @MockBean
    ProjectTransformer projectTransformer;

    @MockBean
    BusinessDomainTransformer businessDomainTransformer;

    @MockBean
    ProjectTypeTransformer projectTypeTransformer;

    @MockBean
    SkillSetTransformer skillSetTransformer;

    Employee employee;

    Project project;

    ExcelResponseModel listOfProject;

    BusinessDomain businessDomain;

    ProjectType projectType;

    BusinessDomainModel businessDomainModel;

    ProjectTypeModel projectTypeModel;

    ProjectModel projectModel;

    ProjectModel projectModelOne;

    ProjectModel projectModelTwo;

    IProjectModel iProjectModelOne;

    IProjectModel iProjectModelTwo;

    Timestamp date;

    Timestamp currentDate;

    Timestamp startDate;

    Timestamp endDate;

    SkillSet skillSet;

    SkillSet skillSet1;

    SkillSet skillSet2;

    SkillSet skillSet3;

    BusinessUnit businessUnit;

    ProjectTech projectTech;

    Project projectImport;

    SkillSetModel skillSetModel;

    ProjectUpdateModel projectUpdateModel;

    String[] stringArray;

    Project projectOne;

    Project projectTwo;

    ProjectStatus projectStatus;

    List<IResultOfCountProject> iResultOfCountProjectList;

    List<ProjectCountModel> projectCountModelList;

    List<IProjectModel> iProjectModels;

    @SneakyThrows
    @BeforeEach
    void setUp(){

        Path filePath = Paths.get("src/test/resources/files/ProjectTest.xlsx");
        byte[] content = Files.readAllBytes(filePath);
        MockMultipartFile file = new MockMultipartFile("file", "ProjectTest.xlsx", "application/vnd.ms-excel", content);
        listOfProject = ExcelUtils.readFromExcel(file.getInputStream(),
                JsonUtils.convertJsonToPojo(JsonUtils.readFileAsString("/jsonconfig/ProjectReadConfig.json")),
                ProjectImportModel.class);

        date = new Timestamp(System.currentTimeMillis());
        currentDate = new Timestamp(System.currentTimeMillis());
        startDate = DateFormatUtils.convertTimestampFromString("2023-09-12");
        endDate = DateFormatUtils.convertTimestampFromString("2023-10-20");

        businessUnit = new BusinessUnit();
        skillSet = new SkillSet();
        skillSet1 = new SkillSet();
        skillSet2 = new SkillSet();
        skillSet3 = new SkillSet();
        projectTech = new ProjectTech();
        projectImport = new Project();

        skillSet.setId(1);
        skillSet.setCode("code");
        skillSet.setName("name");
        skillSet1.setId(1);
        skillSet1.setCode("test_code");
        skillSet1.setName("C#");
        skillSet2.setName("Java");
        skillSet3.setName("Golang");

        businessUnit.setId(1);
        businessUnit.setCode("IoT");

        employee = new Employee();
        employee.setId(1);
        employee.setName("nguyenhai");
        employee.setHccId("123456");
        employee.setLdap("78910");

        businessDomain = new BusinessDomain();
        businessDomain.setId(1);
        businessDomain.setName("Transportation");

        businessDomainModel = new BusinessDomainModel();
        businessDomainModel.setId(1);

        stringArray = new String[3];
        stringArray[0] = "C#";
        stringArray[1] = "Java";
        stringArray[2] = "Golang";

        projectTypeModel = new ProjectTypeModel();
        projectTypeModel.setId(1);

        projectType = new ProjectType();
        projectType.setId(1);
        projectType.setName("Daily Rate");

        skillSet = new SkillSet();
        skillSet.setId(1);

        skillSetModel = new SkillSetModel();
        skillSetModel.setId(1);
        skillSetModel.setName("C");
        skillSetModel.setCode("C");
        skillSetModel.setDescription("C description");

        project = new Project();
        project.setId(1);
        project.setName("FSCMS");
        project.setDescription("FSCMS");
        project.setCustomerName("Nguyen Van A");
        project.setProjectManager("Nguyen Van B");
        project.setStatus(ProjectStatus.OPEN);
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setUpdated(currentDate);
        project.setUpdatedBy(CommonConstant.CREATED_BY_ADMINISTRATOR);
        project.setBusinessDomain(businessDomain);
        project.setProjectType(projectType);

        projectModel = new ProjectModel();
        projectModel.setId(1);
        projectModel.setName("FSCMS");
        projectModel.setStartDate(date);
        projectModel.setEndDate(currentDate);
        projectModel.setCode("ABC");
        projectModel.setCustomerName("Nguyen ABC");
        projectModel.setDescription("ABC");
        projectModel.setProjectManager("Nguyen ABCD");
        projectModel.setStatus(1);
        projectModel.setBusinessDomain(businessDomainModel);
        projectModel.setBusinessUnitId(businessUnit.getId());
        projectModel.setProjectType(projectTypeModel);
        projectModel.setSkillSets(List.of(skillSetModel, skillSetModel));

        projectUpdateModel = new ProjectUpdateModel();
        projectUpdateModel.setProjectId(1);
        projectUpdateModel.setName("ABC");
        projectUpdateModel.setCode("ABC");
        projectUpdateModel.setCustomerName("Nguyen ABC");
        projectUpdateModel.setDescription("ABC");
        projectUpdateModel.setStartDate("2023-07-12 00:00:00.321");
        projectUpdateModel.setEndDate("2023-09-20 00:00:00.123");
        projectUpdateModel.setProjectManager("Nguyen ABCD");
        projectUpdateModel.setStatus(1);
        projectUpdateModel.setBusinessDomainId(1);
        projectUpdateModel.setBusinessUnitId(businessUnit.getId());
        projectUpdateModel.setProjectTypeId(1);
        projectUpdateModel.setProjectsTech(List.of(1));

        iProjectModelOne = mock(IProjectModel.class);
        iProjectModelTwo = mock(IProjectModel.class);

        projectImport.setCode("ZONA-US-P");
        projectImport.setName("ZONA-US-P-Glob-Part-Gat-Integrate");
        projectImport.setCustomerName("Zonar Systems, Inc.");
        projectImport.setStatus(ProjectStatus.ONGOING);
        projectImport.setStartDate(startDate);
        projectImport.setEndDate(endDate);
        projectImport.setCreated(endDate);
        projectImport.setCreatedBy(CommonConstant.CREATED_BY_ADMINISTRATOR);
        projectImport.setProjectManager("Nam Van Nguyen");
        projectImport.setProjectType(projectType);
        projectImport.setBusinessDomain(businessDomain);
        projectImport.setBusinessUnit(businessUnit);

        projectTech.setProject(projectImport);
        projectTech.setSkillSet(skillSet1);

        projectOne = new Project();
        projectOne.setId(1);
        projectOne.setName("FSCMS");
        projectOne.setDescription("FSCMS");
        projectOne.setCustomerName("Nguyen Van A");
        projectOne.setProjectManager("Nguyen Van B");
        projectOne.setStatus(ProjectStatus.OPEN);
        projectOne.setStartDate(startDate);
        projectOne.setEndDate(endDate);
        projectOne.setUpdated(currentDate);
        projectOne.setUpdatedBy(CommonConstant.CREATED_BY_ADMINISTRATOR);
        projectOne.setBusinessDomain(businessDomain);
        projectOne.setProjectType(projectType);

        projectTwo = new Project();
        projectTwo.setId(1);
        projectTwo.setName("FSCMS");
        projectTwo.setDescription("FSCMS");
        projectTwo.setCustomerName("Nguyen Van A");
        projectTwo.setProjectManager("Nguyen Van B");
        projectTwo.setStatus(ProjectStatus.CLOSE);
        projectTwo.setStartDate(startDate);
        projectTwo.setEndDate(endDate);
        projectTwo.setUpdated(currentDate);
        projectTwo.setUpdatedBy(CommonConstant.CREATED_BY_ADMINISTRATOR);
        projectTwo.setBusinessDomain(businessDomain);
        projectTwo.setProjectType(projectType);

        projectModelOne = new ProjectModel();
        projectModelOne.setId(1);
        projectModelOne.setName("FSCMS");
        projectModelOne.setDescription("FSCMS");
        projectModelOne.setCustomerName("Nguyen Van A");
        projectModelOne.setProjectManager("Nguyen Van B");
        projectModelOne.setStatus(1);
        projectModelOne.setStartDate(startDate);
        projectModelOne.setEndDate(endDate);
        projectModelOne.setUpdated(currentDate);
        projectModelOne.setUpdatedBy(CommonConstant.CREATED_BY_ADMINISTRATOR);
        projectModelOne.setBusinessDomain(businessDomainModel);
        projectModelOne.setProjectType(projectTypeModel);

        projectModelTwo = new ProjectModel();
        projectModelTwo.setId(1);
        projectModelTwo.setName("FSCMS");
        projectModelTwo.setDescription("FSCMS");
        projectModelTwo.setCustomerName("Nguyen Van A");
        projectModelTwo.setProjectManager("Nguyen Van B");
        projectModelTwo.setStatus(3);
        projectModelTwo.setStartDate(startDate);
        projectModelTwo.setEndDate(endDate);
        projectModelTwo.setUpdated(currentDate);
        projectModelTwo.setUpdatedBy(CommonConstant.CREATED_BY_ADMINISTRATOR);
        projectModelTwo.setBusinessDomain(businessDomainModel);
        projectModelTwo.setProjectType(projectTypeModel);

        iResultOfCountProjectList = new ArrayList<>();
        IResultOfCountProject iResultOfCountProject = mock(IResultOfCountProject.class);
        iResultOfCountProjectList.add(iResultOfCountProject);

        projectCountModelList = new ArrayList<>();
        ProjectCountModel projectCountModel = mock(ProjectCountModel.class);
        projectCountModelList.add(projectCountModel);

        iProjectModels = new ArrayList<>();
        IProjectModel projectModel = mock(IProjectModel.class);
        iProjectModels.add(projectModel);
    }

    @Test
    @SneakyThrows
    public void testImportInsertProjectExcel_whenValidData_thenSuccess() {

        when(businessDomainRepository.findAll()).thenReturn(List.of(businessDomain));
        when(businessUnitRepository.findAll()).thenReturn(List.of(businessUnit));
        when(projectTypeRepository.findAll()).thenReturn(List.of(projectType));

//        when(skillSetRepository.findAll()).thenReturn(List.of(skillSet1,skillSet2,skillSet3));
        when(projectRepository.findByCode(any(String.class))).thenReturn(null);
        when(projectRepository.save(any(Project.class))).thenReturn(projectImport);

        doNothing().when(projectTechService).addProjectListSkill(projectImport, stringArray);
        ImportResponse importEmployee = projectService.importProject(listOfProject);

        //Verify
        assertNotNull(importEmployee);
        assertEquals(1, importEmployee.getTotalRows());
        assertEquals(1, importEmployee.getSuccessRows());
        assertEquals(0, importEmployee.getErrorRows());
    }

    @Test
    @SneakyThrows
    public void testImportInsertProjectExcel_whenInvalidBusinessDomain_thenSuccess() {

        when(businessDomainRepository.findAll()).thenReturn(Collections.emptyList());
        when(businessUnitRepository.findAll()).thenReturn(List.of(businessUnit));
        when(projectTypeRepository.findAll()).thenReturn(List.of(projectType));

//        when(skillSetRepository.findAll()).thenReturn(List.of(skillSet1,skillSet2,skillSet3));
        when(projectRepository.findByCode(any(String.class))).thenReturn(null);
        when(projectRepository.save(any(Project.class))).thenReturn(projectImport);

        doNothing().when(projectTechService).addProjectListSkill(projectImport, stringArray);
        ImportResponse importEmployee = projectService.importProject(listOfProject);

        //Verify
        assertNotNull(importEmployee);
        assertEquals(1, importEmployee.getTotalRows());
        assertEquals(0, importEmployee.getSuccessRows());
        assertEquals(1, importEmployee.getErrorRows());
    }

    @Test
    @SneakyThrows
    public void testImportInsertProjectExcel_whenInvalidBusinessUnit_thenSuccess() {

        when(businessDomainRepository.findAll()).thenReturn(List.of(businessDomain));
        when(businessUnitRepository.findAll()).thenReturn(Collections.emptyList());
        when(projectTypeRepository.findAll()).thenReturn(List.of(projectType));

//        when(skillSetRepository.findAll()).thenReturn(List.of(skillSet1,skillSet2,skillSet3));
        when(projectRepository.findByCode(any(String.class))).thenReturn(null);
        when(projectRepository.save(any(Project.class))).thenReturn(projectImport);

        doNothing().when(projectTechService).addProjectListSkill(projectImport, stringArray);
        ImportResponse importEmployee = projectService.importProject(listOfProject);

        //Verify
        assertNotNull(importEmployee);
        assertEquals(1, importEmployee.getTotalRows());
        assertEquals(0, importEmployee.getSuccessRows());
        assertEquals(1, importEmployee.getErrorRows());
    }

    @Test
    @SneakyThrows
    public void testImportInsertProjectExcel_whenInvalidProjectType_thenSuccess() {

        when(businessDomainRepository.findAll()).thenReturn(List.of(businessDomain));
        when(businessUnitRepository.findAll()).thenReturn(List.of(businessUnit));
        when(projectTypeRepository.findAll()).thenReturn(Collections.emptyList());

//        when(skillSetRepository.findAll()).thenReturn(List.of(skillSet1,skillSet2,skillSet3));
        when(projectRepository.findByCode(any(String.class))).thenReturn(null);
        when(projectRepository.save(any(Project.class))).thenReturn(projectImport);

        doNothing().when(projectTechService).addProjectListSkill(projectImport, stringArray);
        ImportResponse importEmployee = projectService.importProject(listOfProject);

        //Verify
        assertNotNull(importEmployee);
        assertEquals(1, importEmployee.getTotalRows());
        assertEquals(0, importEmployee.getSuccessRows());
        assertEquals(1, importEmployee.getErrorRows());
    }

    @Test
    public void testGetProjectDetail_whenValidData_thenSuccess(){

        when(iProjectModelOne.getProjectId()).thenReturn(1);
        when(iProjectModelOne.getCode()).thenReturn("1");
        when(iProjectModelOne.getProjectName()).thenReturn("OnSemi");
        when(iProjectModelOne.getProjectTypeId()).thenReturn(1);
        when(iProjectModelOne.getStartDate()).thenReturn(date);
        when(iProjectModelOne.getEndDate()).thenReturn(date);
        when(iProjectModelOne.getProjectManager()).thenReturn("Pm A");
        when(iProjectModelOne.getStatus()).thenReturn(1);
        when(iProjectModelOne.getBusinessDomainId()).thenReturn(1);
        when(iProjectModelOne.getSkillSetId()).thenReturn(1);
        when(iProjectModelOne.getDescription()).thenReturn("A software development project for the finance domain");
        when(iProjectModelOne.getCustomerName()).thenReturn("Nguyen A");

        when(iProjectModelTwo.getProjectId()).thenReturn(1);
        when(iProjectModelTwo.getCode()).thenReturn("1");
        when(iProjectModelTwo.getProjectName()).thenReturn("OnSemi");
        when(iProjectModelTwo.getProjectTypeId()).thenReturn(1);
        when(iProjectModelTwo.getStartDate()).thenReturn(date);
        when(iProjectModelTwo.getEndDate()).thenReturn(date);
        when(iProjectModelTwo.getProjectManager()).thenReturn("Pm A");
        when(iProjectModelTwo.getStatus()).thenReturn(1);
        when(iProjectModelTwo.getBusinessDomainId()).thenReturn(1);
        when(iProjectModelTwo.getSkillSetId()).thenReturn(1);
        when(iProjectModelTwo.getDescription()).thenReturn("A software development project for the finance domain");
        when(iProjectModelTwo.getCustomerName()).thenReturn("Nguyen A");

        List<IProjectModel> iProjectModels = Arrays.asList(iProjectModelOne,iProjectModelTwo);

        when(projectRepository.getProjectDetailById(any(Integer.class))).thenReturn(iProjectModels);
        when(projectTypeRepository.findById(any(Integer.class))).thenReturn(Optional.of(projectType));
        when(businessDomainRepository.findById(any(Integer.class))).thenReturn(Optional.of(businessDomain));
        when(businessDomainTransformer.apply(any(BusinessDomain.class))).thenReturn(businessDomainModel);
        when(skillSetRepository.findById(any(Integer.class))).thenReturn(Optional.of(skillSet));
        when(skillSetTransformer.apply(any(SkillSet.class))).thenReturn(skillSetModel);
        when(projectTypeTransformer.apply(any(ProjectType.class))).thenReturn(projectTypeModel);

        ProjectModel projectListActual = projectService.getProjectDetailById(1);

        //Verify
        assertNotNull(projectListActual);
        assertEquals(iProjectModels.get(0).getProjectId(), projectListActual.getId());
        assertEquals(iProjectModels.get(0).getCode(), projectListActual.getCode());
        assertEquals(iProjectModels.get(0).getProjectName(), projectListActual.getName());
        assertEquals(iProjectModels.get(0).getProjectTypeId(), projectListActual.getProjectType().getId());
        assertEquals(iProjectModels.get(0).getSkillSetId(), projectListActual.getSkillSets().get(0).getId());
        assertEquals(iProjectModels.get(0).getBusinessDomainId(), projectListActual.getBusinessDomain().getId());
        assertEquals(iProjectModels.get(0).getProjectManager(), projectListActual.getProjectManager());
        assertEquals(iProjectModels.get(0).getDescription(), projectListActual.getDescription());
        assertEquals(iProjectModels.get(0).getStartDate(), projectListActual.getStartDate());
        assertEquals(iProjectModels.get(0).getEndDate(), projectListActual.getEndDate());
        assertEquals(iProjectModels.get(0).getStatus(), projectListActual.getStatus());
        assertEquals(iProjectModels.get(0).getCustomerName(), projectListActual.getCustomerName());

        assertEquals(iProjectModels.get(1).getProjectId(), projectListActual.getId());
        assertEquals(iProjectModels.get(1).getCode(), projectListActual.getCode());
        assertEquals(iProjectModels.get(1).getProjectName(), projectListActual.getName());
        assertEquals(iProjectModels.get(1).getProjectTypeId(), projectListActual.getProjectType().getId());
        assertEquals(iProjectModels.get(1).getSkillSetId(), projectListActual.getSkillSets().get(1).getId());
        assertEquals(iProjectModels.get(1).getBusinessDomainId(), projectListActual.getBusinessDomain().getId());
        assertEquals(iProjectModels.get(1).getProjectManager(), projectListActual.getProjectManager());
        assertEquals(iProjectModels.get(1).getDescription(), projectListActual.getDescription());
        assertEquals(iProjectModels.get(1).getStartDate(), projectListActual.getStartDate());
        assertEquals(iProjectModels.get(1).getEndDate(), projectListActual.getEndDate());
        assertEquals(iProjectModels.get(1).getStatus(), projectListActual.getStatus());
        assertEquals(iProjectModels.get(1).getCustomerName(), projectListActual.getCustomerName());


    }

    @Test
    public void testGetProjectDetail_whenBusinessDomainNotFound_thenThrowCoEException(){

        when(iProjectModelOne.getProjectId()).thenReturn(1);
        when(iProjectModelOne.getCode()).thenReturn("1");
        when(iProjectModelOne.getProjectName()).thenReturn("OnSemi");
        when(iProjectModelOne.getProjectTypeId()).thenReturn(1);
        when(iProjectModelOne.getStartDate()).thenReturn(date);
        when(iProjectModelOne.getEndDate()).thenReturn(date);
        when(iProjectModelOne.getProjectManager()).thenReturn("Pm A");
        when(iProjectModelOne.getStatus()).thenReturn(1);
        when(iProjectModelOne.getBusinessDomainId()).thenReturn(1);
        when(iProjectModelOne.getSkillSetId()).thenReturn(1);
        when(iProjectModelOne.getDescription()).thenReturn("A software development project for the finance domain");
        when(iProjectModelOne.getCustomerName()).thenReturn("Nguyen A");

        when(iProjectModelTwo.getProjectId()).thenReturn(1);
        when(iProjectModelTwo.getCode()).thenReturn("1");
        when(iProjectModelTwo.getProjectName()).thenReturn("OnSemi");
        when(iProjectModelTwo.getProjectTypeId()).thenReturn(1);
        when(iProjectModelTwo.getStartDate()).thenReturn(date);
        when(iProjectModelTwo.getEndDate()).thenReturn(date);
        when(iProjectModelTwo.getProjectManager()).thenReturn("Pm A");
        when(iProjectModelTwo.getStatus()).thenReturn(1);
        when(iProjectModelTwo.getBusinessDomainId()).thenReturn(1);
        when(iProjectModelTwo.getSkillSetId()).thenReturn(1);
        when(iProjectModelTwo.getDescription()).thenReturn("A software development project for the finance domain");
        when(iProjectModelTwo.getCustomerName()).thenReturn("Nguyen A");

        List<IProjectModel> iProjectModels = Arrays.asList(iProjectModelOne,iProjectModelTwo);

        when(projectRepository.getProjectDetailById(any(Integer.class))).thenReturn(iProjectModels);
        when(projectTypeRepository.findById(any(Integer.class))).thenReturn(Optional.of(projectType));
        when(businessDomainRepository.findById(any(Integer.class))).thenReturn(Optional.empty());


        Throwable throwable = assertThrows(CoEException.class,
                () -> projectService.getProjectDetailById(1));

        //Verify
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_BUSINESS_DOMAIN_DO_NOT_EXIST, throwable.getMessage());

    }

    @Test
    public void testGetProjectDetail_whenBusinessDomainIsNull_thenSuccess(){

        when(iProjectModelOne.getProjectId()).thenReturn(1);
        when(iProjectModelOne.getCode()).thenReturn("1");
        when(iProjectModelOne.getProjectName()).thenReturn("OnSemi");
        when(iProjectModelOne.getProjectTypeId()).thenReturn(1);
        when(iProjectModelOne.getStartDate()).thenReturn(date);
        when(iProjectModelOne.getEndDate()).thenReturn(date);
        when(iProjectModelOne.getProjectManager()).thenReturn("Pm A");
        when(iProjectModelOne.getStatus()).thenReturn(1);
        when(iProjectModelOne.getBusinessDomainId()).thenReturn(null);
        when(iProjectModelOne.getSkillSetId()).thenReturn(1);
        when(iProjectModelOne.getDescription()).thenReturn("A software development project for the finance domain");
        when(iProjectModelOne.getCustomerName()).thenReturn("Nguyen A");

        when(iProjectModelTwo.getProjectId()).thenReturn(1);
        when(iProjectModelTwo.getCode()).thenReturn("1");
        when(iProjectModelTwo.getProjectName()).thenReturn("OnSemi");
        when(iProjectModelTwo.getProjectTypeId()).thenReturn(1);
        when(iProjectModelTwo.getStartDate()).thenReturn(date);
        when(iProjectModelTwo.getEndDate()).thenReturn(date);
        when(iProjectModelTwo.getProjectManager()).thenReturn("Pm A");
        when(iProjectModelTwo.getStatus()).thenReturn(1);
        when(iProjectModelTwo.getBusinessDomainId()).thenReturn(null);
        when(iProjectModelTwo.getSkillSetId()).thenReturn(1);
        when(iProjectModelTwo.getDescription()).thenReturn("A software development project for the finance domain");
        when(iProjectModelTwo.getCustomerName()).thenReturn("Nguyen A");

        List<IProjectModel> iProjectModels = Arrays.asList(iProjectModelOne,iProjectModelTwo);

        when(projectRepository.getProjectDetailById(any(Integer.class))).thenReturn(iProjectModels);
        when(projectTypeRepository.findById(any(Integer.class))).thenReturn(Optional.of(projectType));
        when(businessDomainRepository.findById(any(Integer.class))).thenReturn(Optional.of(businessDomain));
        when(businessDomainTransformer.apply(any(BusinessDomain.class))).thenReturn(businessDomainModel);
        when(skillSetRepository.findById(any(Integer.class))).thenReturn(Optional.of(skillSet));
        when(skillSetTransformer.apply(any(SkillSet.class))).thenReturn(skillSetModel);
        when(projectTypeTransformer.apply(any(ProjectType.class))).thenReturn(projectTypeModel);

        ProjectModel projectListActual = projectService.getProjectDetailById(1);

        //Verify
        assertNotNull(projectListActual);
        assertEquals(iProjectModels.get(0).getProjectId(), projectListActual.getId());
        assertEquals(iProjectModels.get(0).getCode(), projectListActual.getCode());
        assertEquals(iProjectModels.get(0).getProjectName(), projectListActual.getName());
        assertEquals(iProjectModels.get(0).getProjectTypeId(), projectListActual.getProjectType().getId());
        assertEquals(iProjectModels.get(0).getSkillSetId(), projectListActual.getSkillSets().get(0).getId());
        assertEquals(iProjectModels.get(0).getBusinessDomainId(), projectListActual.getBusinessDomain().getId());
        assertEquals(iProjectModels.get(0).getProjectManager(), projectListActual.getProjectManager());
        assertEquals(iProjectModels.get(0).getDescription(), projectListActual.getDescription());
        assertEquals(iProjectModels.get(0).getStartDate(), projectListActual.getStartDate());
        assertEquals(iProjectModels.get(0).getEndDate(), projectListActual.getEndDate());
        assertEquals(iProjectModels.get(0).getStatus(), projectListActual.getStatus());
        assertEquals(iProjectModels.get(0).getCustomerName(), projectListActual.getCustomerName());

        assertEquals(iProjectModels.get(1).getProjectId(), projectListActual.getId());
        assertEquals(iProjectModels.get(1).getCode(), projectListActual.getCode());
        assertEquals(iProjectModels.get(1).getProjectName(), projectListActual.getName());
        assertEquals(iProjectModels.get(1).getProjectTypeId(), projectListActual.getProjectType().getId());
        assertEquals(iProjectModels.get(1).getSkillSetId(), projectListActual.getSkillSets().get(1).getId());
        assertEquals(iProjectModels.get(1).getBusinessDomainId(), projectListActual.getBusinessDomain().getId());
        assertEquals(iProjectModels.get(1).getProjectManager(), projectListActual.getProjectManager());
        assertEquals(iProjectModels.get(1).getDescription(), projectListActual.getDescription());
        assertEquals(iProjectModels.get(1).getStartDate(), projectListActual.getStartDate());
        assertEquals(iProjectModels.get(1).getEndDate(), projectListActual.getEndDate());
        assertEquals(iProjectModels.get(1).getStatus(), projectListActual.getStatus());
        assertEquals(iProjectModels.get(1).getCustomerName(), projectListActual.getCustomerName());

    }

    @Test
    public void testGetProjectDetail_whenProjectTypeNotFound_thenThrowCoEException(){

        when(iProjectModelOne.getProjectId()).thenReturn(1);
        when(iProjectModelOne.getCode()).thenReturn("1");
        when(iProjectModelOne.getProjectName()).thenReturn("OnSemi");
        when(iProjectModelOne.getProjectTypeId()).thenReturn(1);
        when(iProjectModelOne.getStartDate()).thenReturn(date);
        when(iProjectModelOne.getEndDate()).thenReturn(date);
        when(iProjectModelOne.getProjectManager()).thenReturn("Pm A");
        when(iProjectModelOne.getStatus()).thenReturn(1);
        when(iProjectModelOne.getBusinessDomainId()).thenReturn(1);
        when(iProjectModelOne.getSkillSetId()).thenReturn(1);
        when(iProjectModelOne.getDescription()).thenReturn("A software development project for the finance domain");
        when(iProjectModelOne.getCustomerName()).thenReturn("Nguyen A");

        when(iProjectModelTwo.getProjectId()).thenReturn(1);
        when(iProjectModelTwo.getCode()).thenReturn("1");
        when(iProjectModelTwo.getProjectName()).thenReturn("OnSemi");
        when(iProjectModelTwo.getProjectTypeId()).thenReturn(1);
        when(iProjectModelTwo.getStartDate()).thenReturn(date);
        when(iProjectModelTwo.getEndDate()).thenReturn(date);
        when(iProjectModelTwo.getProjectManager()).thenReturn("Pm A");
        when(iProjectModelTwo.getStatus()).thenReturn(1);
        when(iProjectModelTwo.getBusinessDomainId()).thenReturn(1);
        when(iProjectModelTwo.getSkillSetId()).thenReturn(1);
        when(iProjectModelTwo.getDescription()).thenReturn("A software development project for the finance domain");
        when(iProjectModelTwo.getCustomerName()).thenReturn("Nguyen A");

        List<IProjectModel> iProjectModels = Arrays.asList(iProjectModelOne,iProjectModelTwo);

        when(projectRepository.getProjectDetailById(any(Integer.class))).thenReturn(iProjectModels);
        when(projectTypeRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        when(businessDomainRepository.findById(any(Integer.class))).thenReturn(Optional.of(businessDomain));


        Throwable throwable = assertThrows(CoEException.class,
                () -> projectService.getProjectDetailById(1));

        //Verify
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_PROJECT_TYPE_DO_NOT_EXIST, throwable.getMessage());

    }

    @Test
    public void testGetProjectDetail_whenSKillSetNotFound_thenThrowCoEException(){

        when(iProjectModelOne.getProjectId()).thenReturn(1);
        when(iProjectModelOne.getCode()).thenReturn("1");
        when(iProjectModelOne.getProjectName()).thenReturn("OnSemi");
        when(iProjectModelOne.getProjectTypeId()).thenReturn(1);
        when(iProjectModelOne.getStartDate()).thenReturn(date);
        when(iProjectModelOne.getEndDate()).thenReturn(date);
        when(iProjectModelOne.getProjectManager()).thenReturn("Pm A");
        when(iProjectModelOne.getStatus()).thenReturn(1);
        when(iProjectModelOne.getBusinessDomainId()).thenReturn(1);
        when(iProjectModelOne.getSkillSetId()).thenReturn(1);
        when(iProjectModelOne.getDescription()).thenReturn("A software development project for the finance domain");
        when(iProjectModelOne.getCustomerName()).thenReturn("Nguyen A");

        when(iProjectModelTwo.getProjectId()).thenReturn(1);
        when(iProjectModelTwo.getCode()).thenReturn("1");
        when(iProjectModelTwo.getProjectName()).thenReturn("OnSemi");
        when(iProjectModelTwo.getProjectTypeId()).thenReturn(1);
        when(iProjectModelTwo.getStartDate()).thenReturn(date);
        when(iProjectModelTwo.getEndDate()).thenReturn(date);
        when(iProjectModelTwo.getProjectManager()).thenReturn("Pm A");
        when(iProjectModelTwo.getStatus()).thenReturn(1);
        when(iProjectModelTwo.getBusinessDomainId()).thenReturn(1);
        when(iProjectModelTwo.getSkillSetId()).thenReturn(1);
        when(iProjectModelTwo.getDescription()).thenReturn("A software development project for the finance domain");
        when(iProjectModelTwo.getCustomerName()).thenReturn("Nguyen A");

        List<IProjectModel> iProjectModels = Arrays.asList(iProjectModelOne,iProjectModelTwo);

        when(projectRepository.getProjectDetailById(any(Integer.class))).thenReturn(iProjectModels);
        when(projectTypeRepository.findById(any(Integer.class))).thenReturn(Optional.of(projectType));
        when(businessDomainRepository.findById(any(Integer.class))).thenReturn(Optional.of(businessDomain));
        when(businessDomainTransformer.apply(any(BusinessDomain.class))).thenReturn(businessDomainModel);
        when(skillSetRepository.findById(any(Integer.class))).thenReturn(Optional.empty());


        Throwable throwable = assertThrows(CoEException.class,
                () -> projectService.getProjectDetailById(1));

        //Verify
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_SKILL_SET_DO_NOT_EXIST, throwable.getMessage());

    }

    @Test
    public void testGetProjectDetail_whenIsNull_thenThrowCoEException(){

        Throwable throwable = assertThrows(CoEException.class,
                () -> projectService.getProjectDetailById(1));

        //Verify
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_PROJECT_NOT_FOUND, throwable.getMessage());
    }

    @Test
    public void testUpdate_whenValidData_thenSuccess(){

        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.of(project));
        when(businessDomainRepository.findById(any(Integer.class))).thenReturn(Optional.of(businessDomain));
        when(projectTypeRepository.findById(any(Integer.class))).thenReturn(Optional.of(projectType));
        when(projectRepository.findByCode(any(String.class))).thenReturn(project);
        doNothing().when(projectTechService).deleteProjectTechByProject(any(Integer.class));
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(skillSetRepository.findById(any(Integer.class))).thenReturn(Optional.of(skillSet));
        doNothing().when(projectTechService).saveProjectTech(any(ProjectTech.class));
        when(projectTransformer.apply(any(Project.class))).thenReturn(projectModel);
        when(businessUnitRepository.findById(anyInt())).thenReturn(Optional.ofNullable(businessUnit));

        ProjectModel projectModelActual = projectService.update(projectUpdateModel);

        assertNotNull(projectModelActual);
        assertEquals(projectModel.getSkillSets().size(), projectModelActual.getSkillSets().size());
        assertEquals(projectModel.getCode(), projectModelActual.getCode());
        assertEquals(projectModel.getName(), projectModelActual.getName());
        assertEquals(projectModel.getDescription(), projectModelActual.getDescription());
        assertEquals(projectModel.getCustomerName(), projectModelActual.getCustomerName());
        assertEquals(projectModel.getProjectManager(), projectModelActual.getProjectManager());
        assertEquals(projectModel.getStatus(), projectModelActual.getStatus());
        assertEquals(projectModel.getStartDate(), projectModelActual.getStartDate());
        assertEquals(projectModel.getEndDate(), projectModelActual.getEndDate());
        assertEquals(projectModel.getUpdated(), projectModelActual.getUpdated());
        assertEquals(projectModel.getUpdatedBy(), projectModelActual.getUpdatedBy());
        assertEquals(projectModel.getBusinessDomain(), projectModelActual.getBusinessDomain());
        assertEquals(projectModel.getProjectType(), projectModelActual.getProjectType());
    }

    @Test
    public void testUpdate_whenProjectNotFound_thenThrowCoEException(){

        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        when(businessDomainRepository.findById(any(Integer.class))).thenReturn(Optional.of(businessDomain));
        when(projectTypeRepository.findById(any(Integer.class))).thenReturn(Optional.of(projectType));
        when(projectRepository.findByCode(any(String.class))).thenReturn(project);


        Throwable throwable = assertThrows(CoEException.class,
                () -> projectService.update(projectUpdateModel));

        //Verify
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_PROJECT_NOT_FOUND, throwable.getMessage());

    }

    @Test
    public void testUpdate_whenEndDateBeforeStartDate_thenThrowCoEException(){

        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.of(project));
        when(businessDomainRepository.findById(any(Integer.class))).thenReturn(Optional.of(businessDomain));
        when(projectTypeRepository.findById(any(Integer.class))).thenReturn(Optional.of(projectType));
        when(projectRepository.findByCode(any(String.class))).thenReturn(project);

        projectUpdateModel.setStartDate("2023-07-12 00:00:00.321");
        projectUpdateModel.setEndDate("2023-07-11 00:00:00.123");


        Throwable throwable = assertThrows(CoEException.class,
                () -> projectService.update(projectUpdateModel));

        //Verify
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_INVALID_START_DATE_END_DATE, throwable.getMessage());

    }

    @Test
    public void testUpdate_whenProjectTypeNotFound_thenThrowCoEException(){

        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.of(project));
        when(businessDomainRepository.findById(any(Integer.class))).thenReturn(Optional.of(businessDomain));
        when(projectTypeRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        when(projectRepository.findByCode(any(String.class))).thenReturn(project);


        Throwable throwable = assertThrows(CoEException.class,
                () -> projectService.update(projectUpdateModel));

        //Verify
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_PROJECT_TYPE_DO_NOT_EXIST, throwable.getMessage());

    }

    @Test
    public void testUpdate_whenBusinessDomainNotFound_thenThrowCoEException(){

        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.of(project));
        when(businessDomainRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        when(projectTypeRepository.findById(any(Integer.class))).thenReturn(Optional.of(projectType));
        when(projectRepository.findByCode(any(String.class))).thenReturn(project);


        Throwable throwable = assertThrows(CoEException.class,
                () -> projectService.update(projectUpdateModel));

        //Verify
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_BUSINESS_DOMAIN_DO_NOT_EXIST, throwable.getMessage());

    }

    @Test
    public void testUpdate_whenSkillSetNotFound_thenThrowCoEException(){

        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.of(project));
        when(businessDomainRepository.findById(any(Integer.class))).thenReturn(Optional.of(businessDomain));
        when(projectTypeRepository.findById(any(Integer.class))).thenReturn(Optional.of(projectType));
        when(projectRepository.findByCode(any(String.class))).thenReturn(project);
        doNothing().when(projectTechService).deleteProjectTechByProject(any(Integer.class));
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(skillSetRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        when(businessUnitRepository.findById(projectModel.getBusinessUnitId())).thenReturn(Optional.ofNullable(businessUnit));
        when(skillSetRepository.findById(anyInt())).thenReturn(Optional.empty());

        Throwable throwable = assertThrows(CoEException.class,
                () -> projectService.update(projectUpdateModel));

        //Verify
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_SKILL_SET_DO_NOT_EXIST, throwable.getMessage());

    }

    @Test
    public void testUpdate_whenDuplicateProjectCode_thenThrowCoEException(){

        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.of(project));
        when(businessDomainRepository.findById(any(Integer.class))).thenReturn(Optional.of(businessDomain));
        when(projectTypeRepository.findById(any(Integer.class))).thenReturn(Optional.of(projectType));
        when(projectRepository.findByCode(any(String.class))).thenReturn(null);
        doNothing().when(projectTechService).deleteProjectTechByProject(any(Integer.class));
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(skillSetRepository.findById(any(Integer.class))).thenReturn(Optional.of(skillSet));
        doNothing().when(projectTechService).saveProjectTech(any(ProjectTech.class));
        when(projectTransformer.apply(any(Project.class))).thenReturn(projectModel);
        when(businessUnitRepository.findById(anyInt())).thenReturn(Optional.ofNullable(businessUnit));

        ProjectModel projectModelActual = projectService.update(projectUpdateModel);

        assertNotNull(projectModelActual);
        assertEquals(projectModel.getSkillSets().size(), projectModelActual.getSkillSets().size());
        assertEquals(projectModel.getCode(), projectModelActual.getCode());
        assertEquals(projectModel.getName(), projectModelActual.getName());
        assertEquals(projectModel.getDescription(), projectModelActual.getDescription());
        assertEquals(projectModel.getCustomerName(), projectModelActual.getCustomerName());
        assertEquals(projectModel.getProjectManager(), projectModelActual.getProjectManager());
        assertEquals(projectModel.getStatus(), projectModelActual.getStatus());
        assertEquals(projectModel.getStartDate(), projectModelActual.getStartDate());
        assertEquals(projectModel.getEndDate(), projectModelActual.getEndDate());
        assertEquals(projectModel.getUpdated(), projectModelActual.getUpdated());
        assertEquals(projectModel.getUpdatedBy(), projectModelActual.getUpdatedBy());
        assertEquals(projectModel.getBusinessDomain(), projectModelActual.getBusinessDomain());
        assertEquals(projectModel.getProjectType(), projectModelActual.getProjectType());
    }

    @Test
    public void testUpdateProjectStatus_whenValidData_thenSuccess(){

        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.of(project));
        when(projectRepository.getProjectDetailById(any(Integer.class))).thenReturn(iProjectModels);
        when(skillSetRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(skillSet1));
        when(skillSetTransformer.apply(any(SkillSet.class))).thenReturn(skillSetModel);
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(projectTransformer.apply(any(Project.class))).thenReturn(projectModel);

        ProjectModel projectModelActual = projectService.updateProjectStatus(1,1);

        assertNotNull(projectModelActual);
        assertEquals(projectModel.getStatus(), projectModelActual.getStatus());
        assertEquals(projectModel.getUpdated(), projectModelActual.getUpdated());
        assertEquals(projectModel.getUpdatedBy(), projectModelActual.getUpdatedBy());

    }

    @Test
    public void testUpdateProjectStatus_whenProjectNotFound_thenThrowCoEException(){

        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Throwable throwable = assertThrows(CoEException.class,
                () -> projectService.updateProjectStatus(1,1));

        //Verify
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_PROJECT_NOT_FOUND, throwable.getMessage());

    }

    @Test
    public void testCloseProject_whenValidData_thenSuccess(){

        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.of(project));
        when(projectRepository.getProjectDetailById(any(Integer.class))).thenReturn(iProjectModels);
        when(skillSetRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(skillSet1));
        when(skillSetTransformer.apply(any(SkillSet.class))).thenReturn(skillSetModel);
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(projectTransformer.apply(any(Project.class))).thenReturn(projectModel);

        ProjectModel projectModelActual = projectService.closeProject(1);

        assertNotNull(projectModelActual);
        assertEquals(projectModel.getSkillSets().size(), projectModelActual.getSkillSets().size());
        assertEquals(projectModel.getCode(), projectModelActual.getCode());
        assertEquals(projectModel.getName(), projectModelActual.getName());
        assertEquals(projectModel.getDescription(), projectModelActual.getDescription());
        assertEquals(projectModel.getCustomerName(), projectModelActual.getCustomerName());
        assertEquals(projectModel.getProjectManager(), projectModelActual.getProjectManager());
        assertEquals(projectModel.getStatus(), projectModelActual.getStatus());
        assertEquals(projectModel.getStartDate(), projectModelActual.getStartDate());
        assertEquals(projectModel.getEndDate(), projectModelActual.getEndDate());
        assertEquals(projectModel.getUpdated(), projectModelActual.getUpdated());
        assertEquals(projectModel.getUpdatedBy(), projectModelActual.getUpdatedBy());
        assertEquals(projectModel.getBusinessDomain(), projectModelActual.getBusinessDomain());
        assertEquals(projectModel.getProjectType(), projectModelActual.getProjectType());
        assertEquals(projectModel.getSkillSets().get(0).getCode(), projectModelActual.getSkillSets().get(0).getCode());
        assertEquals(projectModel.getSkillSets().get(0).getDescription(), projectModelActual.getSkillSets().get(0).getDescription());
        assertEquals(projectModel.getSkillSets().get(0).getName(), projectModelActual.getSkillSets().get(0).getName());

    }

    @Test
    public void testCloseProject_whenProjectNotFound_thenThrowCoEException(){

        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Throwable throwable = assertThrows(CoEException.class,
                () -> projectService.closeProject(1));

        //Verify
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_PROJECT_NOT_FOUND, throwable.getMessage());

    }

    @Test
    public void testCloseProject_whenProjectAlreadyClose_thenThrowCoEException(){

        project.setStatus(ProjectStatus.CLOSE);
        when(projectRepository.findById(any(Integer.class))).thenReturn(Optional.of(project));

        Throwable throwable = assertThrows(CoEException.class,
                () -> projectService.closeProject(1));

        //Verify
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals(ErrorConstant.MESSAGE_PROJECT_ALREADY_CLOSE, throwable.getMessage());

    }

    @Test
    public void testGetNewlyCreatedProject_whenValidData_thenReturnSuccess(){
        List<Project> projects = Arrays.asList(projectOne, projectTwo);

        when(projectRepository.getNewlyCreatedProject(anyInt())).thenReturn(projects);
        when(projectTransformer.applyList(projects)).thenReturn(Arrays.asList(projectModelOne, projectModelTwo));

        List<ProjectModel> projectModels = projectService.getNewlyCreatedProject(1);

        assertNotNull(projectModels);
        assertEquals(2, projectModels.size());
    }

    @Test
    public void testGetNewlyCreatedProject_whenEmptyBuId_thenReturnSucess(){
        List<Project> projects = Arrays.asList(projectOne, projectTwo);

        when(projectRepository.getNewlyCreatedProject(eq(null))).thenReturn(projects);

        List<ProjectModel> projectModels = projectService.getNewlyCreatedProject(null);

        assertNotNull(projectModels);
    }

    @Test
    void testGetNewlyCreatedProject_whenEmptyResponse_thenReturnEmptyResult() {
        List<Project> projects = new ArrayList<>(Collections.emptyList());

        when(projectRepository.getNewlyCreatedProject(anyInt())).thenReturn(projects);

        List<ProjectModel> projectModels = projectService.getNewlyCreatedProject(1);

        assertNotNull(projectModels);
        assertTrue(projectModels.isEmpty());
    }

    @Test
    public void testCountNewlyCreatedProject_whenValidData_thenReturnSuccess(){
        List<Project> projects = Arrays.asList(projectOne, projectTwo);

        when(projectRepository.getNewlyCreatedProject(anyInt())).thenReturn(projects);
        when(projectTransformer.applyList(projects)).thenReturn(Arrays.asList(projectModelOne, projectModelTwo));

        List<ProjectModel> projectModels = projectService.getNewlyCreatedProject(1);

        assertNotNull(projectModels);
        assertEquals(2, projectModels.size());
    }

    @Test
    public void testCountNewlyCreatedProject_whenEmptyBuId_thenReturnSuccess(){
        List<Project> projects = Arrays.asList(projectOne, projectTwo);

        when(projectRepository.getNewlyCreatedProject(eq(null))).thenReturn(projects);

        List<ProjectModel> projectModels = projectService.getNewlyCreatedProject(null);

        assertNotNull(projectModels);
        assertEquals(0, projectModels.size());
    }

    @Test
    void testCountNewlyCreatedProject_whenEmptyResponse_thenReturnEmptyResult() {
        List<Project> projects = new ArrayList<>(Collections.emptyList());

        when(projectRepository.getNewlyCreatedProject(anyInt())).thenReturn(projects);

        List<ProjectModel> projectModels = projectService.getNewlyCreatedProject(1);

        assertNotNull(projectModels);
        assertTrue(projectModels.isEmpty());
    }

    @Test
    public void testGetExpiredProject_whenValidData_thenReturnSuccess(){
        List<Project> projects = Arrays.asList(projectOne, projectTwo);

        when(projectRepository.getExpiredProject(anyInt())).thenReturn(projects);
        when(projectTransformer.applyList(projects)).thenReturn(Arrays.asList(projectModelOne, projectModelTwo));

        List<ProjectModel> projectModels = projectService.getExpiredProject(1);

        assertNotNull(projectModels);
        assertEquals(2, projectModels.size());
    }

    @Test
    public void testGetExpiredProject_whenEmptyBuId_thenReturnSuccess(){
        List<Project> projects = Arrays.asList(projectOne, projectTwo);

        when(projectRepository.getExpiredProject(eq(null))).thenReturn(projects);

        List<ProjectModel> projectModels = projectService.getExpiredProject(null);

        assertNotNull(projectModels);
        assertEquals(0, projectModels.size());
    }


    @Test
    void testGetExpiredProject_whenEmptyResponse_thenReturnEmptyResult() {
        List<Project> projects = new ArrayList<>(Collections.emptyList());

        when(projectRepository.getExpiredProject(anyInt())).thenReturn(projects);

        List<ProjectModel> projectModels = projectService.getExpiredProject(1);

        assertNotNull(projectModels);
        assertTrue(projectModels.isEmpty());
    }

    @Test
    public void testCountExpiredProject_whenValidData_thenReturnSuccess(){
        List<Project> projects = Arrays.asList(projectOne, projectTwo);

        when(projectRepository.getExpiredProject(anyInt())).thenReturn(projects);
        when(projectTransformer.applyList(projects)).thenReturn(Arrays.asList(projectModelOne, projectModelTwo));

        List<ProjectModel> projectModels = projectService.getExpiredProject(1);

        assertNotNull(projectModels);
        assertEquals(2, projectModels.size());
    }

    @Test
    public void testCountExpiredProject_whenEmptyBuId_thenReturnResults(){
        List<Project> projects = Arrays.asList(projectOne, projectTwo);

        when(projectRepository.getExpiredProject(eq(null))).thenReturn(projects);

        List<ProjectModel> projectModels = projectService.getExpiredProject(null);

        assertNotNull(projectModels);
        assertEquals(0, projectModels.size());
    }


    @Test
    void testCountExpiredProject_whenEmptyResponse_thenReturnEmptyResult() {
        List<Project> projects = new ArrayList<>(Collections.emptyList());

        when(projectRepository.getExpiredProject(anyInt())).thenReturn(projects);

        List<ProjectModel> projectModels = projectService.getExpiredProject(1);

        assertNotNull(projectModels);
        assertTrue(projectModels.isEmpty());
    }

    @Test
    void testCountProjectByBuIdAndStatus_whenEmptyBuIdAndStatus_thenThrowCoEException() {
        // when-then
        when(projectRepository.countProjectByBuIdAndStatus(any(), any())).thenThrow(CoEException.class);
        // invoke-assert
        Assertions.assertThrows(CoEException.class, () -> {
            projectService.countProjectByBuIdAndStatus(null, null);
        }, "Expected CoEException to be thrown");
    }

    @Test
    void testCountProjectByBuIdAndStatus_whenEmptyBuId_thenReturnNullResult() {
        // when-then
        when(projectRepository.countProjectByBuIdAndStatus(eq(null), anyInt())).thenReturn(null);
        // invoke
        Integer result = projectService.countProjectByBuIdAndStatus(null, 1);
        // assert
        Assertions.assertNull(result);
        // verify
        verify(projectRepository, times(1)).countProjectByBuIdAndStatus(null, 1);
    }

    @Test
    void testCountProjectByBuIdAndStatus_whenEmptyStatus_thenReturnEmptyResult() {
        // when-then
        when(projectRepository.countProjectByBuIdAndStatus(anyInt(), eq(null))).thenReturn(null);
        // invoke-assert
        Assertions.assertThrows(CoEException.class, () -> {
            projectService.countProjectByBuIdAndStatus(1, null);
        });
    }

    @Test
    void testCountProjectByBuIdAndStatus_whenValidData_thenReturnSuccess() {
        // prepare
        Integer returnInt = 3;
        // when-then
        when(projectRepository.countProjectByBuIdAndStatus(anyInt(), anyInt())).thenReturn(returnInt);
        // invoke
        Integer result = projectService.countProjectByBuIdAndStatus(1, 1);
        // assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(returnInt, result);
        // verify
        verify(projectRepository, times(1)).countProjectByBuIdAndStatus(1, 1);
    }

    @Test
    void testCountProjectsForPieChart_whenEmptyBuId_thenReturnSuccess(){
        // when-then
        when(projectRepository.countProjectsForPieChart(anyInt())).thenReturn(Collections.emptyList());
        when(projectService.countProjectsForPieChart(null)).thenReturn(Collections.emptyList());
        // invoke
        List<ProjectCountModel> result = projectService.countProjectsForPieChart(null);
        // assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(Collections.emptyList(), result);
        // verify
        verify(projectRepository, times(1)).countProjectsForPieChart(null);
    }

    @Test
    void testCountProjectsForPieChart_whenValidData_thenReturnSuccess(){
        // when-then
        when(projectRepository.countProjectsForPieChart(anyInt())).thenReturn(iResultOfCountProjectList);
        // invoke
        List<ProjectCountModel> result = projectService.countProjectsForPieChart(1);
        // assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(projectCountModelList.get(0).getBuId(), result.get(0).getBuId());
        // verify
        verify(projectRepository, times(1)).countProjectsForPieChart(1);
    }

    @Test
    void testCountProjectsForPieChart_whenEmptyResponse_thenReturnEmptyResult(){
        // when-then
        when(projectRepository.countProjectsForPieChart(anyInt())).thenReturn(Collections.emptyList());
        when(projectService.countProjectsForPieChart(null)).thenReturn(Collections.emptyList());
        // invoke
        List<ProjectCountModel> result = projectService.countProjectsForPieChart(null);
        // assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(Collections.emptyList(), result);
        // verify
        verify(projectRepository, times(1)).countProjectsForPieChart(null);
    }
}
