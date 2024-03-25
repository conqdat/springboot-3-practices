package com.hitachi.coe.fullstack.model;

import java.util.Date;

/**
 * Represents an IEmployeeSpecificField interface
 */
public interface IEmployeeSpecificField {

    Integer getId();

    String getHccId();

    String getName();

    String getEmail();

    String getLdap();

    Date getLegalEntityHireDate();

}
