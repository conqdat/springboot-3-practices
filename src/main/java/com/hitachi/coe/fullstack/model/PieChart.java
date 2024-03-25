package com.hitachi.coe.fullstack.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PieChart implements IPieChartModel{
    private String label;
    private Double data;
}
