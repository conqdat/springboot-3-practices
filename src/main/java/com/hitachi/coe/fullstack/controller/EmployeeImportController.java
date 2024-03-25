package com.hitachi.coe.fullstack.controller;

import com.hitachi.coe.fullstack.service.EmployeeImportService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitachi.coe.fullstack.model.ImportOperationStatus;
import com.hitachi.coe.fullstack.model.ImportOperationType;
import com.hitachi.coe.fullstack.model.common.BaseResponse;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employee-import")
public class EmployeeImportController {

    private final EmployeeImportService employeeImportService;

    @GetMapping("/")
    @ApiOperation("This api will return list of EmployeeImportModel")
    public BaseResponse<Page<?>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) ImportOperationType typeEnum,
            @RequestParam(required = false) ImportOperationStatus statusEnum,
            @RequestParam(defaultValue = "0") Integer no,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "created") String sortBy,
            @RequestParam(required = false, defaultValue = "true") Boolean desc) {
        return new BaseResponse<>(HttpStatus.OK.value(), null,
                employeeImportService.search(keyword, type, typeEnum, status, statusEnum, no, limit, sortBy, desc));
    }
}
