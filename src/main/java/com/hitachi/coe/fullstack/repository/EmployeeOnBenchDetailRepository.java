package com.hitachi.coe.fullstack.repository;

import com.hitachi.coe.fullstack.entity.EmployeeOnBenchDetail;
import com.hitachi.coe.fullstack.model.IBarChartOnBenchModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public interface EmployeeOnBenchDetailRepository extends JpaRepository<EmployeeOnBenchDetail, Long> {
    /**
     * Count employee on bench by business unit.
     *
     * @param date date to validate with start date in employee on bench.
     * @return A list of objects containing the business unit name and the total count of employee on bench.
     * @author ThuyTrinhThanhLe
     */
    @Query(value = "SELECT results.businessUnitName AS label, results.total AS quantity " +
            "FROM getQuantityOfEmployeesOnBenchByBusinessUnit(:date) AS results", nativeQuery = true)
    List<IBarChartOnBenchModel> getQuantityOfEmployeeOnBenchByBusinessUnit(@Param("date") LocalDate date);

    /**
     * Count employee on bench by location.
     *
     * @param date date to validate with start date in employee on bench.
     * @return A list of objects containing the branch name and the total count of employee on bench.
     * @author ThuyTrinhThanhLe
     */
    @Query(value = "SELECT results.branchName AS label, results.total AS quantity " +
            "FROM getQuantityOfEmployeesOnBenchByLocation(:date) AS results", nativeQuery = true)
    List<IBarChartOnBenchModel> getQuantityOfEmployeesOnBenchByLocation(@Param("date") LocalDate date);

    /**
     * Count employee on bench by level.
     *
     * @param date date to validate with start date in employee on bench.
     * @return A list of objects containing the level name and the total count of employee on bench.
     * @author ThuyTrinhThanhLe
     */
    @Query(value = "SELECT results.levelName AS label, results.total AS quantity " +
            "FROM getQuantityOfEmployeesOnBenchByLevel(:date) AS results", nativeQuery = true)
    List<IBarChartOnBenchModel> getQuantityOfEmployeesOnBenchByLevel(@Param("date") LocalDate date);

    /**
     * Count employee on bench by center of excellence.
     *
     * @param date date to validate with start date in employee on bench.
     * @return A list of objects containing the center of excellence name and the total count of employee on bench.
     * @author ThuyTrinhThanhLe
     */
    @Query(value = "SELECT results.coeName AS label, results.total AS quantity " +
            "FROM getQuantityOfEmployeesOnBenchByCoe(:date) AS results", nativeQuery = true)
    List<IBarChartOnBenchModel> getQuantityOfEmployeesOnBenchByCoe(@Param("date") LocalDate date);
}
