package com.hitachi.coe.fullstack.model;

import com.hitachi.coe.fullstack.enums.EmployeeType;
import com.hitachi.coe.fullstack.model.base.AuditModel;
import com.hitachi.coe.fullstack.model.base.BaseReadonlyModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeProjectModel extends AuditModel<Integer> implements BaseReadonlyModel<Integer> {

    private Integer id;

    @NotNull(message = "Project cannot be null")
    private ProjectModel project;

    @NotNull(message = "Employee cannot be null")
    private EmployeeModel employee;

    @NotNull(message = "Utilization cannot be null")
    private Integer utilization;

    @NotNull(message = "Employee Type cannot be null")
    private EmployeeType employeeType;

    private EmployeeRoleModel employeeRole;

    @NotNull(message = "Start Date cannot be null")
    private Date startDate;

    private Date endDate;

    private Date releaseDate;

}
