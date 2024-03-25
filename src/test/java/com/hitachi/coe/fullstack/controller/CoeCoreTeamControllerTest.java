package com.hitachi.coe.fullstack.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.exceptions.CoEException;
import com.hitachi.coe.fullstack.model.CenterOfExcellenceModel;
import com.hitachi.coe.fullstack.model.CoeCoreTeamModel;
import com.hitachi.coe.fullstack.model.EmployeeModel;
import com.hitachi.coe.fullstack.model.ICoeCoreTeamSearch;
import com.hitachi.coe.fullstack.model.common.BaseResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.hitachi.coe.fullstack.service.CoeCoreTeamService;

import lombok.SneakyThrows;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("classpath:application-data-test.properties")
class CoeCoreTeamControllerTest {
	@Autowired
	private MockMvc mvc;

	@MockBean
	private CoeCoreTeamService coeCoreTeamService;

	@Autowired
	private CoeCoreTeamController coeCoreTeamController;

	CoeCoreTeamModel coeCoreTeamModel;

	EmployeeModel employeeOne;

	CenterOfExcellenceModel coeOne;

	ICoeCoreTeamSearch iCoeCoreTeamModelOne;

	ICoeCoreTeamSearch iCoeCoreTeamModelTwo;

	@BeforeEach
	void setUp() {
		iCoeCoreTeamModelOne = mock(ICoeCoreTeamSearch.class);
		iCoeCoreTeamModelTwo = mock(ICoeCoreTeamSearch.class);

		coeCoreTeamModel = new CoeCoreTeamModel();

		employeeOne = new EmployeeModel();

		coeOne = new CenterOfExcellenceModel();
		coeOne.setCode("STA");
		coeOne.setName("Solution/Technical Architecture");

		coeCoreTeamModel.setCode("BE");
		coeCoreTeamModel.setName("Back-end");
		coeCoreTeamModel.setStatus(1);
		coeCoreTeamModel.setSubLeader(employeeOne);
		coeCoreTeamModel.setCenterOfExcellence(coeOne);
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	@SneakyThrows
	void testCreateCoeCoreTeam_whenValidCoeCoreTeamModel_ThenSuccess() {
		Integer coeCoreTeamId = 1;
		employeeOne.setId(1);
		coeCoreTeamModel.setId(coeCoreTeamId);

		when(coeCoreTeamService.createCoeCoreTeam(coeCoreTeamModel)).thenReturn(coeCoreTeamId);

		mvc.perform(MockMvcRequestBuilders.post("/api/v1/coe-core-team/create").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(coeCoreTeamModel)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(Integer.toString(coeCoreTeamId)))
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	@SneakyThrows
	void testCreateCoeCoreTeam_whenInvalidCoeCoreTeamModel_ThenReturnBadRequest() {
		employeeOne.setId(1);
		Integer coeCoreTeamId = 1;
		CoeCoreTeamModel coeCoreTeamModel = new CoeCoreTeamModel();
		coeCoreTeamModel.setId(coeCoreTeamId);
		coeCoreTeamModel.setCode("0123");
		coeCoreTeamModel.setName("Sang");
		coeCoreTeamModel.setSubLeader(employeeOne);
		coeCoreTeamModel.setStatus(1);
		// CoE is not set, then the model is invalid

		mvc.perform(MockMvcRequestBuilders.post("/api/v1/coe-core-team/create").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(coeCoreTeamModel)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].field").value("centerOfExcellence"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].message").value("must not be null"))
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	@SneakyThrows
	void testUpdateCoeCoreTeam_whenValidCoeCoreTeamModel_ThenSuccess() {
		Integer coeCoreTeamId = 1;
		employeeOne.setId(1);
		coeCoreTeamModel.setId(coeCoreTeamId);

		when(coeCoreTeamService.updateCoeCoreTeam(coeCoreTeamModel)).thenReturn(coeCoreTeamId);

		mvc.perform(MockMvcRequestBuilders.put("/api/v1/coe-core-team/update").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(coeCoreTeamModel)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(Integer.toString(coeCoreTeamId)))
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	@SneakyThrows
	void testUpdateCoeCoreTeam_whenInvalidCoeCoreTeamModel_ThenReturnBadRequest() {
		employeeOne.setId(1);
		Integer coeCoreTeamId = 1;
		CoeCoreTeamModel coeCoreTeamModel = new CoeCoreTeamModel();
		coeCoreTeamModel.setId(coeCoreTeamId);
		coeCoreTeamModel.setCode("0123");
		coeCoreTeamModel.setName("Sang");
		coeCoreTeamModel.setSubLeader(employeeOne);
		coeCoreTeamModel.setStatus(1);
		// CoE is not set, then the model is invalid

		mvc.perform(MockMvcRequestBuilders.put("/api/v1/coe-core-team/update").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(coeCoreTeamModel)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].field").value("centerOfExcellence"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].message").value("must not be null"))
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	@SneakyThrows
	void testAddMembersToCoeCoreTeam_whenSuccess_thenReturnSize() {
		String url = "/api/v1/coe-core-team//add-members/1";
		List<Integer> employeeIds = new ArrayList<>(Arrays.asList(1, 2, 3));
		when(coeCoreTeamService.addMembersToCoeCoreTeam(anyInt(), anyList())).thenReturn(employeeIds.size());
		mvc.perform(MockMvcRequestBuilders.put(url).contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(employeeIds))).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.size").value(employeeIds.size())).andReturn();
	}

