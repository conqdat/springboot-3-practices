package com.hitachi.coe.fullstack.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CountEmployeeModel {
    private Long totalEmployees;

    private Float diffTotal;

    private Long onProjectEmployees;

    private Float diffOnProject;

    private Long onBenchEmployees;

    private Float diffOnBench;

    private Long newEmployees;

    private Float diffNew;
}
