package com.hitachi.coe.fullstack.model.common;

import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.entity.Employee;
import com.hitachi.coe.fullstack.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The class ErrorLineModel is used to define errors in each field with a message.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorLineModel {
    private String field;
    private String message;

    /**
     * Imports error details from error list to list of ErrorModel model.
     *
     * @param employee     the employee object.
     * @param branches     the list of branches name.
     * @param levels       the list of levels name.
     * @param branch       the String branch name.
     * @param businessUnit the String business Unit name.
     * @param level        the String level name.
     * @return the list of ErrorLineModel.
     * @author tquangpham
     */
    public static List<ErrorLineModel> importEmployeeErrorDetails(Employee employee, List<String> branches, List<String> businessUnits, List<String> levels, String branch, String businessUnit, String level) {

        List<ErrorLineModel> errorLineModelList = new ArrayList<>();
        if (ObjectUtils.isEmpty(employee)) {
            errorLineModelList.add(new ErrorLineModel("Employee", ErrorConstant.MESSAGE_EMPLOYEE_DO_NOT_EXIST));
        }
        if (!branches.contains(branch)) {
            errorLineModelList.add(new ErrorLineModel("Branch", ErrorConstant.MESSAGE_BRANCH_DO_NOT_EXIST));
        }
        if (!businessUnits.contains(businessUnit)) {
            errorLineModelList.add(new ErrorLineModel("Business Unit", ErrorConstant.MESSAGE_BUSINESS_UNIT_DO_NOT_EXIST));
        }
        if (!levels.contains(level)) {
            errorLineModelList.add(new ErrorLineModel("Level", ErrorConstant.MESSAGE_LEVEL_DO_NOT_EXIST));
        }
        return errorLineModelList;
    }

    public static List<ErrorLineModel> importInsertEmployeeErrorDetails(List<Employee> employee, List<String> branches, List<String> businessUnits, List<String> levels, List<String> teams, String branch, String businessUnit, String level, String team) {

        List<ErrorLineModel> errorLineModelList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(employee)) {
            errorLineModelList.add(new ErrorLineModel("Employee", ErrorConstant.MESSAGE_HCCID_LDAP_EMAIL_ALEARDY_EXIST));
        }
        if (!branches.contains(branch)) {
            errorLineModelList.add(new ErrorLineModel("Branch", ErrorConstant.MESSAGE_BRANCH_DO_NOT_EXIST));
        }
        if (!businessUnits.contains(businessUnit)) {
            errorLineModelList.add(new ErrorLineModel("Business Unit", ErrorConstant.MESSAGE_BUSINESS_UNIT_DO_NOT_EXIST));
        }
        if (!levels.contains(level)) {
            errorLineModelList.add(new ErrorLineModel("Level", ErrorConstant.MESSAGE_LEVEL_DO_NOT_EXIST));
        }
        if (!teams.contains(team)) {
            errorLineModelList.add(new ErrorLineModel("Coe Core Team", ErrorConstant.MESSAGE_TEAM_DO_NOT_EXIST));
        }
        return errorLineModelList;
    }

    /**
     * Imports error details from error list to list of ErrorModel model.
     *
     * @param project         the project object.
     * @param status          the project status.
     * @param businessUnits   the list of Business Units code.
     * @param businessUnit    the Business Unit code.
     * @param businessDomains the list of Business Domains code.
     * @param businessDomain  the Business Domain code.
     * @param projectTypes    the list of Project Types code.
     * @param projectType     the Project Type code.
     * @return the list of ErrorLineModel.
     * @author tquangpham
     */
    public static List<ErrorLineModel> importProjectErrorDetails(Project project, String status, List<String> businessUnits, String businessUnit, List<String> businessDomains, String businessDomain, List<String> projectTypes, String projectType) {

        List<ErrorLineModel> errorLineModelList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(project)) {
            errorLineModelList.add(new ErrorLineModel("Project", ErrorConstant.MESSAGE_PROJECT_CODE_EXIST));
        }

        if (!businessDomains.contains(businessDomain)) {
            errorLineModelList.add(new ErrorLineModel("Domain", ErrorConstant.MESSAGE_BUSINESS_DOMAIN_DO_NOT_EXIST));
        }

        if (!businessUnits.contains(businessUnit)) {
            errorLineModelList.add(new ErrorLineModel("BU", ErrorConstant.MESSAGE_BUSINESS_UNIT_DO_NOT_EXIST));
        }

        if (!projectTypes.contains(projectType)) {
            errorLineModelList.add(new ErrorLineModel("Contract Type", ErrorConstant.MESSAGE_PROJECT_TYPE_DO_NOT_EXIST));
        }

        if (status != null) {
            errorLineModelList.add(new ErrorLineModel("Project status", status + ": " + ErrorConstant.MESSAGE_PROJECT_STATUS_EXIST));
        }
        return errorLineModelList;

    }

    /**
     * Imports error details from error list to list of ErrorModel model.
     *
     * @param field   the field of either object field or excel column.
     * @param message the message string to display
     * @return the list of ErrorLineModel.
     * @author ngocth
     */
    public static List<ErrorLineModel> createErrorDetails(String field, String message) {
        List<ErrorLineModel> errorLineModelList = new ArrayList<>();
        errorLineModelList.add(new ErrorLineModel(field, message));
        return errorLineModelList;
    }
}
