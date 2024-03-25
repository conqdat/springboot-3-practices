package com.hitachi.coe.fullstack.model;

import com.hitachi.coe.fullstack.model.base.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeOnBenchDetailModel extends AuditModel<Long> {
    private Long id;

    private Integer benchDays;

    private Date dateOfJoin;

    private Date statusChangeDate;

    private Integer categoryCode;

    private Integer employeeId;

    private Integer employeeOnBenchId;

    private EmployeeOnBenchModel employeeOnBenchModel;
}
