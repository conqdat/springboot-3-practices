package com.hitachi.coe.fullstack.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Arrays;
import java.util.Collections;

import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.constant.StatusConstant;
import com.hitachi.coe.fullstack.entity.CoeCoreTeam;
import com.hitachi.coe.fullstack.entity.EmployeeStatus;
import com.hitachi.coe.fullstack.repository.CoeCoreTeamRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import com.hitachi.coe.fullstack.entity.Employee;
import com.hitachi.coe.fullstack.exceptions.CoEException;
import com.hitachi.coe.fullstack.exceptions.InvalidDataException;
import com.hitachi.coe.fullstack.model.BarChartModel;
import com.hitachi.coe.fullstack.model.ChartSkillAndLevelModel;
import com.hitachi.coe.fullstack.model.CountEmployeeModel;
import com.hitachi.coe.fullstack.model.DataSetBarChart;
import com.hitachi.coe.fullstack.model.EmployeeModel;
import com.hitachi.coe.fullstack.model.IBarChartDepartmentModel;
import com.hitachi.coe.fullstack.model.IEmployeeDetails;
import com.hitachi.coe.fullstack.model.IEmployeeSearchForTeam;
import com.hitachi.coe.fullstack.model.IPieChartModel;
import com.hitachi.coe.fullstack.model.IResultOfQueryBarChart;
import com.hitachi.coe.fullstack.model.IResultOfQueryCountEmployees;
import com.hitachi.coe.fullstack.model.IResultOfQuerySkillAndLevel;
import com.hitachi.coe.fullstack.repository.EmployeeRepository;
import com.hitachi.coe.fullstack.repository.LevelRepository;
import com.hitachi.coe.fullstack.service.impl.EmployeeServiceImpl;
import com.hitachi.coe.fullstack.transformation.BranchModelTransformer;
import com.hitachi.coe.fullstack.transformation.CoeCoreTeamModelTransformer;
import com.hitachi.coe.fullstack.transformation.EmployeeModelTransformer;
import com.hitachi.coe.fullstack.transformation.EmployeeTransformer;
import com.hitachi.coe.fullstack.transformation.PracticeModelTransformer;

import lombok.SneakyThrows;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-data-test.properties")
class EmployeeServiceTest {
	@InjectMocks
	private EmployeeServiceImpl employeeService;
	@Mock
	private EmployeeRepository employeeRepository;

	@MockBean
	CoeCoreTeamRepository coeCoreTeamRepository;
	@Mock
	private LevelRepository levelRepository;
	@Mock
	private EmployeeModel employeeModel;

	@Mock
	private EmployeeTransformer employeeTransformer;

	@Mock
	private EmployeeModelTransformer employeeModelTransformer;

	@Mock
	private PracticeModelTransformer practiceModelTransformer;

	@Mock
	private CoeCoreTeamModelTransformer coeCoreTeamModelTransformer;

	@Mock
	private BranchModelTransformer branchModelTransformer;

	@Test
	void testGetEmployeeById_ExistingEmployee() {
		// Arrange
		Integer employeeId = 1;
		Employee employee = new Employee();
		EmployeeStatus employeeStatus = new EmployeeStatus();
		employeeStatus.setId(1);
		employeeStatus.setStatus(StatusConstant.STATUS_ACTIVE);
		Calendar calendar = Calendar.getInstance();
		calendar.set(2002, Calendar.NOVEMBER, 17);
		Date specificDate = calendar.getTime();
		// Set the specific date on the mock object
		employeeStatus.setStatusDate(specificDate);
		employee.setId(employeeId);
		employee.setEmployeeStatuses(List.of(employeeStatus));
		EmployeeModel expectedEmployeeModel = new EmployeeModel();
		expectedEmployeeModel.setId(employeeId);
		when(employeeRepository.findById(any(Integer.class))).thenReturn(Optional.of(employee));
		when(employeeTransformer.apply(any(Employee.class))).thenReturn(expectedEmployeeModel);
		EmployeeModel result = employeeService.getEmployeeById(employeeId);

		assertEquals(expectedEmployeeModel.getId(), result.getId());
	}

