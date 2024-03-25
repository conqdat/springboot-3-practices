package com.hitachi.coe.fullstack.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Setter
@Getter
@NoArgsConstructor
public class EmployeeUtilizationFree {

    private Double billable;
    private Page<IEmployeeUtilizationFree> iEmployeeUtilizationFreePage;
}
