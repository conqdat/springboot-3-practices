package com.hitachi.coe.fullstack.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hitachi.coe.fullstack.model.EmployeeImportModel;
import com.hitachi.coe.fullstack.model.IEmployeeImportSearch;
import com.hitachi.coe.fullstack.model.ImportOperationStatus;
import com.hitachi.coe.fullstack.model.ImportOperationType;
import com.hitachi.coe.fullstack.repository.EmployeeImportRepository;
import com.hitachi.coe.fullstack.service.EmployeeImportService;
import com.hitachi.coe.fullstack.util.StringUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmployeeImportServiceImpl implements EmployeeImportService {

    private final EmployeeImportRepository employeeImportRepository;

    @Override
    public Page<EmployeeImportModel> search(
            String keyword,
            Integer type,
            ImportOperationType typeEnum,
            Integer status,
            ImportOperationStatus statusEnum,
            Integer no,
            Integer limit,
            String sortBy,
            Boolean desc) {
        // init
        final Sort sort = Sort.by(
                Boolean.TRUE.equals(desc)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                sortBy);
        final Pageable pageable = PageRequest.of(no, limit, sort);
        final String searchKeyword = Optional.ofNullable(keyword)
                .map(e -> StringUtil.combineString(StringUtil.removeSymbols(e, Arrays.asList("%")), "%"))
                .orElse("%");
        final Integer searchType = Optional.ofNullable(type).orElseGet(() -> Optional.ofNullable(typeEnum)
                .map(ImportOperationType::getValue)
                .orElse(null));
        final Integer searchStatus = Optional.ofNullable(status).orElseGet(() -> Optional.ofNullable(statusEnum)
                .map(ImportOperationStatus::getValue)
                .orElse(null));
        final Optional<Page<IEmployeeImportSearch>> result = Optional
                .ofNullable(employeeImportRepository.search(searchKeyword, searchType, searchStatus, pageable));
        final List<EmployeeImportModel> employeeImportModels = result
                .map(Page::getContent)
                .orElse(Collections.emptyList())
                .stream()
                .map(e -> {
                    EmployeeImportModel importModel = new EmployeeImportModel();
                    importModel.setId(e.getId());
                    importModel.setName(e.getName());
                    importModel.setType(ImportOperationType.valueOf(e.getType()).orElse(null));
                    importModel.setStatus(ImportOperationStatus.valueOf(e.getStatus()).orElse(null));
                    importModel.setMessage(e.getMessage());
                    importModel.setCreated(e.getCreated());
                    importModel.setCreatedBy(e.getCreatedBy());
                    importModel.setUpdated(e.getUpdated());
                    importModel.setUpdatedBy(e.getUpdatedBy());
                    return importModel;
                }).collect(Collectors.toList());
        return new PageImpl<>(employeeImportModels, pageable, result.map(Page::getTotalElements).orElse(0L));
    }

}
