package com.hitachi.coe.fullstack.transformation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.hitachi.coe.fullstack.entity.BusinessUnit;
import com.hitachi.coe.fullstack.entity.Practice;
import com.hitachi.coe.fullstack.entity.ProjectType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.hitachi.coe.fullstack.entity.BusinessDomain;
import com.hitachi.coe.fullstack.entity.Project;
import com.hitachi.coe.fullstack.entity.ProjectStatus;
import com.hitachi.coe.fullstack.model.ProjectModel;

public class ProjectTransformerTest {
    private ProjectTypeTransformer projectTypeTransformer = new ProjectTypeTransformer();

    private BusinessDomainTransformer businessDomainTransformer = new BusinessDomainTransformer();

    private BusinessUnitTransformer businessUnitTransformer = new BusinessUnitTransformer();
    private ProjectTransformer projectTransformer = new ProjectTransformer(projectTypeTransformer, businessDomainTransformer, businessUnitTransformer);

    @Test
    void testApply() {
        Project project = new Project();
        project.setId(20);
        project.setCode("project 1");
        project.setCustomerName("ABC Company");
        project.setDescription("Project for ABC Company");
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 86400000); // Thêm một ngày
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setName("ABC Project");
        project.setProjectManager("Uchiha Itachi");
        project.setStatus(ProjectStatus.CLOSE);

		Practice practice = new Practice();
		practice.setId(1);
        practice.setCode("test");
        practice.setName("ABC");

        BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setId(1);
        businessDomain.setCode("abcd");
        businessDomain.setDescription("abcd domain");
        businessDomain.setName("abcd.com");
		businessDomain.setPractice(practice);
        project.setBusinessDomain(businessDomain);

        BusinessUnit businessUnit = new BusinessUnit();
        businessUnit.setId(10);
        businessUnit.setCode("test");
        businessUnit.setName("ABC");
        project.setBusinessUnit(businessUnit);

        ProjectType projectType = new ProjectType();
        projectType.setId(1);
        projectType.setCode("test");
        projectType.setName("ABC");
        project.setProjectType(projectType);
		businessDomain.setProjects(List.of(project));

        ProjectModel projectModel = projectTransformer.apply(project);

        assertEquals(20, projectModel.getId());
        assertEquals("project 1", projectModel.getCode());
        assertEquals("ABC Company", projectModel.getCustomerName());
        assertEquals("Project for ABC Company", projectModel.getDescription());
        assertEquals(endDate, projectModel.getEndDate());
        assertEquals("ABC Project", projectModel.getName());
        assertEquals("Uchiha Itachi", projectModel.getProjectManager());
        assertEquals(startDate, projectModel.getStartDate());
        assertEquals(3, projectModel.getStatus());
        assertEquals(1, projectModel.getBusinessDomain().getId());
        assertEquals(10, projectModel.getBusinessUnitId());
        assertEquals(1, projectModel.getProjectType().getId());
    }

    @Test
    void testProjectTransformerApplyList() {
        // prepare
        List<Project> projects = new ArrayList<>();
        Project project = new Project();
        project.setId(20);
        project.setCode("project 1");
        project.setCustomerName("ABC Company");
        project.setDescription("Project for ABC Company");
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 86400000); // Thêm một ngày
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setName("ABC Project");
        project.setProjectManager("Uchiha Itachi");
        project.setStatus(ProjectStatus.CLOSE);

        Practice practice = new Practice();
        practice.setId(1);
        practice.setCode("test");
        practice.setName("ABC");

        BusinessDomain businessDomain = new BusinessDomain();
        businessDomain.setId(1);
        businessDomain.setCode("abcd");
        businessDomain.setDescription("abcd domain");
        businessDomain.setName("abcd.com");
        businessDomain.setPractice(practice);
        project.setBusinessDomain(businessDomain);

        BusinessUnit businessUnit = new BusinessUnit();
        businessUnit.setId(10);
        businessUnit.setCode("test");
        businessUnit.setName("ABC");
        project.setBusinessUnit(businessUnit);

        ProjectType projectType = new ProjectType();
        projectType.setId(1);
        projectType.setCode("test");
        projectType.setName("ABC");
        project.setProjectType(projectType);
        businessDomain.setProjects(List.of(project));
        projects.add(project);
        projects.add(project);

        // invoke
        List<ProjectModel> projectModel = projectTransformer.applyList(projects);
        // assert
        assertNotNull(projectModel);
        assertEquals(2, projectModel.size());
        assertEquals(20, projectModel.get(0).getId());
        assertEquals("project 1", projectModel.get(0).getCode());
        assertEquals("ABC Company", projectModel.get(0).getCustomerName());
        assertEquals("Project for ABC Company", projectModel.get(0).getDescription());
        assertEquals(endDate, projectModel.get(0).getEndDate());
        assertEquals("ABC Project", projectModel.get(0).getName());
        assertEquals("Uchiha Itachi", projectModel.get(0).getProjectManager());
        assertEquals(startDate, projectModel.get(0).getStartDate());
        assertEquals(3, projectModel.get(0).getStatus());
        assertEquals(1, projectModel.get(0).getBusinessDomain().getId());
        assertEquals(10, projectModel.get(0).getBusinessUnitId());
        assertEquals(1, projectModel.get(0).getProjectType().getId());
    }

    @Test
    public void testTransformerBusinessDomainApplyList_EmptyList() {
        List<Project> models = Collections.emptyList();

        List<ProjectModel> result = projectTransformer.applyList(models);

        Assertions.assertTrue(result.isEmpty());
    }
}
