package com.hitachi.coe.fullstack.service;

import org.springframework.data.domain.Page;

import com.hitachi.coe.fullstack.model.EmployeeImportModel;
import com.hitachi.coe.fullstack.model.ImportOperationStatus;
import com.hitachi.coe.fullstack.model.ImportOperationType;

public interface EmployeeImportService {
    Page<EmployeeImportModel> search(String keyword,
            Integer type,
            ImportOperationType typeEnum,
            Integer status,
            ImportOperationStatus statusEnum,
            Integer no,
            Integer limit,
            String sortBy,
            Boolean desc);
}
