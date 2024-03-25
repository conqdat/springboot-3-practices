package com.hitachi.coe.fullstack.model;

import com.hitachi.coe.fullstack.model.base.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeLeaveModel extends AuditModel<Integer> {
    private String ldap;

    private String oracleId;

    private String employeeName;

    private String level;

    private Date legalEntityHireDate;

    private String email;

    private String businessUnit;

    private String location;

    private Date leaveDate;

    private String reportManager;

    private String hinextManager;

    private String buCode;

    private String reason;

}
