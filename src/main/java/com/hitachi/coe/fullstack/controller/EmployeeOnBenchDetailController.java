package com.hitachi.coe.fullstack.controller;

import com.hitachi.coe.fullstack.model.IBarChartOnBenchModel;
import com.hitachi.coe.fullstack.service.EmployeeOnBenchDetailService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee-on-bench-detail")
public class EmployeeOnBenchDetailController {

    @Autowired
    private EmployeeOnBenchDetailService employeeOnBenchDetailService;

    /**
     * Count employee on bench by business unit.
     *
     * @param yearMonth contains a string with 6 numbers.
     * @return a ResponseEntity containing the quantity of employee on bench according to month
     * @author ThuyTrinhThanhLe
     */
    @GetMapping("/get-by-bu")
    @ApiOperation("This api will calculate the quantity of employee on bench by business unit")
    public ResponseEntity<List<IBarChartOnBenchModel>> getQuantityOfEmployeeOnBenchByBusinessUnit(@RequestParam(value = "month", required = false) Integer yearMonth) {
        return ResponseEntity.ok(employeeOnBenchDetailService.getQuantityOfEmployeeOnBenchByBusinessUnit(yearMonth));
    }

    /**
     * Count employee on bench by location.
     *
     * @param yearMonth contains a string with 6 numbers.
     * @return a ResponseEntity containing the quantity of employee on bench according to month
     * @author ThuyTrinhThanhLe
     */
    @GetMapping("/get-by-location")
    @ApiOperation("This api will calculate the quantity of employee on bench by location")
    public ResponseEntity<List<IBarChartOnBenchModel>> getQuantityOfEmployeesOnBenchByLocation(@RequestParam(value = "month", required = false) Integer yearMonth) {
        return ResponseEntity.ok(employeeOnBenchDetailService.getQuantityOfEmployeesOnBenchByLocation(yearMonth));
    }

    /**
     * Count employee on bench by level.
     *
     * @param yearMonth contains a string with 6 numbers.
     * @return a ResponseEntity containing the quantity of employee on bench according to month
     * @author ThuyTrinhThanhLe
     */
    @GetMapping("/get-by-level")
    @ApiOperation("This api will calculate the quantity of employee on bench by level")
    public ResponseEntity<List<IBarChartOnBenchModel>> getQuantityOfEmployeesOnBenchByLevel(@RequestParam(value = "month", required = false) Integer yearMonth) {
        return ResponseEntity.ok(employeeOnBenchDetailService.getQuantityOfEmployeesOnBenchByLevel(yearMonth));
    }

    /**
     * Count employee on bench by coe.
     *
     * @param yearMonth contains a string with 6 numbers.
     * @return a ResponseEntity containing the quantity of employee on bench according to month
     * @author ThuyTrinhThanhLe
     */
    @GetMapping("/get-by-coe")
    @ApiOperation("This api will calculate the quantity of employee on bench by coe")
    public ResponseEntity<List<IBarChartOnBenchModel>> getQuantityOfEmployeesOnBenchByCoe(
            @RequestParam(value = "month", required = false) Integer yearMonth) {
        return ResponseEntity.ok(employeeOnBenchDetailService.getQuantityOfEmployeesOnBenchByCoe(yearMonth));
    }
}
