package com.hitachi.coe.fullstack.service.impl;

import com.hitachi.coe.fullstack.constant.CommonConstant;
import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.entity.CoeUtilization;
import com.hitachi.coe.fullstack.exceptions.CoEException;
import com.hitachi.coe.fullstack.model.CoeUtilizationModel;
import com.hitachi.coe.fullstack.repository.CoeUtilizationRepository;
import com.hitachi.coe.fullstack.service.CoeUtilizationService;
import com.hitachi.coe.fullstack.transformation.CoeUtilizationTransformer;
import com.hitachi.coe.fullstack.util.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class CoeUtilizationServiceImpl implements CoeUtilizationService{

	@Autowired
	CoeUtilizationRepository coeUtilizationRepository;
	
	@Autowired
	CoeUtilizationTransformer coeUtilizationTransformer;
	
	/**
	 * @return list of CoE Utilization
	 * @author PhanNguyen
	 */
	
	@Override
	public List<CoeUtilizationModel> getAllCoeUtilization() {
		return coeUtilizationTransformer.applyList(coeUtilizationRepository.findAll());
	}

	@Override
	public CoeUtilization saveCoEUtilizationFromDuration(String formatDate, String duration, String dateImportStr, Double totalUtilization) {
		//Check format string "Duration: dd MM yyyy - dd MM yyyy"
		if (!duration.contains("Duration: ") || !duration.contains(" - ")) {
			throw new CoEException(ErrorConstant.CODE_INVALID_FORMAT_DURATION, ErrorConstant.MESSAGE_INVALID_FORMAT_DURATION);
		}
		final String validateDuration = duration.substring(duration.indexOf(": ") + 2);
		final String[] dateSplit = validateDuration.split(" - ");
		final Date startDate = DateFormatUtils.convertDateFromString(dateSplit[0], formatDate);
		final Date endDate = DateFormatUtils.convertDateFromString(dateSplit[1], formatDate);
		final Date dateImport = DateFormatUtils.convertDateFromString(dateImportStr, formatDate);

		if (startDate == null || endDate == null || dateImport == null) {
			throw new CoEException(ErrorConstant.CODE_DURATION_IS_REQUIRED, ErrorConstant.MESSAGE_DURATION_IS_REQUIRED);
		}

		final LocalDate startLDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		final LocalDate endLDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		final LocalDate importLDate = dateImport.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		if (!startLDate.getMonth().equals(importLDate.getMonth()) || !endLDate.getMonth().equals(importLDate.getMonth()) || startLDate.getYear() != importLDate.getYear() || endLDate.getYear() != importLDate.getYear()) {
			throw new CoEException(ErrorConstant.CODE_INVALID_DATE, ErrorConstant.MESSAGE_INVALID_DATE_NOT_MATCH);
		}
		if (importLDate.getDayOfMonth() > 1 || !startLDate.isEqual(importLDate)) {
			throw new CoEException(ErrorConstant.CODE_INVALID_DATE, ErrorConstant.MESSAGE_INVALID_DATE_IMPORT);
		}

		if (startLDate.isEqual(endLDate)) {
			throw new CoEException(ErrorConstant.CODE_INVALID_START_DATE_END_DATE, ErrorConstant.MESSAGE_INVALID_START_DATE_END_DATE);
		}

		CoeUtilization coeUtilization = new CoeUtilization();
		coeUtilization.setDuration(validateDuration);
		coeUtilization.setStartDate(startDate);
		coeUtilization.setEndDate(endDate);
		coeUtilization.setTotalUtilization(totalUtilization);
		coeUtilization.setCreated(new Date());
		coeUtilization.setCreatedBy(CommonConstant.CREATED_BY_ADMINISTRATOR);
		return coeUtilization;
	}

	@Override
	public Page<CoeUtilizationModel> getListOfCoeUtilizationByMonth(String fromDateStr, String toDateStr, Integer no, Integer limit, String sortBy, Boolean desc){

		final Timestamp fromDate = DateFormatUtils.convertTimestampFromString(fromDateStr);
		final Timestamp toDate = DateFormatUtils.convertTimestampFromString(toDateStr);
		Sort sort = Sort.by(sortBy);

		if (desc != null) {
			sort = sort.descending();
		}

		if (fromDate != null && toDate != null && fromDate.after(toDate)) {
			throw new CoEException(ErrorConstant.CODE_INVALID_START_DATE_END_DATE, ErrorConstant.MESSAGE_INVALID_START_DATE_END_DATE);
		}

		List<CoeUtilization> coeUtilizationList = coeUtilizationRepository.getListOfCoeUtilization(fromDate, toDate);

		if (coeUtilizationList.isEmpty()) {
			return new PageImpl<>(Collections.emptyList());
		}

		List<CoeUtilizationModel> coeUtilizationModelList = coeUtilizationTransformer.applyList(coeUtilizationList);
		final Pageable pageable = PageRequest.of(no, limit, sort);
		int start = (int) pageable.getOffset();
		int end = Math.min((start + PageRequest.of(no, limit, sort).getPageSize()), coeUtilizationModelList.size());

		return new PageImpl<>(coeUtilizationModelList.subList(start, end), pageable, coeUtilizationModelList.size());
	}
}
