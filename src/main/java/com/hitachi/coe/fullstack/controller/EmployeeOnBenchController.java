package com.hitachi.coe.fullstack.controller;

import com.hitachi.coe.fullstack.model.common.BaseResponse;
import com.hitachi.coe.fullstack.service.EmployeeOnBenchService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employee-on-bench")
public class EmployeeOnBenchController {

    @Autowired
    private EmployeeOnBenchService employeeOnBenchService;

    /**
     * Get all employee on bench.
     *
     * @param no     The page number to retrieve.
     * @param limit  The maximum employee of results to return of each page.
     * @param sortBy The field to sort the results by.
     * @param desc   The field to sort the results asc or desc.
     * @return BaseResponse containing the page of employee on bench.
     * @author ThuyTrinhThanhLe
     */
    @GetMapping("/")
    @ApiOperation("This api will return list of Employee On Bench")
    public BaseResponse<Page<?>> getAllEmployeeOnBench(@RequestParam(defaultValue = "0") Integer no,
                                                       @RequestParam(defaultValue = "10") Integer limit,
                                                       @RequestParam(defaultValue = "created") String sortBy,
                                                       @RequestParam(required = false, defaultValue = "true") Boolean desc) {
        return new BaseResponse<>(HttpStatus.OK.value(), null, employeeOnBenchService.getAllEmployeeOnBench(no, limit, sortBy, desc));
    }
}
