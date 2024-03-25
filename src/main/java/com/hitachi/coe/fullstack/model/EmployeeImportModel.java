package com.hitachi.coe.fullstack.model;

import com.hitachi.coe.fullstack.model.base.AuditModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeImportModel extends AuditModel<Integer>{
    private Integer id;
    private String name;
    private ImportOperationType type;
    private ImportOperationStatus status;
    private Object message;
}