	@Test
	@SneakyThrows
	void testRemoveMembersToCoeCoreTeam_whenSuccess_thenReturnSize() {
		String url = "/api/v1/coe-core-team/remove-members";
		List<Integer> employeeIds = new ArrayList<>(Arrays.asList(1, 2, 3));
		when(coeCoreTeamService.removeMembersFromCoeCoreTeam(anyList())).thenReturn(employeeIds.size());
		mvc.perform(MockMvcRequestBuilders.put(url).contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(employeeIds))).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.size").value(employeeIds.size())).andReturn();
	}

	@Test
	@SneakyThrows
	void testDeleteCoeCoreTeam_whenSuccess_thenReturnTrue() {
		String url = "/api/v1/coe-core-team/delete/2";
		when(coeCoreTeamService.deleteCoeCoreTeam(anyInt())).thenReturn(true);
		mvc.perform(MockMvcRequestBuilders.delete(url).contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.result").value(true)).andReturn();
	}

	@Test
	@SneakyThrows
	void testGetCoeCoreTeamByCoeId_whenSuccess_thenReturnList() {
		String url = "/api/v1/coe-core-team/list-coe-team/22";
		CoeCoreTeamModel coe1 = new CoeCoreTeamModel();
		CoeCoreTeamModel coe2 = new CoeCoreTeamModel();
		CoeCoreTeamModel coe3 = new CoeCoreTeamModel();
		List<CoeCoreTeamModel> mockCoe = new ArrayList<>();
		mockCoe.add(coe1);
		mockCoe.add(coe2);
		mockCoe.add(coe3);
		when(coeCoreTeamService.getAllCoeTeamByCoeIdAndStatus(anyInt(), anyInt())).thenReturn(mockCoe);
		mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty()).andReturn();
	}

	@Test
	@SneakyThrows
	void testgetCoeCoreTeam_whenSuccess_thenReturnList() {
		String url = "/api/v1/coe-core-team/list-coe-team";
		CoeCoreTeamModel coe1 = new CoeCoreTeamModel();
		CoeCoreTeamModel coe2 = new CoeCoreTeamModel();
		CoeCoreTeamModel coe3 = new CoeCoreTeamModel();
		List<CoeCoreTeamModel> mockCoe = new ArrayList<>();
		mockCoe.add(coe1);
		mockCoe.add(coe2);
		mockCoe.add(coe3);
		when(coeCoreTeamService.getAllCoeTeamByStatus(anyInt())).thenReturn(mockCoe);
		mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty()).andReturn();
	}

	@Test
    void testSearchTeamById_withGivenId_existingTeam() {
        when(coeCoreTeamService.getCoeCoreTeamByTeamId(1)).thenReturn(coeCoreTeamModel);

        BaseResponse<CoeCoreTeamModel> result = coeCoreTeamController.getCoeCoreTeamByTeamId(1);

        assertNotNull(result);
        assertEquals(HttpStatus.OK.value(), result.getStatus());
        assertNull(result.getMessage());
        assertNotNull(result.getData());
        assertEquals(coeCoreTeamModel, result.getData());
    }

	@Test
    void testSearchTeamById_withGivenId_nonExistingTeam() {
        when(coeCoreTeamService.getCoeCoreTeamByTeamId(1))
                .thenThrow(new CoEException(ErrorConstant.CODE_COE_CORE_TEAM_NOT_FOUND, ErrorConstant.MESSAGE_COE_CORE_TEAM_NOT_FOUND));

        CoEException exception = assertThrows(CoEException.class, () -> {
            coeCoreTeamService.getCoeCoreTeamByTeamId(1);
        });
        assertEquals(CoEException.class, exception.getClass());
        assertEquals(ErrorConstant.MESSAGE_COE_CORE_TEAM_NOT_FOUND, exception.getMessage());
    }

	@Test
    void testSearchTeamById_withNonGivenId_thenThrowException() {
        when(coeCoreTeamService.getCoeCoreTeamByTeamId(null))
                .thenThrow(new CoEException(ErrorConstant.CODE_COE_CORE_TEAM_ID_REQUIRED, ErrorConstant.MESSAGE_COE_CORE_TEAM_ID_REQUIRED));

        CoEException exception = assertThrows(CoEException.class, () -> {
            coeCoreTeamController.getCoeCoreTeamByTeamId(null);
        });
        assertEquals(CoEException.class, exception.getClass());
        assertEquals(ErrorConstant.MESSAGE_COE_CORE_TEAM_ID_REQUIRED, exception.getMessage());
    }

	@Test
	void testSearchTeamByConditions_withValidCoeId_returnAccess() {
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

        List<ICoeCoreTeamSearch> team = Arrays.asList(iCoeCoreTeamModelOne,iCoeCoreTeamModelTwo);
        Page<ICoeCoreTeamSearch> expectedResult = new PageImpl<>(team);

		when(coeCoreTeamService.searchCoeCoreTeam(anyString(), anyInt(), anyInt(), anyInt(), anyInt(), anyString(), anyBoolean()))
				.thenReturn(expectedResult);

        Page<ICoeCoreTeamSearch> result =  coeCoreTeamService.searchCoeCoreTeam("1", 1, 1, 1, 5, "name",false);

        assertNotNull(result);
        Assertions.assertEquals(expectedResult, result, "Found Team");

		verify(coeCoreTeamService).searchCoeCoreTeam("1", 1, 1, 1, 5, "name", false);
	}

	@Test
	void testSearchTeamByConditions_withEmptySearchKeyWord_returnAllAccess() {
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

        List<ICoeCoreTeamSearch> team = Arrays.asList(iCoeCoreTeamModelOne,iCoeCoreTeamModelTwo);
        Page<ICoeCoreTeamSearch> expectedResult = new PageImpl<>(team);

		when(coeCoreTeamService.searchCoeCoreTeam(eq(""), anyInt(), anyInt(), anyInt(), anyInt(), anyString(), anyBoolean()))
                .thenReturn(expectedResult);

        Page<ICoeCoreTeamSearch> result =  coeCoreTeamService.searchCoeCoreTeam("", 1, 1, 1, 5, "name",false);

		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals(team, result.getContent());

		verify(coeCoreTeamService).searchCoeCoreTeam("", 1, 1, 1, 5, "name",false);
	}

	@Test
	void testSearchTeamByConditions_withSearchKeyWord_returnSuccess(){
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

        List<ICoeCoreTeamSearch> team = Arrays.asList(iCoeCoreTeamModelOne,iCoeCoreTeamModelTwo);
        Page<ICoeCoreTeamSearch> expectedResult = new PageImpl<>(team);

        when(coeCoreTeamService.searchCoeCoreTeam(eq("thuy"), anyInt(), anyInt(), anyInt(), anyInt(), anyString(), anyBoolean()))
                .thenReturn(expectedResult);

        Page<ICoeCoreTeamSearch> result =  coeCoreTeamService.searchCoeCoreTeam("thuy", 1, 1, 1, 5, "name",false);

        assertEquals(expectedResult, result);
        assertFalse(result.isEmpty());
        assertEquals(team, result.getContent());

		verify(coeCoreTeamService).searchCoeCoreTeam("thuy", 1, 1, 1, 5, "name",false);
	}

	@Test
	void testSearchTeamByConditions_withInvalidKeyword_thenReturnEmptyPage() {
		Page<ICoeCoreTeamSearch> expectedResult = new PageImpl<>(Collections.emptyList());

		when(coeCoreTeamService.searchCoeCoreTeam(anyString(), anyInt(), anyInt(), anyInt(), anyInt(), anyString(), anyBoolean()))
				.thenReturn(expectedResult);

		Page<ICoeCoreTeamSearch> result = coeCoreTeamService.searchCoeCoreTeam("thuy", 1, 1, 1, 5, "name", false);

		assertNotNull(result);
		assertTrue(result.isEmpty());
		assertEquals(0, result.getTotalElements());
		assertEquals(0, result.getContent().size());

		verify(coeCoreTeamService).searchCoeCoreTeam("thuy", 1, 1, 1, 5, "name", false);
	}

	@Test
	void testValidateCoeCoreTeam_whenNotAllNullParams_thenReturnSuccess() {
		String teamName = "Valid Team Name";
		Integer teamLeaderId = 15;
		String teamCode = "VTN";
		List<String> mockResult = new ArrayList<>();
        mockResult.add(null);
        mockResult.add(null);
		mockResult.add(null);
		Mockito.when(coeCoreTeamService.validateCoeCoreTeam(teamName, teamLeaderId, teamCode))
				.thenReturn(mockResult);
		BaseResponse<Object> result = coeCoreTeamController.validateCoeCoreTeam(teamName, teamLeaderId, teamCode);
		assertEquals(HttpStatus.OK.value(), result.getStatus());
		assertEquals(null, result.getMessage());
		assertNotNull(result.getData());
	}

	@Test
	void testValidateCoeCoreTeam_whenAllNullParams_thenReturnBadRequest() {
		String teamName = null;
		Integer teamLeaderId = null;
		String teamCode = null;
		BaseResponse<Object> result = coeCoreTeamController.validateCoeCoreTeam(teamName, teamLeaderId, teamCode);
		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
		assertEquals("Nothing to validate!", result.getMessage());
	}
}