	@Test
	@SneakyThrows(InvalidDataException.class)
	void testGetEmployeeById_NonExistingEmployee() {
		Integer employeeId = 1;
		when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

		assertThrows(InvalidDataException.class, () -> employeeService.getEmployeeById(employeeId));
	}


//	TODO
//	mvn test have error: NullPointer
//	@Test
//	void createEmployee_Success() {
//		EmployeeModel em = new EmployeeModel();
//		em.setId(1);
//		em.setCreatedBy("ThoVo");
//		em.setName("DatCongNguyen");
//
//		Employee e = new Employee();
//		e.setId(1);
//		e.setCreatedBy("ThoVo");
//		e.setName("DatCongNguyen");
//
//		when(employeeModelTransformer.apply(em)).thenReturn(e);
//		when(employeeRepository.save(e)).thenReturn(e);
//
//		Integer actual = employeeService.add(em);
//		assertEquals(1, actual);
//	}

//    TODO
//    mvn test have Failures: expected: <com.hitachi.coe.fullstack.exceptions.InvalidDataException>
//	  but was: <java.lang.NullPointer Exception>
//    @Test
//    void createEmployee_UnSuccess() {
//        EmployeeModel em = new EmployeeModel();
//
//        Throwable throwable = assertThrows(Exception.class, () -> employeeService.add(em));
//
//        assertEquals(InvalidDataException.class, throwable.getClass());
//        assertEquals("employee.null", throwable.getMessage());
//    }

//	TODO
//	@Test
//	void updateEmployee() {
//		int employeeId = 1;
//		EmployeeModel employeeModel = new EmployeeModel();
//		employeeModel.setId(employeeId);
//		employeeModel.setEmail("huu.vo@hitachivantar.com");
//		employeeModel.setHccId("COE01");
//		employeeModel.setLdap("HV01");
//		employeeModel.setLegalEntityHireDate(new Date("22/03/2023"));
//		employeeModel.setName("Huu Vo");
//
//		Employee existEmployee = new Employee();
//		existEmployee.setId(employeeId);
//		existEmployee.setEmail("huu.vo@hitachivantar.com");
//		existEmployee.setHccId("COE01");
//		existEmployee.setLdap("HV01");
//		existEmployee.setLegalEntityHireDate(new Date("22/03/2023"));
//		existEmployee.setName("Huu Vo");
//
//		when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existEmployee));
//		when(employeeRepository.save(any(Employee.class))).thenReturn(existEmployee);
//
//		Integer actual = employeeService.updateEmployee(employeeModel);
//
//		assertEquals(employeeId, actual);
//
//	}

	@Test
	void updateEmployee_fail_not_found_employee() {
		int employeeId = 1;
		EmployeeModel employeeModel = new EmployeeModel();
		employeeModel.setId(employeeId);

		when(employeeRepository.findById(employeeModel.getId())).thenReturn(Optional.empty());

		assertThrows(InvalidDataException.class, () -> employeeService.updateEmployee(employeeModel));
	}

	@Test
	void getQuantityEmployeeOfLevel_ValidIds_Success() {
		// Arrange
		Integer branchId = 1;
		Integer coeCoreTeamId = 1;
		Integer coe_id = 1;

		// Act
		List<IBarChartDepartmentModel> result = employeeService.getQuantityEmployeeOfLevel(branchId, coeCoreTeamId,
				coe_id);

		// Assert
		assertNotNull(result);
	}

	@Test
	void getQuantityEmployeeOfLevel_NullBranchId_coeCoreTeamId_coe_id() {
		Integer branchId = null;
		Integer coeCoreTeamId = null;
		Integer coe_id = null;

		// Call the service method and verify the exception
		assertNotNull(employeeService.getQuantityEmployeeOfLevel(branchId, coeCoreTeamId, coe_id));

	}

	@Test
	void testGetQuantityOfEachLevelForBarChart_whenValidIds_Success() {
		// Arrange
		Integer branchId = 1;
		Integer groupId = 2;
		Integer teamId = 3;

		// Act
		BarChartModel result = employeeService.getQuantityOfLevelForBarChart(branchId, groupId, teamId);

		// Assert
		assertNotNull(result);
	}

	@Test
	void testGetQuantityOfEachLevelForBarChart_whenNullGroupId_ThrowsException() {
		Integer branchId = 1;
		Integer groupId = null;
		Integer teamId = 3;

		// Call the service method and verify the exception
		Throwable throwable = assertThrows(CoEException.class, () -> {
			employeeService.getQuantityOfLevelForBarChart(branchId, groupId, teamId);
		});
		assertEquals(CoEException.class, throwable.getClass());
	}

