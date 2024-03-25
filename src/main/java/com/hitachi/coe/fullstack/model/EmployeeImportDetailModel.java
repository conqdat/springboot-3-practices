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
public class EmployeeImportDetailModel extends AuditModel<Long>{
    private Long id;
    private Object body;
    private ImportOperationStatus status;
    private Integer lineNum;
    private Object messsageLineList;
    private Integer employeeImportId;
    private EmployeeImportModel employeeImportModel;
}
