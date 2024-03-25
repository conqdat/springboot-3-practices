package com.hitachi.coe.fullstack.service.impl;

import com.hitachi.coe.fullstack.exceptions.InvalidDataException;
import com.hitachi.coe.fullstack.model.IBarChartOnBenchModel;
import com.hitachi.coe.fullstack.repository.EmployeeOnBenchDetailRepository;
import com.hitachi.coe.fullstack.service.EmployeeOnBenchDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.lang.Integer.parseInt;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmployeeOnBenchDetailServiceImpl implements EmployeeOnBenchDetailService {
    private final EmployeeOnBenchDetailRepository employeeOnBenchDetailRepository;
    /**
     * Count employee on bench by business unit.
     *
     * @param yearMonth contains a string with 6 numbers.
     * @return A list of objects containing the business unit name and the total count of employee on bench.
     * @author ThuyTrinhThanhLe
     */
    @Override
    public List<IBarChartOnBenchModel> getQuantityOfEmployeeOnBenchByBusinessUnit(Integer yearMonth) {
        LocalDate dt;

        if (ObjectUtils.isEmpty(yearMonth)) {
            dt = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
        } else if (String.valueOf(yearMonth).length() != 6) {
            throw new InvalidDataException("404", "Wrong input format");
        } else {
            dt = LocalDate.of(parseInt(yearMonth.toString().substring(0, 4)), parseInt(yearMonth.toString().substring(yearMonth.toString().length() - 2)), 1);
        }

        return employeeOnBenchDetailRepository.getQuantityOfEmployeeOnBenchByBusinessUnit(dt);
    }

    /**
     * Count employee on bench by location.
     *
     * @param yearMonth contains a string with 6 numbers.
     * @return A list of objects containing the branch name and the total count of employee on bench.
     * @author ThuyTrinhThanhLe
     */

    @Override
    public List<IBarChartOnBenchModel> getQuantityOfEmployeesOnBenchByLocation(Integer yearMonth) {
        LocalDate dt;

        if (ObjectUtils.isEmpty(yearMonth)) {
            dt = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
        } else if (String.valueOf(yearMonth).length() != 6) {
            throw new InvalidDataException("404", "Wrong input format");
        } else {
            dt = LocalDate.of(parseInt(yearMonth.toString().substring(0, 4)), parseInt(yearMonth.toString().substring(yearMonth.toString().length() - 2)), 1);
        }

        return employeeOnBenchDetailRepository.getQuantityOfEmployeesOnBenchByLocation(dt);
    }

    /**
     * Count employee on bench by level.
     *
     * @param yearMonth contains a string with 6 numbers.
     * @return A list of objects containing the level name and the total count of employee on bench.
     * @author ThuyTrinhThanhLe
     */
    @Override
    public List<IBarChartOnBenchModel> getQuantityOfEmployeesOnBenchByLevel(Integer yearMonth) {
        LocalDate dt;

        if (ObjectUtils.isEmpty(yearMonth)) {
            dt = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
        } else if (String.valueOf(yearMonth).length() != 6) {
            throw new InvalidDataException("404", "Wrong input format");
        } else {
            dt = LocalDate.of(parseInt(yearMonth.toString().substring(0, 4)), parseInt(yearMonth.toString().substring(yearMonth.toString().length() - 2)), 1);
        }

        return employeeOnBenchDetailRepository.getQuantityOfEmployeesOnBenchByLevel(dt);
    }

    /**
     * Count employee on bench by center of excellence.
     *
     * @param month contains a string with 6 numbers.
     * @return A list of objects containing the center of excellence name or practice name and the total count of employee on bench.
     * @author ThuyTrinhThanhLe
     */
    @Override
    public List<IBarChartOnBenchModel> getQuantityOfEmployeesOnBenchByCoe(Integer month) {
        LocalDate dt;

        if (ObjectUtils.isEmpty(month)) {
            dt = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
        } else if (String.valueOf(month).length() != 6) {
            throw new InvalidDataException("404", "Wrong input format");
        } else {
            dt = LocalDate.of(parseInt(month.toString().substring(0, 4)), parseInt(month.toString().substring(month.toString().length() - 2)), 1);
        }

        return employeeOnBenchDetailRepository.getQuantityOfEmployeesOnBenchByCoe(dt);
    }
}