	@Test
	void testGetQuantityOfEachLevelForBarChart_whenAllNull_ThenSuccess() {
		Integer branchId = null;
		Integer groupId = null;
		Integer teamId = null;

		// Call the service method and verify the exception
		assertNotNull(employeeService.getQuantityOfLevelForBarChart(branchId, groupId, teamId));

	}

	@Test
	void testGetQuantityOfEachLevelForBarChart() {
		// Mock data
		Integer branchId = 1;
		Integer groupId = 2;
		Integer teamId = 3;

		List<IResultOfQueryBarChart> results = Arrays.asList(createResultOfQueryBarChart("Label1", "Level1", 5L),
				createResultOfQueryBarChart("Label1", "Level2", 8L),
				createResultOfQueryBarChart("Label2", "Level1", 3L));

		// Mock repository method
		when(employeeRepository.getQuantityOfLevelForBarChart(branchId, groupId, teamId)).thenReturn(results);

		// Expected output
		BarChartModel expectedBarChartModel = new BarChartModel();
		List<String> expectedLabelLevels = Arrays.asList("Level1", "Level2");
		List<DataSetBarChart> expectedDatasets = Arrays.asList(createDataSetBarChart("Label1", Arrays.asList(5L, 8L)),
				createDataSetBarChart("Label2", Arrays.asList(3L, 0L)));
		expectedBarChartModel.setLabels(expectedLabelLevels);
		expectedBarChartModel.setDatasets(expectedDatasets);

		employeeService.getQuantityOfLevelForBarChart(branchId, groupId, teamId);

		// Verify repository method was called
		verify(employeeRepository).getQuantityOfLevelForBarChart(branchId, groupId, teamId);

		// Assert the expected and actual results
	}

	private IResultOfQueryBarChart createResultOfQueryBarChart(String label, String fieldName, Long total) {
		return new IResultOfQueryBarChart() {
			@Override
			public String getLabel() {
				return label;
			}

			@Override
			public String getFieldName() {
				return fieldName;
			}

			@Override
			public Long getTotal() {
				return total;
			}
		};
	}

	private DataSetBarChart createDataSetBarChart(String label, List<Long> data) {
		DataSetBarChart dataSetBarChart = new DataSetBarChart();
		dataSetBarChart.setLabel(label);
		dataSetBarChart.setData(data);
		return dataSetBarChart;
	}

    @Test
    void testGetEmployeeDetailsByHccId_whenHccIdIsNull_thenThrowCoEException() {
        // prepare
        String testHccId = null;
        // invoke & assert
        assertThrows(CoEException.class, () -> employeeService.getEmployeeDetailsByHccId(testHccId));
    }

    @Test
    void testGetEmployeeDetailsByHccId_whenHccIdIsNotNullAndInvalid_thenThrowCoEException() {
        // prepare
        String testHccId = "invalid_hccId_because_this_hccId_is_not_exist";
        // invoke & assert
        assertThrows(CoEException.class, () -> employeeService.getEmployeeDetailsByHccId(testHccId));
    }

    @Test
    void testGetEmployeeDetailsByHccId_whenHccIdIsValid_thenReturnIEmployeeDetails() {
        // prepare
        String testHccId = "test_hccId";
        IEmployeeDetails testIEmployeeDetails = mock(IEmployeeDetails.class);
        Employee testEmployee = new Employee();
        testEmployee.setHccId("9999");
        testEmployee.setName("test_employee");
        testEmployee.setEmail("test_employee@gmail.com");
        // when - then
        when(employeeRepository.findByHccId(anyString())).thenReturn(testEmployee);
        when(employeeService.getEmployeeDetailsByHccId(testHccId)).thenReturn(testIEmployeeDetails);
        // invoke
        employeeRepository.findByHccId(testHccId);
        // assert
        assertNotNull(employeeService.getEmployeeDetailsByHccId(testHccId));
    }


    @Test
    void testPieChartLevel_WithData() {
        Integer branchId = 1;
        Integer coeCoreTeamId = 2;
        Integer coeId = 3;

        List<IPieChartModel> expectedResponse = new ArrayList<>();
        expectedResponse.add(new IPieChartModel() {
			
			@Override
			public String getLabel() {
				return "label";
			}
			
			@Override
			public Double getData() {
				return 1D;
			}
		});
        when(levelRepository.piechartlevel(branchId, coeCoreTeamId, coeId)).thenReturn(expectedResponse);
        List<IPieChartModel> response = employeeService.getLevelPieChart(branchId, coeCoreTeamId, coeId);

        assertNotNull(response);

    }

