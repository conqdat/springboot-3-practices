package com.hitachi.coe.fullstack.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.hitachi.coe.fullstack.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.sql.Timestamp;
import java.util.Date;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-data-test.properties")
public class CoeCoreTeamModelTest {

	@Test
	void testModelCoeCoreTeam_thenSuccess() {
		CoeCoreTeamModel coeCoreTeamModel = new CoeCoreTeamModel();
		coeCoreTeamModel.setId(1);
		coeCoreTeamModel.setCode("Code");
		coeCoreTeamModel.setName("Coe Name");
		coeCoreTeamModel.setStatus(1);

		EmployeeModel employee = new EmployeeModel();
		employee.setId(1);
		employee.setEmail("a.nguyen@hitachivantara.com");
		employee.setLdap("71269780");
		employee.setHccId("125351");
		employee.setName("Nguyen A 1");
		employee.setLegalEntityHireDate(new Timestamp(System.currentTimeMillis()));
		employee.setCreatedBy("admin");
		employee.setCreated(new Date());
		coeCoreTeamModel.setSubLeader(employee);

		CenterOfExcellenceModel coe = new CenterOfExcellenceModel();
		coe.setId(1);
		coe.setCode("SA");
		coe.setName("Solution Architect CoE");
		coeCoreTeamModel.setCenterOfExcellence(coe);

		assertEquals(1, coeCoreTeamModel.getId());
		assertEquals("Code", coeCoreTeamModel.getCode());
		assertEquals("Coe Name", coeCoreTeamModel.getName());
		assertEquals(1, coeCoreTeamModel.getStatus());
		assertEquals(1, coeCoreTeamModel.getSubLeader().getId());
		assertEquals(1, coeCoreTeamModel.getCenterOfExcellence().getId());
	}

	@Test
	void testAllAgruCoeCoreTeamModel_thenSuccess() {
		CoeCoreTeamModel coeCoreTeamModel = new CoeCoreTeamModel("code", "name", 1, new EmployeeModel(), new CenterOfExcellenceModel() );

		assertEquals("code", coeCoreTeamModel.getCode());
	}
}
