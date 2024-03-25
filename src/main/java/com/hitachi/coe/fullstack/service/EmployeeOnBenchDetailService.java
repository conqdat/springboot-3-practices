package com.hitachi.coe.fullstack.service;

import com.hitachi.coe.fullstack.model.IBarChartOnBenchModel;

import java.util.List;

public interface EmployeeOnBenchDetailService {
    /**
     * Count employee on bench by business unit.
     *
     * @param yearMonth the month to validate with start date in employee on bench.
     * @return A list of objects containing the business unit name and the total count of employee on bench.
     * @author ThuyTrinhThanhLe
     */
    List<IBarChartOnBenchModel> getQuantityOfEmployeeOnBenchByBusinessUnit(Integer yearMonth);

    /**
     * Count employee on bench by location.
     *
     * @param yearMonth contains a string with 6 numbers.
     * @return A list of objects containing the branch name and the total count of employee on bench.
     * @author ThuyTrinhThanhLe
     */
    List<IBarChartOnBenchModel> getQuantityOfEmployeesOnBenchByLocation(Integer yearMonth);

    /**
     * Count employee on bench by level.
     *
     * @param yearMonth contains a string with 6 numbers.
     * @return A list of objects containing the level name and the total count of employee on bench.
     * @author ThuyTrinhThanhLe
     */
    List<IBarChartOnBenchModel> getQuantityOfEmployeesOnBenchByLevel(Integer yearMonth);

    /**
     * Count employee on bench by center of excellence.
     *
     * @param yearMonth contains a string with 6 numbers.
     * @return A list of objects containing the center of excellence name and the total count of employee on bench.
     * @author ThuyTrinhThanhLe
     */
    List<IBarChartOnBenchModel> getQuantityOfEmployeesOnBenchByCoe(Integer yearMonth);
}
