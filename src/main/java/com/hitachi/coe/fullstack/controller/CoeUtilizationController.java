package com.hitachi.coe.fullstack.controller;

import com.hitachi.coe.fullstack.model.CoeUtilizationModel;
import com.hitachi.coe.fullstack.model.common.BaseResponse;
import com.hitachi.coe.fullstack.service.CoeUtilizationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coe-utilization")
public class CoeUtilizationController {
	@Autowired
	private CoeUtilizationService coeUtilizationService;
	
	/**
	 * @return list of CoE Utilization
	 * @author PhanNguyen
	 */
	@GetMapping("/get-all")
	@ApiOperation("This api will return list of coe utilization ")
	public BaseResponse<List<CoeUtilizationModel>> getListCoeUtilization() {
		return BaseResponse.success(coeUtilizationService.getAllCoeUtilization());
	}

	/**
	 * Retrieves a page of CoE Utilization that from start date to end date.
	 *
	 * @param fromDate The start date of the date range to retrieve utilization statistics for, in the format "yyyy-MM-dd".
	 * @param toDate The end date of the date range to retrieve utilization statistics for, in the format "yyyy-MM-dd".
	 * @param no            The page number to retrieve.
	 * @param limit         The maximum employee of results to return per page.
	 * @param sortBy        The field to sort the results by.
	 * @param desc          The field to sort desc or asc the results.
	 * @return the page of CoE Utilization.
	 * @author tquangpham
	 */
	@GetMapping("/get")
	@ApiOperation("This api will return list of coe utilization ")
	public BaseResponse<Page<CoeUtilizationModel>> getListCoeUtilizationByMonth(@RequestParam(value="fromDate", required = false) String fromDate, @RequestParam(value="toDate", required = false) String toDate, @RequestParam(defaultValue = "0") Integer no
			, @RequestParam(defaultValue = "10") Integer limit, @RequestParam(defaultValue = "startDate") String sortBy, @RequestParam(required = false) Boolean desc) {
		return BaseResponse.success(coeUtilizationService.getListOfCoeUtilizationByMonth(fromDate, toDate, no, limit, sortBy,desc));
	}
}
