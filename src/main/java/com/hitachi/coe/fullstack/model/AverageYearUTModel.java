package com.hitachi.coe.fullstack.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AverageYearUTModel {
    Integer year;
    List<IPieChartModel> pieChartModelList;
}
