package com.hitachi.coe.fullstack.model;

import java.math.BigInteger;

import com.hitachi.coe.fullstack.controller.EmployeeOnBenchDetailController;

/**
 * This interface is used to get the quantity of employee on bench by business
 * unit, location, level, skill API.
 * 
 * @see EmployeeOnBenchDetailController#getQuantityOfEmployeeOnBenchByBusinessUnit(Integer)
 * @see EmployeeOnBenchDetailController#getQuantityOfEmployeesOnBenchByLocation(Integer)
 * @see EmployeeOnBenchDetailController#getQuantityOfEmployeesOnBenchByLevel(Integer)
 * @see EmployeeOnBenchDetailController#getQuantityOfEmployeesOnBenchByCoe(Integer)
 */
public interface IBarChartOnBenchModel {
    String getLabel();

    BigInteger getQuantity();
}