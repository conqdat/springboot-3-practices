package com.hitachi.coe.fullstack.service;

import com.hitachi.coe.fullstack.entity.CoeUtilization;
import com.hitachi.coe.fullstack.model.CoeUtilizationModel;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CoeUtilizationService {
	/**
	 * @return list of CoE Utilization
	 * @author PhanNguyen
	 */
	List<CoeUtilizationModel> getAllCoeUtilization();

	/**
	 * Save CoE Utilization from a given duration text.
	 *
	 * @param duration duration of CoE Utilization
	 * @param formatDate format of Date
	 * @param dateImportStr date for Import
	 * @param totalUtilization Total Utilization of CoE Utilization
	 * @return CoeUtilization
	 * @author tquangpham
	 */
	CoeUtilization saveCoEUtilizationFromDuration(String formatDate, String duration, String dateImportStr, Double totalUtilization);

	/**
	 * Retrieves a page of CoE Utilization that from start date to end date.
	 *
	 * @param fromDateStr The start date of the date range to retrieve utilization statistics for, in the format "yyyy-MM-dd".
	 * @param toDateStr The end date of the date range to retrieve utilization statistics for, in the format "yyyy-MM-dd".
	 * @param no            The page number to retrieve.
	 * @param limit         The maximum employee of results to return per page.
	 * @param sortBy        The field to sort the results by.
	 * @param desc          The field to sort desc or asc the results.
	 * @return the page of CoE Utilization.
	 * @author tquangpham
	 */
	Page<CoeUtilizationModel> getListOfCoeUtilizationByMonth(String fromDateStr, String toDateStr, Integer no, Integer limit, String sortBy, Boolean desc);
}
