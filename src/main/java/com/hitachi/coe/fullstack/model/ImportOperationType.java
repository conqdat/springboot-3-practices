package com.hitachi.coe.fullstack.model;

import java.util.Objects;
import java.util.Optional;

import com.hitachi.coe.fullstack.controller.EmployeeImportController;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The enum ImportOperationType is used to define type of import operation.
 * 
 * @author tminhto
 * @see EmployeeImportController#search(String, Integer, Integer,
 *      ImportOperationType, ImportOperationStatus, Integer, Integer, String,
 *      Boolean)
 */
@RequiredArgsConstructor
@Slf4j
@Getter
public enum ImportOperationType {
    EMPLOYEE_ADD(0),
    EMPOYEE_UPDATE(1),
    EMPLOYEE_DELETE(2),
    EMPLOYEE_ON_BENCH(3),
    EMPLOYEE_LEAVE(4),
    SURVEY_DATA(5),
    UTILIZATION(6),
    PROJECT_INSERT(7),
    OTHER(-1);

    private final int value;

    /**
     * Returns an Optional containing the ImportOperationType enum constant
     * associated with the specified value.
     * If the value is null, an empty Optional is returned.
     *
     * @param value the integer value to lookup
     * @return an Optional containing the ImportOperationType enum constant if
     *         found, otherwise an empty Optional
     * @see ImportOperationType
     * @see Optional
     */
    public static Optional<ImportOperationType> valueOf(Integer value) {
        if (Objects.isNull(value)) {
            log.debug("value is null");
            return Optional.empty();
        }
        for (ImportOperationType type : ImportOperationType.values()) {
            if (type.getValue() == value) {
                return Optional.of(type);
            }
        }
        log.debug("ImportOperationType not found for value: {}", value);
        return Optional.empty();
    }
}
