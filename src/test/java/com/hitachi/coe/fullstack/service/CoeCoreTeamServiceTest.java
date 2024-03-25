package com.hitachi.coe.fullstack.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.hitachi.coe.fullstack.constant.Constants;
import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.exceptions.CoEException;
import com.hitachi.coe.fullstack.model.CoeCoreTeamModel;
import com.hitachi.coe.fullstack.model.EmployeeModel;
import com.hitachi.coe.fullstack.model.CenterOfExcellenceModel;
import com.hitachi.coe.fullstack.model.ICoeCoreTeamSearch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;

import com.hitachi.coe.fullstack.constant.StatusConstant;
import com.hitachi.coe.fullstack.entity.CenterOfExcellence;
import com.hitachi.coe.fullstack.entity.CoeCoreTeam;
import com.hitachi.coe.fullstack.entity.Employee;
import com.hitachi.coe.fullstack.entity.EmployeeStatus;
import com.hitachi.coe.fullstack.repository.CenterOfExcellenceRepository;
import com.hitachi.coe.fullstack.repository.CoeCoreTeamRepository;
import com.hitachi.coe.fullstack.repository.EmployeeRepository;
import com.hitachi.coe.fullstack.repository.EmployeeStatusRepository;
import com.hitachi.coe.fullstack.transformation.CoeCoreTeamModelTransformer;
import com.hitachi.coe.fullstack.transformation.CoeCoreTeamTransformer;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestPropertySource("classpath:application-data-test.properties")
class CoeCoreTeamServiceTest {
	@MockBean
	private EmployeeRepository employeeRepository;

	@MockBean
	private CoeCoreTeamRepository coeCoreTeamRepository;

	@MockBean
	private EmployeeStatusRepository employeeStatusRepository;

	@Autowired
	private CoeCoreTeamService coeCoreTeamService;

	@MockBean
	CoeCoreTeamModelTransformer coeCoreTeamModelTransformer;

	@MockBean
	CoeCoreTeamTransformer coeCoreTeamTransformer;

	@MockBean
	CenterOfExcellenceRepository centerOfExcellenceRepository;

	CoeCoreTeamModel coeCoreTeamModel;

	CenterOfExcellenceModel coeOne;

	EmployeeModel employeeOne;

	ICoeCoreTeamSearch iCoeCoreTeamModelOne;

	ICoeCoreTeamSearch iCoeCoreTeamModelTwo;

	CenterOfExcellence coe;

	@BeforeEach
	void setUp() {
		iCoeCoreTeamModelOne = mock(ICoeCoreTeamSearch.class);
		iCoeCoreTeamModelTwo = mock(ICoeCoreTeamSearch.class);
	}

	@Test
	void testRemoveMembersFromCoeCoreTeam_whenValidParam_thenSuccess() {
		// Mock data
		List<Integer> employeeIds = new ArrayList<>();
		employeeIds.add(1);
		employeeIds.add(2);

		List<Employee> employees = new ArrayList<>();
		employees.add(new Employee());
		employees.add(new Employee());

		CoeCoreTeam defaultCoeCoreTeam = new CoeCoreTeam();
		defaultCoeCoreTeam.setCode(Constants.DEFAULT_CODE);

		// Mock the behavior of repositories
		when(employeeRepository.findAllById(employeeIds)).thenReturn(employees);
		when(coeCoreTeamRepository.findByCode(anyString())).thenReturn(defaultCoeCoreTeam);
		when(employeeRepository.saveAll(employees)).thenReturn(employees);

		int removedCount = coeCoreTeamService.removeMembersFromCoeCoreTeam(employeeIds);

		assertEquals(2, removedCount);
		for (Employee emp : employees) {
			assertEquals(defaultCoeCoreTeam, emp.getCoeCoreTeam());
		}
	}

	@Test
	void testRemoveMembersFromCoeCoreTeam_whenEmployeeIdsNotFound_thenThrowException() {
		// Mock data
		List<Integer> employeeIds = new ArrayList<>();

		CoEException throwable = assertThrows(CoEException.class, () -> coeCoreTeamService.addMembersToCoeCoreTeam(1, employeeIds));
		assertEquals(ErrorConstant.MESSAGE_EMPLOYEE_LIST_ID_NOT_EMPTY, throwable.getMessage());
	}

	@Test
	void testRemoveMembersToCoeCoreTeam_whenDefaultTeamIdNotFound_ThenThrowException() {
		List<Integer> employeeIds = new ArrayList<>();
		employeeIds.add(1);
		employeeIds.add(2);
		System.out.println(employeeIds);
		List<Employee> employees = new ArrayList<>();
		employees.add(new Employee());
		employees.add(new Employee());
	
		// Mock the behavior of repositories
		when(employeeRepository.findAllById(employeeIds)).thenReturn(employees);
		when(coeCoreTeamRepository.findById(0)).thenReturn(Optional.empty());	//Default Team not found

		CoEException throwable = assertThrows(CoEException.class, () -> coeCoreTeamService.addMembersToCoeCoreTeam(1, employeeIds));
		assertEquals(ErrorConstant.MESSAGE_COE_CORE_TEAM_NOT_FOUND, throwable.getMessage());
	}

