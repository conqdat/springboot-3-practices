package com.hitachi.coe.fullstack.service;

import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.entity.CoeUtilization;
import com.hitachi.coe.fullstack.exceptions.CoEException;
import com.hitachi.coe.fullstack.model.CoeUtilizationModel;
import com.hitachi.coe.fullstack.repository.CoeUtilizationRepository;
import com.hitachi.coe.fullstack.service.impl.CoeUtilizationServiceImpl;
import com.hitachi.coe.fullstack.transformation.CoeUtilizationTransformer;
import com.hitachi.coe.fullstack.util.DateFormatUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class CoeUtilizationServiceTest {
	@MockBean
	CoeUtilizationRepository coeUtilizationRepository;
	
	@MockBean
	CoeUtilizationTransformer coeUtilizationTransformer;

	@Autowired
	CoeUtilizationServiceImpl coeUtilizationService;

	CoeUtilization coeUtilization;

	CoeUtilization coeUtilization1;

	CoeUtilization coeUtilization2;

	CoeUtilization coeUtilization3;

	CoeUtilizationModel coeUtilizationModel1;

	CoeUtilizationModel coeUtilizationModel2;

	CoeUtilizationModel coeUtilizationModel3;

	@BeforeEach
	void setUp(){
		coeUtilization = new CoeUtilization();
		coeUtilization1 = new CoeUtilization();
		coeUtilization2 = new CoeUtilization();
		coeUtilization3 = new CoeUtilization();
		coeUtilizationModel1 = new CoeUtilizationModel();
		coeUtilizationModel2 = new CoeUtilizationModel();
		coeUtilizationModel3 = new CoeUtilizationModel();

		coeUtilization.setId(1);
		coeUtilization.setDuration("01 Aug 2023 - 30 Aug 2023");
		coeUtilization.setStartDate(DateFormatUtils.convertDateFromString("01 Aug 2023", "dd MMM uuuu"));
		coeUtilization.setEndDate(DateFormatUtils.convertDateFromString("30 Aug 2023", "dd MMM uuuu"));
		coeUtilization.setTotalUtilization(60.8);

		coeUtilization1.setId(1);
		coeUtilization1.setDuration("01 Aug 2023 - 30 Aug 2023");
		coeUtilization2.setId(1);
		coeUtilization2.setDuration("01 Aug 2023 - 30 Aug 2023");
		coeUtilization3.setId(1);
		coeUtilization3.setDuration("01 Aug 2023 - 30 Aug 2023");

		coeUtilizationModel1.setId(1);
		coeUtilizationModel1.setDuration("01 Aug 2023 - 30 Aug 2023");
		coeUtilizationModel2.setId(1);
		coeUtilizationModel2.setDuration("01 Aug 2023 - 30 Aug 2023");
		coeUtilizationModel3.setId(1);
		coeUtilizationModel3.setDuration("01 Aug 2023 - 30 Aug 2023");

	}
	@Test
	void testGetAllCoeUtilization_whenSuccess_thenReturnListCoeUtilization() {

		List<CoeUtilization> coeUtilizations = Arrays.asList(coeUtilization1, coeUtilization2, coeUtilization3);
		Mockito.when(coeUtilizationRepository.findAll()).thenReturn(coeUtilizations);
		Mockito.when(coeUtilizationTransformer.applyList(coeUtilizations)).thenReturn(Arrays.asList(coeUtilizationModel1, coeUtilizationModel2, coeUtilizationModel3));
		List<CoeUtilizationModel> result = coeUtilizationService.getAllCoeUtilization();
		Assertions.assertNotNull(result);
		Assertions.assertEquals(coeUtilizations.size(), result.size());
	}

	@Test
	public void testSaveCoEUtilizationFromDuration_whenValidData_thenSuccess(){
		String duration = "Duration: 01 Aug 2023 - 30 Aug 2023";
		String dateImport = "01 Aug 2023";
		String format = "dd MMM uuuu";
		Double totalUT = 60.8;

		CoeUtilization coeUtilizationActual = coeUtilizationService.saveCoEUtilizationFromDuration(format, duration, dateImport, totalUT);

		assertNotNull(coeUtilizationActual);
		assertEquals(coeUtilization.getStartDate(), coeUtilizationActual.getStartDate());
		assertEquals(coeUtilization.getEndDate(), coeUtilizationActual.getEndDate());
		assertEquals(coeUtilization.getDuration(), coeUtilizationActual.getDuration());
		assertEquals(coeUtilization.getTotalUtilization(), coeUtilizationActual.getTotalUtilization());
	}

	@Test
	public void testSaveCoEUtilizationFromDuration_whenInvalidDurationFormat_thenThrowCoEException(){
		String duration = "Duration:15 Aug 2023 - 30 Aug 2023";
		String dateImport = "01 Aug 2023";
		String format = "dd MMM uuuu";
		Double totalUT = 60.8;


		Throwable throwable = assertThrows(CoEException.class,
				() -> coeUtilizationService.saveCoEUtilizationFromDuration(format, duration, dateImport, totalUT));

		//Verify
		assertEquals(CoEException.class, throwable.getClass());
		assertEquals(ErrorConstant.MESSAGE_INVALID_FORMAT_DURATION, throwable.getMessage());
	}

	@Test
	public void testSaveCoEUtilizationFromDuration_whenDurationIsMissing_thenThrowCoEException(){
		String duration = "Duration:  - 30 Aug 2023";
		String dateImport = "01 Aug 2023";
		String format = "dd MMM uuuu";
		Double totalUT = 60.8;

		Throwable throwable = assertThrows(CoEException.class,
				() -> coeUtilizationService.saveCoEUtilizationFromDuration(format, duration, dateImport, totalUT));

		//Verify
		assertEquals(CoEException.class, throwable.getClass());
		assertEquals(ErrorConstant.MESSAGE_DURATION_IS_REQUIRED, throwable.getMessage());
	}

	@Test
	public void testSaveCoEUtilizationFromDuration_whenStartDateEqualEndDate_thenThrowCoEException(){
		String duration = "Duration: 01 Aug 2023 - 01 Aug 2023";
		String dateImport = "01 Aug 2023";
		String format = "dd MMM uuuu";
		Double totalUT = 60.8;

		Throwable throwable = assertThrows(CoEException.class,
				() -> coeUtilizationService.saveCoEUtilizationFromDuration(format, duration, dateImport, totalUT));

		//Verify
		assertEquals(CoEException.class, throwable.getClass());
		assertEquals(ErrorConstant.MESSAGE_INVALID_START_DATE_END_DATE, throwable.getMessage());
	}

	@Test
	public void testSaveCoEUtilizationFromDuration_whenStartDateNotMatchImportDate_thenThrowCoEException(){
		String duration = "Duration: 15 Aug 2023 - 30 Aug 2023";
		String dateImport = "01 Jan 2023";
		String format = "dd MMM uuuu";
		Double totalUT = 60.8;

		Throwable throwable = assertThrows(CoEException.class,
				() -> coeUtilizationService.saveCoEUtilizationFromDuration(format, duration, dateImport, totalUT));

		//Verify
		assertEquals(CoEException.class, throwable.getClass());
		assertEquals(ErrorConstant.MESSAGE_INVALID_DATE_NOT_MATCH, throwable.getMessage());
	}

	@Test
	public void testSaveCoEUtilizationFromDuration_whenStartDateNotStartOfTheMonth_thenThrowCoEException(){
		String duration = "Duration: 15 Jan 2023 - 30 Jan 2023";
		String dateImport = "01 Jan 2023";
		String format = "dd MMM uuuu";
		Double totalUT = 60.8;

		Throwable throwable = assertThrows(CoEException.class,
				() -> coeUtilizationService.saveCoEUtilizationFromDuration(format, duration, dateImport, totalUT));

		//Verify
		assertEquals(CoEException.class, throwable.getClass());
		assertEquals(ErrorConstant.MESSAGE_INVALID_DATE_IMPORT, throwable.getMessage());
	}

	@Test
	public void testGetListOfCoeUtilizationByMonth_whenValidData_thenSuccess(){

		int no = 0;
		int limit = 10;
		String sortBy = "name";
		Boolean desc = true;

		List<CoeUtilization> coeUtilizations = Arrays.asList(coeUtilization1, coeUtilization2, coeUtilization3);

		when(coeUtilizationRepository.getListOfCoeUtilization(any(Timestamp.class), any(Timestamp.class))).thenReturn(coeUtilizations);
		when(coeUtilizationTransformer.applyList(coeUtilizations)).thenReturn(Arrays.asList(coeUtilizationModel1, coeUtilizationModel2,coeUtilizationModel3));

		Page<CoeUtilizationModel> coeUtilizationModels = coeUtilizationService.getListOfCoeUtilizationByMonth("2023-08-01","2023-08-30", no, limit, sortBy, desc);

		//Verify
		assertNotNull(coeUtilizationModels);
		assertEquals(coeUtilizations.size(), coeUtilizationModels.getTotalElements());
	}

	@Test
	public void testGetListOfCoeUtilizationByMonth_whenEmptyContent_thenSuccess(){

		int no = 0;
		int limit = 10;
		String sortBy = "name";
		Boolean desc = true;

		when(coeUtilizationRepository.getListOfCoeUtilization(any(Timestamp.class), any(Timestamp.class))).thenReturn(Collections.emptyList());

		Page<CoeUtilizationModel> coeUtilizationModels = coeUtilizationService.getListOfCoeUtilizationByMonth("2023-08-01","2023-08-30", no, limit, sortBy, desc);

		//Verify
		assertNotNull(coeUtilizationModels);
		assertEquals(0, coeUtilizationModels.getTotalElements());
	}

	@Test
	void testGetListOfCoeUtilizationByMonth_whenInvalidDate_thenThrowException() {

		int no = 0;
		int limit = 10;
		String sortBy = "name";
		Boolean desc = true;

		Throwable throwable = assertThrows(CoEException.class,
				() -> coeUtilizationService.getListOfCoeUtilizationByMonth("2023-09-01","2023-08-30", no, limit, sortBy, desc));
		assertEquals(CoEException.class, throwable.getClass());
		assertEquals(ErrorConstant.MESSAGE_INVALID_START_DATE_END_DATE, throwable.getMessage());
	}
}
