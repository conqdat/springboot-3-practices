package com.hitachi.coe.fullstack.controller;

import com.hitachi.coe.fullstack.model.CoeUtilizationModel;
import com.hitachi.coe.fullstack.model.common.BaseResponse;
import com.hitachi.coe.fullstack.service.CoeUtilizationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-data-test.properties")
public class CoeUtilizationControllerTest {
	
	@InjectMocks
	private CoeUtilizationController coeUtilizationcontroller;
	
	@MockBean
	private CoeUtilizationService coeUtilizationService;
	
	@Test
	void testGetListCoeUtilization_whenSuccess_thenReturnListOfCoeUtilization() {
		CoeUtilizationModel model1 = new CoeUtilizationModel();
		CoeUtilizationModel model2 = new CoeUtilizationModel();
		CoeUtilizationModel model3 = new CoeUtilizationModel();
		List<CoeUtilizationModel> models = List.of(model1, model2, model3);
		Mockito.when(coeUtilizationService.getAllCoeUtilization()).thenReturn(models);
		BaseResponse<List<CoeUtilizationModel>> status = coeUtilizationcontroller.getListCoeUtilization();
		assertEquals(3, status.getData().size());
	}

	@Test
	public void testGetListCoeUtilizationByMonth_whenValidData_thenSuccess() {

		CoeUtilizationModel coeUtilizationModel1 = new CoeUtilizationModel();
		CoeUtilizationModel coeUtilizationModel2 = new CoeUtilizationModel();
		CoeUtilizationModel coeUtilizationModel3 = new CoeUtilizationModel();

		coeUtilizationModel1.setId(1);
		coeUtilizationModel1.setDuration("01 Aug 2023 - 30 Aug 2023");
		coeUtilizationModel2.setId(1);
		coeUtilizationModel2.setDuration("01 Aug 2023 - 30 Aug 2023");
		coeUtilizationModel3.setId(1);
		coeUtilizationModel3.setDuration("01 Aug 2023 - 30 Aug 2023");

		Integer no = 0;
		Integer limit = 10;
		String sortBy = "name";
		Boolean desc = true;

		List<CoeUtilizationModel> coeUtilizations = Arrays.asList(coeUtilizationModel1, coeUtilizationModel2, coeUtilizationModel3);

		Page<CoeUtilizationModel> mockPage = new PageImpl<>(coeUtilizations);

		when(coeUtilizationService.getListOfCoeUtilizationByMonth(any(String.class), any(String.class), any(Integer.class), any(Integer.class), any(String.class), any(Boolean.class))).thenReturn(mockPage);

		BaseResponse<Page<CoeUtilizationModel>> result = coeUtilizationcontroller.getListCoeUtilizationByMonth("2023-08-01","2023-08-31", no, limit, sortBy, desc);

		assertNotNull(result);
		assertEquals(HttpStatus.OK.value(), result.getStatus());
		assertNull(result.getMessage());
		assertNotNull(result.getData());
		assertEquals(mockPage, result.getData());
	}
}