	@Test
	void testUpdateCoeCoreTeam_whenValidCoeCoreTeamModel_ThenSuccess() {
		employeeOne = new EmployeeModel();
		employeeOne.setId(1);
		CenterOfExcellenceModel centerOfExcellenceModel = new CenterOfExcellenceModel();
		centerOfExcellenceModel.setId(2);
		Integer coeCoreTeamId = 1;
		coeCoreTeamModel = new CoeCoreTeamModel();
		coeCoreTeamModel.setId(coeCoreTeamId);
		coeCoreTeamModel.setCode("0123");
		coeCoreTeamModel.setName("UXUI");
		coeCoreTeamModel.setSubLeader(employeeOne);
		coeCoreTeamModel.setStatus(1);
		coeCoreTeamModel.setCenterOfExcellence(centerOfExcellenceModel);


		coe = new CenterOfExcellence();
		coe.setId(centerOfExcellenceModel.getId());
		CoeCoreTeam existingCoeCoreTeam = new CoeCoreTeam();
		existingCoeCoreTeam.setId(1);
		existingCoeCoreTeam.setCode(coeCoreTeamModel.getCode());
		existingCoeCoreTeam.setName(coeCoreTeamModel.getName().trim());
		existingCoeCoreTeam.setStatus(coeCoreTeamModel.getStatus());
		existingCoeCoreTeam.setSubLeaderId(1);
		existingCoeCoreTeam.setCenterOfExcellence(coe);

		CoeCoreTeam defaultCoeCoreTeam = new CoeCoreTeam();
		defaultCoeCoreTeam.setId(2);

		Optional<EmployeeStatus> lastStatusOptional = Optional.of(new EmployeeStatus());
		lastStatusOptional.get().setStatus(StatusConstant.STATUS_ACTIVE);
		
		Mockito.when(coeCoreTeamRepository.findByCode(anyString())).thenReturn(defaultCoeCoreTeam);
		Mockito.when(coeCoreTeamRepository.findById(anyInt())).thenReturn(Optional.of(existingCoeCoreTeam));
		Mockito.when(coeCoreTeamRepository.existsBySubLeaderId(anyInt())).thenReturn(false);
		Mockito.when(coeCoreTeamRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);
		Mockito.when(centerOfExcellenceRepository.findById(anyInt())).thenReturn(Optional.of(coe));
		Mockito.when(coeCoreTeamRepository.save(existingCoeCoreTeam)).thenReturn(existingCoeCoreTeam);
		Mockito.when(employeeStatusRepository.findFirstByEmployeeIdOrderByStatusDateDesc(anyInt())).thenReturn(lastStatusOptional);

		Integer actual = coeCoreTeamService.updateCoeCoreTeam(coeCoreTeamModel);
		
		assertNotNull(actual);
		assertEquals(1, actual);
		assertEquals("0123", existingCoeCoreTeam.getCode());
		assertEquals("UXUI", existingCoeCoreTeam.getName());
		assertEquals(1, existingCoeCoreTeam.getStatus());
		assertEquals(2,existingCoeCoreTeam.getCenterOfExcellence().getId());
	}

	@Test
	void testUpdateCoeCoreTeam_whenInvalidTeamName_ThrowException() {
		employeeOne = new EmployeeModel();
		employeeOne.setId(1);
		CenterOfExcellenceModel centerOfExcellenceModel = new CenterOfExcellenceModel();
		centerOfExcellenceModel.setId(2);
		Integer coeCoreTeamId = 1;
		CoeCoreTeamModel coeCoreTeamModel = new CoeCoreTeamModel();
		coeCoreTeamModel.setId(coeCoreTeamId);
		coeCoreTeamModel.setCode("0123");
		coeCoreTeamModel.setName("UXUI");	//New Team name
		coeCoreTeamModel.setSubLeader(employeeOne);
		coeCoreTeamModel.setStatus(1);
		coeCoreTeamModel.setCenterOfExcellence(centerOfExcellenceModel);


		CenterOfExcellence centerOfExcellence = new CenterOfExcellence();
		centerOfExcellence.setId(centerOfExcellenceModel.getId());
		CoeCoreTeam existingCoeCoreTeam = new CoeCoreTeam();
		existingCoeCoreTeam.setId(1);
		existingCoeCoreTeam.setCode("UXI");
		existingCoeCoreTeam.setName("UX");	//Old Team name
		existingCoeCoreTeam.setStatus(coeCoreTeamModel.getStatus());
		existingCoeCoreTeam.setSubLeaderId(1);
		existingCoeCoreTeam.setCenterOfExcellence(centerOfExcellence);

		CoeCoreTeam defaultCoeCoreTeam = new CoeCoreTeam();
		defaultCoeCoreTeam.setId(2);

		Optional<EmployeeStatus> lastStatusOptional = Optional.of(new EmployeeStatus());
		lastStatusOptional.get().setStatus(StatusConstant.STATUS_ACTIVE);

		Mockito.when(coeCoreTeamRepository.findByCode(anyString())).thenReturn(defaultCoeCoreTeam);
		Mockito.when(coeCoreTeamRepository.findById(anyInt())).thenReturn(Optional.of(existingCoeCoreTeam));
		Mockito.when(coeCoreTeamRepository.existsBySubLeaderId(anyInt())).thenReturn(false);
		Mockito.when(coeCoreTeamRepository.existsByNameIgnoreCase(anyString())).thenReturn(true);	// Team name is already existed
		Mockito.when(centerOfExcellenceRepository.findById(anyInt())).thenReturn(Optional.of(centerOfExcellence));
		Mockito.when(coeCoreTeamRepository.save(existingCoeCoreTeam)).thenReturn(existingCoeCoreTeam);
		Mockito.when(employeeStatusRepository.findFirstByEmployeeIdOrderByStatusDateDesc(anyInt())).thenReturn(lastStatusOptional);
		

		CoEException throwable = assertThrows(CoEException.class,
				() -> coeCoreTeamService.updateCoeCoreTeam(coeCoreTeamModel));

		assertEquals(CoEException.class, throwable.getClass());
		assertEquals(ErrorConstant.MESSAGE_COE_CORE_TEAM_NAME_EXISTED, throwable.getMessage());
	}

