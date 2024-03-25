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
public class EmployeeOnBenchModel extends AuditModel<Integer> {

    private Integer id;

    private String name;

    private Date startDate;

    private Date endDate;
}
