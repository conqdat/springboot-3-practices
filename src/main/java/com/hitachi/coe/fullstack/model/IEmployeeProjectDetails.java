package com.hitachi.coe.fullstack.model;

import java.util.Date;

public interface IEmployeeProjectDetails {
    Integer getEmployeeType();

    Date getStartDate();

    Date getEndDate();

    Date getCreatedDate();

    Date getReleaseDate();

    String getProjectCode();

    String getProjectName();

    String getProjectManager();

    Integer getProjectStatus();

    String getProjectTypeName();

    Integer getUtilization();

}
