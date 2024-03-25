package com.hitachi.coe.fullstack.model;

import com.hitachi.coe.fullstack.model.base.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectImportModel extends AuditModel<Integer> {

    private static final long serialVersionUID = -392837135186039875L;

    private String projectCode;

    private String projectName;

    private String customerName;

    private String projectStatus;

    private Date startDate;

    private Date endDate;

    private String contractType;

    private String businessUnit;

    private String projectManager;

    private String businessDomain;

    private String shortDescription;

    private String description;

    private String skillSetList;
}