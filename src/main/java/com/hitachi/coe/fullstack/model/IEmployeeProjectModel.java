package com.hitachi.coe.fullstack.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hitachi.coe.fullstack.util.DateTimeSerializer;

import java.util.Date;

/**
 * Represents an IEmployeeProjectModel interface
 */
public interface IEmployeeProjectModel {

    Integer getEmployeeProjectId();

    Integer getProjectId();

    Integer getEmployeeId();

    String getHccId();

    String getName();

    String getEmail();

    Integer getEmployeeType();

    @JsonSerialize(using = DateTimeSerializer.class)
    Date getStartDate();

    @JsonSerialize(using = DateTimeSerializer.class)
    Date getEndDate();

    @JsonSerialize(using = DateTimeSerializer.class)
    Date getCreatedDate();

    @JsonSerialize(using = DateTimeSerializer.class)
    Date getReleaseDate();

    Integer getUtilization();

    String getPmName();

}
