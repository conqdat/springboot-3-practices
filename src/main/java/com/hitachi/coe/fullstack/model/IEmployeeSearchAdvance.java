package com.hitachi.coe.fullstack.model;

import java.util.Date;

public interface IEmployeeSearchAdvance {

    Integer getId();

    String getHccId();

    String getName();

    String getEmail();

    String getLdap();

    String getPm();

    Date getLegalEntityHireDate();

    String getBranchName();

    String getCoeCoreTeamName();

    String getBusinessUnitName();

    String getEmployeeWorkingStatus();

    Boolean getIsWorkingOnAnotherProject();
}
