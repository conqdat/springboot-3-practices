package com.hitachi.coe.fullstack.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeUTModel {

    private Double avgBillable;
    private Page<IEmployeeUTModel> iEmployeeUTModels;
}
