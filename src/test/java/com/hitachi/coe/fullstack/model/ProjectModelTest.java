package com.hitachi.coe.fullstack.model;

import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-data-test.properties")
public class ProjectModelTest {

	@Test
	public void testProjectModel() {
		BusinessDomainModel businessDomainModel = new BusinessDomainModel();
		businessDomainModel.setId(1);
		businessDomainModel.setCode("abc");
		businessDomainModel.setDescription("HEHHEHE");
		businessDomainModel.setName("abc.com");

		ProjectTypeModel projectTypeModel = new ProjectTypeModel();
		projectTypeModel.setId(1);
		projectTypeModel.setCode("FFTE");
		projectTypeModel.setName("Fixed Fee- Time and Expense");

		Random random = new Random();
		long randomMilliseconds = random.nextLong();
		Date randomDate = new Date(randomMilliseconds);
		ProjectModel projectModel = new ProjectModel();
		projectModel.setId(12);
		projectModel.setCode("project2");
		projectModel.setCustomerName("Nguyen van C");
		projectModel.setDescription("project vip pro v2");
		projectModel.setEndDate(randomDate);
		projectModel.setName("project 2 A");
		projectModel.setProjectManager("Nguyen van D");
		projectModel.setStartDate(randomDate);
		projectModel.setStatus(1);
		projectModel.setBusinessDomain(businessDomainModel);
		projectModel.setBusinessUnitId(1);
		projectModel.setProjectType(projectTypeModel);
		
		assertEquals("project2", projectModel.getCode());
		assertEquals("Nguyen van C",projectModel.getCustomerName());
		assertEquals("project vip pro v2", projectModel.getDescription());
		assertEquals(randomDate, projectModel.getEndDate());
		assertEquals("project 2 A", projectModel.getName());
		assertEquals("Nguyen van D", projectModel.getProjectManager());
		assertEquals(randomDate, projectModel.getStartDate());
		assertEquals(1, projectModel.getStatus());
		assertEquals(1, projectModel.getBusinessDomain().getId());
		assertEquals(1, projectModel.getBusinessUnitId());
		assertEquals(1, projectModel.getProjectType().getId());
	}
	
}