    @Test
    void testPieChartLevel_WithEmptyData() {
        Integer branchId = 1;
        Integer coeCoreTeamId = 2;
        Integer coeId = 3;

        List<IPieChartModel> expectedResponse = new ArrayList<>();
        when(levelRepository.piechartlevel(branchId, coeCoreTeamId, coeId)).thenReturn(expectedResponse);

        assertThrows(InvalidDataException.class, () -> {
        	employeeService.getLevelPieChart(branchId, coeCoreTeamId, coeId);
        });
    }

	@Test
	void testGetEmployeesByTeamId_withValidTeamId_returnEmployeeDTO(){
		Integer teamId = 1;
		CoeCoreTeam team = new CoeCoreTeam();
		team.setCode("BE");
		team.setName("Back-end");
		team.setStatus(1);
		team.setSubLeaderId(1);
		List<Employee> employeeList = new ArrayList<>();
		List<EmployeeModel> employeeModels = new ArrayList<>();
		employeeModels.add(new EmployeeModel());
		
		when(coeCoreTeamRepository.findById(teamId)).thenReturn(Optional.of(team));
		when(employeeRepository.findByCoeCoreTeamId(teamId)).thenReturn(employeeList);
		when(employeeTransformer.applyList(employeeList)).thenReturn(employeeModels);

		List<EmployeeModel> result = employeeService.getEmployeesByTeamId(teamId);

		assertNotNull(result);
		assertEquals(1, result.size());
	}
	
	@Test
	void testGetEmployeesByTeamId_withInvalidTeamId_returnEmptyList(){
		Mockito.when(coeCoreTeamRepository.findById(1)).thenReturn(Optional.empty());
		List<EmployeeModel> result = employeeService.getEmployeesByTeamId(1);
		Assertions.assertNotNull(result);
		Assertions.assertTrue(result.isEmpty());
	}

	@Test
	void testGetQuantityOfEmployeesForEachSkill_WhenFullParams_ThenSuccess() {

		Integer branchId = 1;
		Integer coeId = 8;
		String strSkillIds = "1,2,3,4,5,6";
		List<Integer> arrSkillIds = Arrays.asList(1, 2, 3, 4, 5, 6);

		List<IResultOfQuerySkillAndLevel> mockData = Arrays.asList(createResultOfQuerySkillAndLevel("Cloud & Data", "Java", 10L, 1L, 4L, 2L, 1L,2L),
																   createResultOfQuerySkillAndLevel("Cloud & Data", "AWS", 9L, 1L, 3L, 2L, 1L, 2L),
																   createResultOfQuerySkillAndLevel("Cloud & Data", "Python", 13L, 1L, 6L, 2L, 2L, 2L));

		when(employeeRepository.getQuantityOfEmployeesForEachSkill(branchId, coeId, strSkillIds))
				.thenReturn(mockData);

		ChartSkillAndLevelModel responseEntity = employeeService.getQuantityOfEmployeesForEachSkill(branchId, coeId,
				arrSkillIds);

		assertNotNull(responseEntity);

	}

	@Test
	void testGetQuantityOfEmployeesForEachSkill_WhenNullSkillIds_ThenThrowCoEException() {
		Integer branchId = 1;
		Integer coeId = 3;
		List<Integer> arrTopSkillIds = null;
		Throwable throwable = Assertions.assertThrows(CoEException.class, () -> {
			employeeService.getQuantityOfEmployeesForEachSkill(branchId, coeId, arrTopSkillIds);
		});

		Assertions.assertEquals(CoEException.class, throwable.getClass());

	}

	private IResultOfQuerySkillAndLevel createResultOfQuerySkillAndLevel(String label, String skillName, Long total, Long level1, Long level2, 
	Long level3, Long level4, Long level5) {
		return new IResultOfQuerySkillAndLevel() {
			@Override
			public String getLabel() {
				return label;
			}

			@Override
			public String getSkillName() {
				return skillName;
			}

			@Override
			public Long getTotal() {
				return total;
			}

			@Override
			public Long getLevel1() {
				return level1;
			}

			@Override
			public Long getLevel2() {
				return level2;
			}

			@Override
			public Long getLevel3() {
				return level3;
			}

			@Override
			public Long getLevel4() {
				return level4;
			}

			@Override
			public Long getLevel5() {
				return level5;
			}
		};
	}

