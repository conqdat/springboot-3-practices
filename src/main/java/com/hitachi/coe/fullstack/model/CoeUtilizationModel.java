package com.hitachi.coe.fullstack.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hitachi.coe.fullstack.model.base.AuditModel;
import com.hitachi.coe.fullstack.util.DateTimeSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CoeUtilizationModel extends AuditModel<Integer>{

	private static final long serialVersionUID = -8384013958808105025L;

    private String duration;

    @JsonSerialize(using = DateTimeSerializer.class)
    private Date startDate;

    @JsonSerialize(using = DateTimeSerializer.class)
    private Date endDate;
    
    private Double totalUtilization;
    
    private List<EmployeeUtilizationModel> employeeUtilizations;
}