	@Test
	void testUpdateCoeCoreTeam_whenInvalidSubLeaderId_ThrowException() {
		employeeOne = new EmployeeModel();
		employeeOne.setId(1);	//New SubLeaderId
		CenterOfExcellenceModel centerOfExcellenceModel = new CenterOfExcellenceModel();
		centerOfExcellenceModel.setId(2);
		Integer coeCoreTeamId = 1;
		CoeCoreTeamModel coeCoreTeamModel = new CoeCoreTeamModel();
		coeCoreTeamModel.setId(coeCoreTeamId);
		coeCoreTeamModel.setCode("0123");
		coeCoreTeamModel.setName("UXUI");
		coeCoreTeamModel.setSubLeader(employeeOne);
		coeCoreTeamModel.setStatus(1);
		coeCoreTeamModel.setCenterOfExcellence(centerOfExcellenceModel);


		CenterOfExcellence centerOfExcellence = new CenterOfExcellence();
		centerOfExcellence.setId(centerOfExcellenceModel.getId());
		CoeCoreTeam existingCoeCoreTeam = new CoeCoreTeam();
		existingCoeCoreTeam.setId(1);
		existingCoeCoreTeam.setCode(coeCoreTeamModel.getCode());
		existingCoeCoreTeam.setName("UXUI");
		existingCoeCoreTeam.setStatus(coeCoreTeamModel.getStatus());
		existingCoeCoreTeam.setSubLeaderId(3);	//Old SubLeaderId
		existingCoeCoreTeam.setCenterOfExcellence(centerOfExcellence);
		
		CoeCoreTeam defaultCoeCoreTeam = new CoeCoreTeam();
		defaultCoeCoreTeam.setId(2);

		Optional<EmployeeStatus> lastStatusOptional = Optional.of(new EmployeeStatus());
		lastStatusOptional.get().setStatus(StatusConstant.STATUS_ACTIVE);

		Mockito.when(coeCoreTeamRepository.findByCode(anyString())).thenReturn(defaultCoeCoreTeam);
		Mockito.when(coeCoreTeamRepository.findById(anyInt())).thenReturn(Optional.of(existingCoeCoreTeam));
		Mockito.when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(new Employee()));
		Mockito.when(coeCoreTeamRepository.existsBySubLeaderId(anyInt())).thenReturn(true);	//SubLeaderId is already assigned to another team
		Mockito.when(coeCoreTeamRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);
		Mockito.when(centerOfExcellenceRepository.findById(anyInt())).thenReturn(Optional.of(centerOfExcellence));
		Mockito.when(coeCoreTeamRepository.save(existingCoeCoreTeam)).thenReturn(existingCoeCoreTeam);
		Mockito.when(employeeStatusRepository.findFirstByEmployeeIdOrderByStatusDateDesc(anyInt())).thenReturn(lastStatusOptional);

		CoEException throwable = assertThrows(CoEException.class,
				() -> coeCoreTeamService.updateCoeCoreTeam(coeCoreTeamModel));