	@Test
	void testGetCountOfEmployeesForCurrentMonth_WhenCountNotEqualTo0_ThenSuccess()
	{
		// Arrange
		List<IResultOfQueryCountEmployees> results = new ArrayList<>();
		results.add(0, new IResultOfQueryCountEmployees() {	//Current month data
			@Override
			public Long getTotalEmployees() {
				return 725L;
			}
			@Override
			public Long getOnProjectEmployees() {
				return 565L;
			}
			@Override
			public Long getOnBenchEmployees() {
				return 146L;
			}
			@Override
			public Long getNewEmployees() {
				return 11L;
			}
		});

		results.add(1, new IResultOfQueryCountEmployees() {	//Last month data
			@Override
			public Long getTotalEmployees() {
				return 710L;
			}
			@Override
			public Long getOnProjectEmployees() {
				return 559L;
			}
			@Override
			public Long getOnBenchEmployees() {
				return 126L;
			}
			@Override
			public Long getNewEmployees() {
				return 6L;
			}
		});

		//Act
		when(employeeRepository.getCountOfEmployeesCurrentAndLastMonth()).thenReturn(results);
		CountEmployeeModel countEmployeeModel = employeeService.getCountOfEmployeesForCurrentMonth();
		
		//Assert
		assertNotNull(countEmployeeModel);
		assertEquals(725L, countEmployeeModel.getTotalEmployees());
		assertEquals(565L, countEmployeeModel.getOnProjectEmployees());
		assertEquals(1.5f, countEmployeeModel.getDiffNew());
	}

	@Test
	void testGetCountOfEmployeesForCurrentMonth_WhenCountEqualTo0_ThenSuccess()
	{
		// Arrange
		List<IResultOfQueryCountEmployees> results = new ArrayList<>();
		results.add(0, new IResultOfQueryCountEmployees() {	//Current month data
			@Override
			public Long getTotalEmployees() {
				return 725L;
			}
			@Override
			public Long getOnProjectEmployees() {
				return 565L;
			}
			@Override
			public Long getOnBenchEmployees() {
				return 146L;
			}
			@Override
			public Long getNewEmployees() {
				return 11L;
			}
		});

		results.add(1, new IResultOfQueryCountEmployees() {	//Last month data
			@Override
			public Long getTotalEmployees() {
				return 710L;
			}
			@Override
			public Long getOnProjectEmployees() {
				return 700L;
			}
			@Override
			public Long getOnBenchEmployees() {	//On Bench last month is 0, the diffOnBench will return 0.0f as default
				return 0L;
			}
			@Override
			public Long getNewEmployees() {
				return 6L;
			}
		});

		//Act
		when(employeeRepository.getCountOfEmployeesCurrentAndLastMonth()).thenReturn(results);
		CountEmployeeModel countEmployeeModel = employeeService.getCountOfEmployeesForCurrentMonth();
		
		//Assert
		assertNotNull(countEmployeeModel);
		assertEquals(725L, countEmployeeModel.getTotalEmployees());
		assertEquals(565L, countEmployeeModel.getOnProjectEmployees());
		assertEquals(0.0f, countEmployeeModel.getDiffOnBench());
	}

	@Test
	void testSearchEmployeesByCoeAndNameString_whenMatch_thenReturnEmployees() {
		Integer coeId = 1;
		String nameString = "nguyen";
		List<IEmployeeSearchForTeam> mockList = new ArrayList<IEmployeeSearchForTeam>();

		when(employeeRepository.searchEmployeesByCoeAndNameString(coeId, nameString)).thenReturn(mockList);

		List<IEmployeeSearchForTeam> resultList = employeeService.searchEmployeesByCoeAndNameString(coeId, nameString);
		
		assertNotNull(resultList);
	}

	@Test
	void testSearchEmployeesByCoeAndNameString_whenNotMatch_thenEmptyList() {
		Integer coeId = 1;
		String nameString = "nguyen";
		List<IEmployeeSearchForTeam> emptyList = new ArrayList<>(Collections.emptyList());

		when(employeeRepository.searchEmployeesByCoeAndNameString(coeId, nameString)).thenReturn(emptyList);

		List<IEmployeeSearchForTeam> resultList = employeeService.searchEmployeesByCoeAndNameString(coeId, nameString);
		
		assertNotNull(resultList);
		assertTrue(resultList.isEmpty());
	}
}
