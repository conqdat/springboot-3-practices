package com.hitachi.coe.fullstack.model;

import java.util.Objects;
import java.util.Optional;

import com.hitachi.coe.fullstack.controller.EmployeeImportController;
import com.hitachi.coe.fullstack.service.EmployeeImportService;
import com.hitachi.coe.fullstack.service.EmployeeService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The enum ImportOperationStatus is used to define status of import operation.
 *
 * @author tminhto
 * @see EmployeeImportController#search(String, Integer, Integer,
 *      ImportOperationType, ImportOperationStatus, Integer, Integer, String,
 *      Boolean)
 * @see EmployeeImportService#search(String, Integer, ImportOperationType,
 *      Integer, ImportOperationStatus, Integer, Integer, String, Boolean)
 * @see EmployeeService#importOnBenchEmployee(ExcelResponseModel,
 *      org.springframework.web.multipart.MultipartFile)
 */
@Slf4j
@RequiredArgsConstructor
@Getter
public enum ImportOperationStatus {
    SUCCESS(0),
    FAILED(1);

    private final int value;

    /**
     * Returns an {@link Optional} containing the ImportOperationStatus enum
     * constant associated with the specified value.
     * If the value is null, an empty {@link Optional} is returned.
     *
     * @param value the integer value to lookup
     * @return an {@link Optional} containing the ImportOperationStatus enum
     *         constant if
     *         found, otherwise an empty {@link Optional}
     */
    public static Optional<ImportOperationStatus> valueOf(Integer value) {
        if (Objects.isNull(value)) {
            log.debug("value is null");
            return Optional.empty();
        }
        for (ImportOperationStatus type : ImportOperationStatus.values()) {
            if (type.getValue() == value) {
                return Optional.of(type);
            }
        }
        log.debug("ImportOperationStatus not found for value: {}", value);
        return Optional.empty();
    }
}