		assertEquals(CoEException.class, throwable.getClass());
		assertEquals(ErrorConstant.MESSAGE_SUBLEADER_UNAVAILABLE, throwable.getMessage());
	}

	@Test
	void testDeleteCoeCoreTeam_success() {
		Integer deleteId = 1;
		CoeCoreTeam coeCoreTeamMock = new CoeCoreTeam();
		coeCoreTeamMock.setId(1);
		coeCoreTeamMock.setStatus(StatusConstant.STATUS_ACTIVE);

		when(coeCoreTeamRepository.findById(anyInt())).thenReturn(Optional.of(coeCoreTeamMock));
		when(coeCoreTeamRepository.save(coeCoreTeamMock)).thenReturn(coeCoreTeamMock);
		
		boolean result = coeCoreTeamService.deleteCoeCoreTeam(deleteId);

		assertTrue(result);
		assertEquals(StatusConstant.STATUS_DELETED, coeCoreTeamMock.getStatus());
	}

	@Test
	void testDeleteCoeCoreTeam_whenDuplicateStatus_thenThrowException() {
		Integer deleteId = 1;

		CoeCoreTeam coeCoreTeamMock = new CoeCoreTeam();
		coeCoreTeamMock.setId(1);
		coeCoreTeamMock.setStatus(StatusConstant.STATUS_DELETED);

		when(coeCoreTeamRepository.findById(deleteId)).thenReturn(Optional.of(coeCoreTeamMock));

		CoEException throwable = assertThrows(CoEException.class, () -> coeCoreTeamService.deleteCoeCoreTeam(deleteId));
		assertEquals(ErrorConstant.MESSAGE_COE_CORE_TEAM_STATUS_DUPLICATE, throwable.getMessage());
	}

	@Test
	void testDeleteCoeCoreTeam_whenDeleteIdNotFound_theThrowException() {
		Integer deleteId = 1;
		when(coeCoreTeamRepository.findById(anyInt())).thenReturn(Optional.ofNullable(null));

		CoEException throwable = assertThrows(CoEException.class, () -> coeCoreTeamService.deleteCoeCoreTeam(deleteId));
		assertEquals(ErrorConstant.MESSAGE_COE_CORE_TEAM_NOT_FOUND, throwable.getMessage());
	}

	@Test
	void testAddMembersToCoeCoreTeam_whenAllParamsValid_ThenSuccess() {
		List<Integer> employeeListId = new ArrayList<>();
		employeeListId.add(1);
		employeeListId.add(2);
		System.out.println(employeeListId);
		List<Employee> employees = new ArrayList<>();
		employees.add(new Employee());
		employees.add(new Employee());

		CoeCoreTeam existingCoeCoreTeam = new CoeCoreTeam();
		existingCoeCoreTeam.setStatus(StatusConstant.STATUS_ACTIVE);
		// Mock the behavior of repositories
		when(employeeRepository.findAllById(employeeListId)).thenReturn(employees);
		when(coeCoreTeamRepository.findById(any())).thenReturn(Optional.of(existingCoeCoreTeam));
		when(employeeRepository.saveAll(employees)).thenReturn(employees);

		int addCount = coeCoreTeamService.addMembersToCoeCoreTeam(1, employeeListId);
		assertEquals(2, addCount);

		for (Employee emp : employees) {
			assertEquals(existingCoeCoreTeam, emp.getCoeCoreTeam());
		}
	}

	@Test
	void testAddMembersToCoeCoreTeam_whenEmployeeIdsEmpty_ThenThrowException() {
		List<Integer> employeeListId = new ArrayList<>();

		CoEException throwable = assertThrows(CoEException.class, () -> coeCoreTeamService.addMembersToCoeCoreTeam(1, employeeListId));
		assertEquals(ErrorConstant.MESSAGE_EMPLOYEE_LIST_ID_NOT_EMPTY, throwable.getMessage());
	}

	@Test
	void testAddMembersToCoeCoreTeam_whenEmployeeIdsNotFound_ThenThrowException() {
		List<Integer> employeeListId = new ArrayList<>();
		employeeListId.add(1);
		employeeListId.add(2);
		System.out.println(employeeListId);
		List<Employee> employees = new ArrayList<>();	//Empty list
	
		// Mock the behavior of repositories
		when(employeeRepository.findAllById(employeeListId)).thenReturn(employees);	//Not found any employee, return empty list
		CoEException throwable = assertThrows(CoEException.class, () -> coeCoreTeamService.addMembersToCoeCoreTeam(1, employeeListId));
		assertEquals(ErrorConstant.MESSAGE_EMPLOYEE_NOT_FOUND, throwable.getMessage());
	}

	@Test
	void testAddMembersToCoeCoreTeam_whenTeamIdNotFound_ThenThrowException() {
		List<Integer> employeeListId = new ArrayList<>();
		employeeListId.add(1);
		employeeListId.add(2);
		System.out.println(employeeListId);
		List<Employee> employees = new ArrayList<>();
		employees.add(new Employee());
		employees.add(new Employee());
	
		// Mock the behavior of repositories
		when(employeeRepository.findAllById(employeeListId)).thenReturn(employees);
		when(coeCoreTeamRepository.findById(any())).thenReturn(Optional.empty());

		CoEException throwable = assertThrows(CoEException.class, () -> coeCoreTeamService.addMembersToCoeCoreTeam(1, employeeListId));
		assertEquals(ErrorConstant.MESSAGE_COE_CORE_TEAM_NOT_FOUND, throwable.getMessage());
	}

	@Test
	void testGetAllCoeTeamByCoeIdAndStatus_whenInvalidCoeId_thenReturnEmptyList() {
		Integer coeId = 1;
		
		Mockito.when(centerOfExcellenceRepository.getCenterOfExcellencesById(coeId)).thenReturn(null);
		
		List<CoeCoreTeamModel> result = coeCoreTeamService.getAllCoeTeamByCoeIdAndStatus(coeId, 1);
		Assertions.assertNotNull(result);
		Assertions.assertTrue(result.isEmpty());
	}

	@Test
	void testGetAllCoeTeamByCoeIdAndStatus_whenValidCoeId_thenReturnListModel() {
		Integer coeId = 1;
		CenterOfExcellence coe = new CenterOfExcellence();
		Mockito.when(centerOfExcellenceRepository.getCenterOfExcellencesById(coeId)).thenReturn(coe);
		List<CoeCoreTeam> coeCoreTeams = new ArrayList<>();
		Mockito.when(coeCoreTeamRepository.getAllByCenterOfExcellenceIdAndStatus(coeId, 1)).thenReturn(coeCoreTeams);
		CoeCoreTeamModel test1 = new CoeCoreTeamModel();
		test1.setCode("177013");
		CoeCoreTeamModel test2 = new CoeCoreTeamModel();
		test2.setCode("4299");
		CoeCoreTeamModel test3 = new CoeCoreTeamModel();
		test3.setCode("228922");
		List<CoeCoreTeamModel> transformedModels = new ArrayList<>(Arrays.asList(test1, test2, test3));
		Mockito.when(coeCoreTeamTransformer.applyList(coeCoreTeams)).thenReturn(transformedModels);
		List<CoeCoreTeamModel> result = coeCoreTeamService.getAllCoeTeamByCoeIdAndStatus(coeId, 1);
		Assertions.assertEquals(transformedModels, result);
		Assertions.assertEquals("177013", result.get(0).getCode());
	}

	@Test
	void testGetAllCoeTeam_whenNoCoeCoreTeamExist_thenReturnEmptyList() {
		Mockito.when(coeCoreTeamRepository.findAllByStatus(1)).thenReturn(new ArrayList<>());	//No Team exists
		
		List<CoeCoreTeamModel> result = coeCoreTeamService.getAllCoeTeamByStatus(1);
		Assertions.assertNotNull(result);
		Assertions.assertTrue(result.isEmpty());
	}

	@Test
	void testGetAllCoeTeamByCoeId_whenCoeCoreTeamExist_thenReturnListModel() {
		List<CoeCoreTeam> coeCoreTeams = new ArrayList<>(Arrays.asList(new CoeCoreTeam(), new CoeCoreTeam(), new CoeCoreTeam()));
	
		CoeCoreTeamModel test1 = new CoeCoreTeamModel();
		test1.setCode("177013");
		CoeCoreTeamModel test2 = new CoeCoreTeamModel();
		test2.setCode("4299");
		CoeCoreTeamModel test3 = new CoeCoreTeamModel();
		test3.setCode("228922");
		List<CoeCoreTeamModel> transformedModels = new ArrayList<>(Arrays.asList(test1, test2, test3));

		Mockito.when(coeCoreTeamRepository.findAllByStatus(1)).thenReturn(coeCoreTeams);
		Mockito.when(coeCoreTeamTransformer.applyList(coeCoreTeams)).thenReturn(transformedModels);

		List<CoeCoreTeamModel> result = coeCoreTeamService.getAllCoeTeamByStatus(1);

		Assertions.assertEquals(transformedModels, result);
		Assertions.assertEquals("4299", result.get(1).getCode());
	}

	@Test
	void testGetCoeCoreTeamByTeamId_whenExistTeam_ThenSuccess() {
		CoeCoreTeam team = new CoeCoreTeam();
		team.setId(1);

		CoeCoreTeamModel teamMd = new CoeCoreTeamModel();
		teamMd.setId(1);

		when(coeCoreTeamRepository.findById(1)).thenReturn(Optional.of(team));
		when(coeCoreTeamTransformer.apply(team)).thenReturn(teamMd);

		CoeCoreTeamModel result = coeCoreTeamService.getCoeCoreTeamByTeamId(1);
		assertNotNull(result);
		assertEquals(teamMd, result);
	}

	@Test
	void testGetCoeCoreTeamByTeamId_whenNoExistTeam_ThenThrowException(){
		CoEException throwable = assertThrows(CoEException.class,
				() -> coeCoreTeamService.getCoeCoreTeamByTeamId(1));

		assertEquals(CoEException.class, throwable.getClass());
		assertEquals(ErrorConstant.MESSAGE_COE_CORE_TEAM_NOT_FOUND, throwable.getMessage());
	}

	@Test
	void testSearchCoeCoreTeam_withKeyWord_returnAccess() {
		String keyword = "thuy";
		Integer coeId = 1;
		Integer status = 1;
		String sortBy = "name";
		Integer no = 1;
		Integer limit = 5;
		Boolean desc = true;

		when(iCoeCoreTeamModelOne.getId()).thenReturn(1);
		when(iCoeCoreTeamModelOne.getCode()).thenReturn("BE");
		when(iCoeCoreTeamModelOne.getName()).thenReturn("Back-end");
		when(iCoeCoreTeamModelOne.getLeaderName()).thenReturn("Thuy Le");
		when(iCoeCoreTeamModelOne.getLeaderEmail()).thenReturn("thuyle2@gmail.com");
		when(iCoeCoreTeamModelOne.getCoeId()).thenReturn(1);
		when(iCoeCoreTeamModelOne.getStatus()).thenReturn(1);

		when(iCoeCoreTeamModelTwo.getId()).thenReturn(2);
		when(iCoeCoreTeamModelTwo.getCode()).thenReturn("FE");
		when(iCoeCoreTeamModelTwo.getName()).thenReturn("Front-end");
		when(iCoeCoreTeamModelTwo.getLeaderName()).thenReturn("Thuy Le");
		when(iCoeCoreTeamModelTwo.getLeaderEmail()).thenReturn("thuyle3@gmail.com");
		when(iCoeCoreTeamModelTwo.getCoeId()).thenReturn(1);
		when(iCoeCoreTeamModelTwo.getStatus()).thenReturn(1);

		List<ICoeCoreTeamSearch> team = Arrays.asList(iCoeCoreTeamModelOne, iCoeCoreTeamModelTwo);

		when(coeCoreTeamRepository.searchCoeCoreTeam(any(String.class), any(Integer.class), any(Integer.class), any(PageRequest.class))).thenReturn(team);

		Page<ICoeCoreTeamSearch> result = coeCoreTeamService.searchCoeCoreTeam(keyword, coeId, status, no, limit, sortBy, desc);

		assertNotNull(result);
		assertEquals(result.getContent().get(0).getId(), team.get(0).getId());
		assertEquals(result.getContent().get(0).getCode(), team.get(0).getCode());
		assertEquals(result.getContent().get(0).getName(), team.get(0).getName());
		assertEquals(result.getContent().get(0).getLeaderName(), team.get(0).getLeaderName());
		assertEquals(result.getContent().get(0).getLeaderEmail(), team.get(0).getLeaderEmail());
		assertEquals(result.getContent().get(0).getCoeId(), team.get(0).getCoeId());
		assertEquals(result.getContent().get(0).getStatus(), team.get(0).getStatus());

		assertEquals(result.getContent().get(1).getId(), team.get(1).getId());
		assertEquals(result.getContent().get(1).getCode(), team.get(1).getCode());
		assertEquals(result.getContent().get(1).getName(), team.get(1).getName());
		assertEquals(result.getContent().get(1).getLeaderName(), team.get(1).getLeaderName());
		assertEquals(result.getContent().get(1).getLeaderEmail(), team.get(1).getLeaderEmail());
		assertEquals(result.getContent().get(1).getCoeId(), team.get(1).getCoeId());
		assertEquals(result.getContent().get(1).getStatus(), team.get(1).getStatus());
	}

	@Test
	void testSearchCoeCoreTeam_withEmptySearchKeyword_returnAllAccess(){
		String keyword = null;
		Integer coeId = 1;
		Integer status = 1;
		String sortBy = "name";
		Integer no = 1;
		Integer limit = 5;
		Boolean desc = true;

		List<ICoeCoreTeamSearch> team = Collections.emptyList();

		when(coeCoreTeamRepository.searchCoeCoreTeam(any(String.class), any(Integer.class), any(Integer.class), any(PageRequest.class))).thenReturn(team);

		Page<ICoeCoreTeamSearch> result = coeCoreTeamService.searchCoeCoreTeam(keyword, coeId, status, no, limit, sortBy, desc);

		assertNotNull(result);
	}

	@Test
	void testSearchCoeCoreTeam_withInvalidKeyword_thenReturnEmptyPage(){
		String keyword = "thuy";
		Integer coeId = 1;
		Integer status = 1;
		String sortBy = "name";
		Integer no = 1;
		Integer limit = 5;
		Boolean desc = true;

		Sort sort = Sort.by(sortBy);
		Pageable pageable = PageRequest.of(0, 10, sort);

		List<ICoeCoreTeamSearch> team = Collections.emptyList();

		when(coeCoreTeamRepository.searchCoeCoreTeam(keyword, coeId, status, pageable)).thenReturn(team);

		Page<ICoeCoreTeamSearch> result = coeCoreTeamService.searchCoeCoreTeam(keyword, coeId, status, no, limit, sortBy, desc);

		assertNotNull(result);
		assertTrue(result.isEmpty());
		assertEquals(0, result.getTotalElements());
		assertEquals(0, result.getContent().size());
	}

	@Test
	void testCreateCoeCoreTeam_withValidModel_ThenSuccess(){
		employeeOne = new EmployeeModel();
		employeeOne.setId(1);
		coeOne = new CenterOfExcellenceModel();
		coeOne.setId(2);
		Integer coeCoreTeamId = 1;
		coeCoreTeamModel = new CoeCoreTeamModel();
		coeCoreTeamModel.setId(coeCoreTeamId);
		coeCoreTeamModel.setCode("0123");
		coeCoreTeamModel.setName("UXUI");
		coeCoreTeamModel.setSubLeader(employeeOne);
		coeCoreTeamModel.setStatus(1);
		coeCoreTeamModel.setCenterOfExcellence(coeOne);
		

		coe = new CenterOfExcellence();
		coe.setId(coeOne.getId());
		CoeCoreTeam coeCoreTeam = new CoeCoreTeam();
		coeCoreTeam.setId(1);
		coeCoreTeam.setCode(coeCoreTeamModel.getCode());
		coeCoreTeam.setName(coeCoreTeamModel.getName().trim());
		coeCoreTeam.setStatus(coeCoreTeamModel.getStatus());
		coeCoreTeam.setSubLeaderId(1);
		coeCoreTeam.setCenterOfExcellence(coe);

		Optional<EmployeeStatus> lastStatusOptional = Optional.of(new EmployeeStatus());
		lastStatusOptional.get().setStatus(StatusConstant.STATUS_ACTIVE);
		
		Mockito.when(coeCoreTeamRepository.findByCode(anyString())).thenReturn(null);
		Mockito.when(coeCoreTeamRepository.existsBySubLeaderId(anyInt())).thenReturn(false);
		Mockito.when(coeCoreTeamRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);
		Mockito.when(centerOfExcellenceRepository.findById(anyInt())).thenReturn(Optional.of(coe));
		Mockito.when(coeCoreTeamRepository.save(any(CoeCoreTeam.class))).thenReturn(coeCoreTeam);
		Mockito.when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(new Employee()));
		Mockito.when(coeCoreTeamModelTransformer.apply(coeCoreTeamModel)).thenReturn(coeCoreTeam);
		Mockito.when(employeeStatusRepository.findFirstByEmployeeIdOrderByStatusDateDesc(anyInt())).thenReturn(lastStatusOptional);

		Integer expectedResult = coeCoreTeamService.createCoeCoreTeam(coeCoreTeamModel);

		assertNotNull(expectedResult);
		assertEquals(coeCoreTeam.getId(), expectedResult);
	}

	@Test
	void testCreateCoeCoreTeam_whenInvalidCodeTeam_ThrowException() {
		employeeOne = new EmployeeModel();
		employeeOne.setId(1);
		CenterOfExcellenceModel centerOfExcellenceModel = new CenterOfExcellenceModel();
		centerOfExcellenceModel.setId(2);
		Integer coeCoreTeamId = 1;
		CoeCoreTeamModel coeCoreTeamModel = new CoeCoreTeamModel();
		coeCoreTeamModel.setId(coeCoreTeamId);
		coeCoreTeamModel.setCode("0123");
		coeCoreTeamModel.setName("UXUI");
		coeCoreTeamModel.setSubLeader(employeeOne);
		coeCoreTeamModel.setStatus(1);
		coeCoreTeamModel.setCenterOfExcellence(centerOfExcellenceModel);


		CenterOfExcellence centerOfExcellence = new CenterOfExcellence();
		centerOfExcellence.setId(centerOfExcellenceModel.getId());
	
		
		Mockito.when(coeCoreTeamRepository.findByCode(anyString())).thenReturn(new CoeCoreTeam());	// Team code is already existed
		Mockito.when(coeCoreTeamRepository.existsBySubLeaderId(anyInt())).thenReturn(false);
		Mockito.when(coeCoreTeamRepository.existsByNameIgnoreCase(anyString())).thenReturn(true);
		Mockito.when(centerOfExcellenceRepository.findById(anyInt())).thenReturn(Optional.of(centerOfExcellence));
		Mockito.when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(new Employee()));
		
		CoEException throwable = assertThrows(CoEException.class,
				() -> coeCoreTeamService.createCoeCoreTeam(coeCoreTeamModel));

		assertEquals(CoEException.class, throwable.getClass());
		assertEquals(ErrorConstant.MESSAGE_COE_CORE_TEAM_ALREADY_EXISTS, throwable.getMessage());
	}
	
	@Test
	void testCreateCoeCoreTeam_whenInvalidTeamName_ThrowException() {
		employeeOne = new EmployeeModel();
		employeeOne.setId(1);
		CenterOfExcellenceModel centerOfExcellenceModel = new CenterOfExcellenceModel();
		centerOfExcellenceModel.setId(2);
		Integer coeCoreTeamId = 1;
		CoeCoreTeamModel coeCoreTeamModel = new CoeCoreTeamModel();
		coeCoreTeamModel.setId(coeCoreTeamId);
		coeCoreTeamModel.setCode("0123");
		coeCoreTeamModel.setName("UXUI");	//New Team name
		coeCoreTeamModel.setSubLeader(employeeOne);
		coeCoreTeamModel.setStatus(1);
		coeCoreTeamModel.setCenterOfExcellence(centerOfExcellenceModel);


		CenterOfExcellence centerOfExcellence = new CenterOfExcellence();
		centerOfExcellence.setId(centerOfExcellenceModel.getId());
	
		
		Mockito.when(coeCoreTeamRepository.findByCode(anyString())).thenReturn(null);
		Mockito.when(coeCoreTeamRepository.existsBySubLeaderId(anyInt())).thenReturn(false);
		Mockito.when(coeCoreTeamRepository.existsByNameIgnoreCase(anyString())).thenReturn(true);	// Team name is already existed
		Mockito.when(centerOfExcellenceRepository.findById(anyInt())).thenReturn(Optional.of(centerOfExcellence));
		Mockito.when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(new Employee()));
		
		CoEException throwable = assertThrows(CoEException.class,
				() -> coeCoreTeamService.createCoeCoreTeam(coeCoreTeamModel));

		assertEquals(CoEException.class, throwable.getClass());
		assertEquals(ErrorConstant.MESSAGE_COE_CORE_TEAM_NAME_EXISTED, throwable.getMessage());
	}
	
	@Test
	void testCreateCoeCoreTeam_whenSubLeaderIdExist_ThrowException() {
		employeeOne = new EmployeeModel();
		employeeOne.setId(1);
		CenterOfExcellenceModel centerOfExcellenceModel = new CenterOfExcellenceModel();
		centerOfExcellenceModel.setId(2);
		Integer coeCoreTeamId = 1;
		CoeCoreTeamModel coeCoreTeamModel = new CoeCoreTeamModel();
		coeCoreTeamModel.setId(coeCoreTeamId);
		coeCoreTeamModel.setCode("0123");
		coeCoreTeamModel.setName("UXUI");
		coeCoreTeamModel.setSubLeader(employeeOne);
		coeCoreTeamModel.setStatus(1);
		coeCoreTeamModel.setCenterOfExcellence(centerOfExcellenceModel);


		CenterOfExcellence centerOfExcellence = new CenterOfExcellence();
		centerOfExcellence.setId(centerOfExcellenceModel.getId());
	
		
		Mockito.when(coeCoreTeamRepository.findByCode(anyString())).thenReturn(null);
		Mockito.when(coeCoreTeamRepository.existsBySubLeaderId(anyInt())).thenReturn(false);
		Mockito.when(coeCoreTeamRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);	
		Mockito.when(centerOfExcellenceRepository.findById(anyInt())).thenReturn(Optional.of(centerOfExcellence));
		Mockito.when(employeeRepository.findById(anyInt())).thenReturn(Optional.empty());	// SubLeaderId is not existed
		
		CoEException throwable = assertThrows(CoEException.class,
				() -> coeCoreTeamService.createCoeCoreTeam(coeCoreTeamModel));

		assertEquals(CoEException.class, throwable.getClass());
		assertEquals(ErrorConstant.MESSAGE_EMPLOYEE_DO_NOT_EXIST, throwable.getMessage());
	}
	
	@Test
	void testCreateCoeCoreTeam_whenSubLeaderIsAlreadyAssigned_ThrowException() {
		employeeOne = new EmployeeModel();
		employeeOne.setId(1);
		CenterOfExcellenceModel centerOfExcellenceModel = new CenterOfExcellenceModel();
		centerOfExcellenceModel.setId(2);
		Integer coeCoreTeamId = 1;
		CoeCoreTeamModel coeCoreTeamModel = new CoeCoreTeamModel();
		coeCoreTeamModel.setId(coeCoreTeamId);
		coeCoreTeamModel.setCode("0123");
		coeCoreTeamModel.setName("UXUI");
		coeCoreTeamModel.setSubLeader(employeeOne);
		coeCoreTeamModel.setStatus(1);
		coeCoreTeamModel.setCenterOfExcellence(centerOfExcellenceModel);


		CenterOfExcellence centerOfExcellence = new CenterOfExcellence();
		centerOfExcellence.setId(centerOfExcellenceModel.getId());

		Optional<EmployeeStatus> lastStatusOptional = Optional.of(new EmployeeStatus());
		lastStatusOptional.get().setStatus(StatusConstant.STATUS_ACTIVE);
	
		Mockito.when(coeCoreTeamRepository.findByCode(anyString())).thenReturn(null);
		Mockito.when(coeCoreTeamRepository.existsBySubLeaderId(anyInt())).thenReturn(true);	// SubLeaderId is already assigned
		Mockito.when(coeCoreTeamRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);	
		Mockito.when(centerOfExcellenceRepository.findById(anyInt())).thenReturn(Optional.of(centerOfExcellence));
		Mockito.when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(new Employee()));
		Mockito.when(employeeStatusRepository.findFirstByEmployeeIdOrderByStatusDateDesc(anyInt())).thenReturn(lastStatusOptional);
		
		CoEException throwable = assertThrows(CoEException.class,
				() -> coeCoreTeamService.createCoeCoreTeam(coeCoreTeamModel));

		assertEquals(CoEException.class, throwable.getClass());
		assertEquals(ErrorConstant.MESSAGE_SUBLEADER_UNAVAILABLE, throwable.getMessage());
	}
	
	@Test
	void testCreateCoeCoreTeam_whenCoeNotExist_ThrowException() {
		employeeOne = new EmployeeModel();
		employeeOne.setId(1);
		CenterOfExcellenceModel centerOfExcellenceModel = new CenterOfExcellenceModel();
		centerOfExcellenceModel.setId(2);
		Integer coeCoreTeamId = 1;
		CoeCoreTeamModel coeCoreTeamModel = new CoeCoreTeamModel();
		coeCoreTeamModel.setId(coeCoreTeamId);
		coeCoreTeamModel.setCode("0123");
		coeCoreTeamModel.setName("UXUI");
		coeCoreTeamModel.setSubLeader(employeeOne);
		coeCoreTeamModel.setStatus(1);
		coeCoreTeamModel.setCenterOfExcellence(centerOfExcellenceModel);
	
		Mockito.when(coeCoreTeamRepository.findByCode(anyString())).thenReturn(null);
		Mockito.when(coeCoreTeamRepository.existsBySubLeaderId(anyInt())).thenReturn(true);
		Mockito.when(coeCoreTeamRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);	
		Mockito.when(centerOfExcellenceRepository.findById(anyInt())).thenReturn(Optional.empty());	// Center Of Excellence is not existed
		Mockito.when(employeeRepository.findById(anyInt())).thenReturn(Optional.empty());
		
		CoEException throwable = assertThrows(CoEException.class,
				() -> coeCoreTeamService.createCoeCoreTeam(coeCoreTeamModel));

		assertEquals(CoEException.class, throwable.getClass());
		assertEquals(ErrorConstant.MESSAGE_CENTER_OF_EXCELLENCE_NOT_FOUND, throwable.getMessage());
	}

	@Test
	void testCreateCoeCoreTeam_withNoneFieldFilled_throwException()
	{
		NullPointerException exception = assertThrows(NullPointerException.class, () -> { coeCoreTeamService.createCoeCoreTeam(null);
		});
		assertEquals(NullPointerException.class, exception.getClass());
		assertNotEquals(ErrorConstant.MESSAGE_FIELD_REQUIRED, exception.getMessage());
	}

	@Test
	void testIsCoeCoreTeamValid_withAllValidParams_thenReturnAllNullMessages(){
		Mockito.when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(new Employee()));
		Mockito.when(coeCoreTeamRepository.existsBySubLeaderId(anyInt())).thenReturn(false);	
		Mockito.when(coeCoreTeamRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);
		Mockito.when(coeCoreTeamRepository.existsByCodeIgnoreCase(anyString())).thenReturn(false);
		List<String> result = coeCoreTeamService.validateCoeCoreTeam("Golang BackEnd", 11, "GB");
		assertNull(result.get(0));	//Valid input, return null
		assertNull(result.get(1));	//Valid input, return null
		assertNull(result.get(2));	//Valid input, return null
	}

	@Test
	void testIsCoeCoreTeamValid_withSubLeaderIdNotExist_thenReturnErrorMessage(){
		Mockito.when(employeeRepository.findById(anyInt())).thenReturn(Optional.empty());	//SubLeaderId is not exist
		Mockito.when(coeCoreTeamRepository.existsBySubLeaderId(anyInt())).thenReturn(false);
		Mockito.when(coeCoreTeamRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);
		Mockito.when(coeCoreTeamRepository.existsByCodeIgnoreCase(anyString())).thenReturn(false);
		
		List<String> result = coeCoreTeamService.validateCoeCoreTeam("Golang BackEnd", 11, "GB");

		assertEquals(ErrorConstant.MESSAGE_EMPLOYEE_DO_NOT_EXIST, result.get(1));
		assertNull(result.get(0));
		assertNull(result.get(2));
	}

	@Test
	void testIsCoeCoreTeamValid_withSubLeaderIsAlreadyAssigned_thenReturnErrorMessage(){
		Mockito.when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(new Employee()));
		Mockito.when(coeCoreTeamRepository.existsBySubLeaderId(anyInt())).thenReturn(true);	//SubLeaderId is already assigned to another team
		Mockito.when(coeCoreTeamRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);
		Mockito.when(coeCoreTeamRepository.existsByCodeIgnoreCase(anyString())).thenReturn(false);
		
		List<String> result = coeCoreTeamService.validateCoeCoreTeam("Golang BackEnd", 11, "GB");

		assertEquals(ErrorConstant.MESSAGE_SUBLEADER_UNAVAILABLE, result.get(1));
		assertNull(result.get(0));
		assertNull(result.get(2));
	}

	@Test
	void testIsCoeCoreTeamValid_withTeamNameExist_thenReturnErrorMessage(){
		Mockito.when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(new Employee()));
		Mockito.when(coeCoreTeamRepository.existsBySubLeaderId(anyInt())).thenReturn(false);	
		Mockito.when(coeCoreTeamRepository.existsByNameIgnoreCase(anyString())).thenReturn(true);	//Teamname is existed
		Mockito.when(coeCoreTeamRepository.existsByCodeIgnoreCase(anyString())).thenReturn(false);
		
		List<String> result = coeCoreTeamService.validateCoeCoreTeam("Golang BackEnd", 11, "GB");

		assertEquals(ErrorConstant.MESSAGE_COE_CORE_TEAM_NAME_EXISTED, result.get(0));
		assertNull(result.get(1));
		assertNull(result.get(2));
	}

	@Test
	void testIsCoeCoreTeamValid_withTeamCodeExist_thenReturnErrorMessage(){
		Mockito.when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(new Employee()));
		Mockito.when(coeCoreTeamRepository.existsBySubLeaderId(anyInt())).thenReturn(false);	
		Mockito.when(coeCoreTeamRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);
		Mockito.when(coeCoreTeamRepository.existsByCodeIgnoreCase(anyString())).thenReturn(true);	//Team code is existed
		
		List<String> result = coeCoreTeamService.validateCoeCoreTeam("Golang BackEnd", 11, "GB");

		assertNull(result.get(0));
		assertNull(result.get(1));
		assertEquals(ErrorConstant.MESSAGE_COE_CORE_TEAM_CODE_EXISTED, result.get(2));
	}

	@Test
	void testIsCoeCoreTeamValid_withLeaderIdValidAndOthersNull_thenReturnNullessage(){
		Mockito.when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(new Employee()));
		Mockito.when(coeCoreTeamRepository.existsBySubLeaderId(anyInt())).thenReturn(false);	

		List<String> result = coeCoreTeamService.validateCoeCoreTeam(null, 11, null);

		assertNull(result.get(0));	//Null input, return null
		assertNull(result.get(1));	//Valid input, return null
		assertNull(result.get(2));	//Null input, return null
	}


	@Test
	void testIsCoeCoreTeamValid_withTeamNameValidAndOthersNull_thenReturnNullMessage(){
		Mockito.when(coeCoreTeamRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);

		List<String> result = coeCoreTeamService.validateCoeCoreTeam("Golang BackEnd", null, null);

		assertNull(result.get(0));	//Valid input, return null
		assertNull(result.get(1));	//Null input, return null
		assertNull(result.get(2));	//Null input, return null
	}

	@Test
	void testIsCoeCoreTeamValid_withTeamCodeValidAndOthersNull_thenReturnNullMessage(){
		Mockito.when(coeCoreTeamRepository.existsByCodeIgnoreCase(anyString())).thenReturn(false);

		List<String> result = coeCoreTeamService.validateCoeCoreTeam(null, null, "GB");

		assertNull(result.get(0));	//Null input, return null
		assertNull(result.get(1));	//Null input, return null
		assertNull(result.get(2));	//Valid input, return null
	}
}