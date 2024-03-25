package com.hitachi.coe.fullstack.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeProjectAddModel  {

    @NotNull(message = "Employee ID is required")
    @Min(value = 1, message = "Employee ID must be greater than or equal to 1")
    private Integer employeeId;

    @NotNull(message = "Utilization is required")
    @Min(value = 0, message = "Utilization must be greater than or equal to 0")
    @Max(value = 100, message = "Utilization must be lesser than or equal to 100")
    private Integer utilization;

    @NotBlank(message = "Employee Type is required")
    private String employeeType;

    @NotBlank(message = "Start date is required")
    private String startDate;

    @NotBlank(message = "End date is required")
    private String endDate;

    @NotNull(message = "Employee role is required")
    private Integer employeeRoleId;

}
