package com.hitachi.coe.fullstack.model;

import com.hitachi.coe.fullstack.model.base.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class EmployeeOnBenchImportModel extends AuditModel<Integer> {

    private String jinzaiId;

    private String staffId;

    private String employeeName;

    private String grade;

    private String benchDays;

    private String location;

    private String primarySkills;

    private Date dateOfJoin;

    private Integer categoryCode;

    private String category;

    private String justification;

    private String plannedBillableDate;

    private String accountsBlocked;

    private String trainingUpskillsinProject;

    private Date statusChangeDate;

    private String cop;

    private String coEPractice;

    private String practice;

    private String benchDaysBucket;

    private String excludeInclude;

    private String role;

}
