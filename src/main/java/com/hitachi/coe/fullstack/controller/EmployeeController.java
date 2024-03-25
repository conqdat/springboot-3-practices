package com.hitachi.coe.fullstack.controller;

import com.hitachi.coe.fullstack.constant.Constants;
import com.hitachi.coe.fullstack.model.BarChartModel;
import com.hitachi.coe.fullstack.model.ChartSkillAndLevelModel;
import com.hitachi.coe.fullstack.model.CountEmployeeModel;
import com.hitachi.coe.fullstack.model.EmployeeModel;
import com.hitachi.coe.fullstack.model.EmployeeStatusModel;
import com.hitachi.coe.fullstack.model.IBarChartDepartmentModel;
import com.hitachi.coe.fullstack.model.IEmployeeDetails;
import com.hitachi.coe.fullstack.model.IEmployeeSearchAdvance;
import com.hitachi.coe.fullstack.model.IEmployeeSearchForTeam;
import com.hitachi.coe.fullstack.model.IEmployeeSpecificField;
import com.hitachi.coe.fullstack.model.IPieChartModel;
import com.hitachi.coe.fullstack.model.common.BaseResponse;
import com.hitachi.coe.fullstack.service.EmployeeService;
import com.hitachi.coe.fullstack.service.EmployeeStatusService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    private final EmployeeStatusService employeeStatusService;

    /**
     * Non-JavaDoc
     * 
     * @deprecated use
     *             {@link #searchEmployeesAdvance(String, Integer, Integer, Integer, String, Integer, String, String, Integer, Integer, String, Boolean)}
     *             instead.
     */
    @Deprecated
	@GetMapping("/search")
	@ApiOperation("This api will return list of Employee matches with one or many conditions ")
    public BaseResponse<?> searchEmployees(@RequestParam(required = false) String keyword,
                                           @RequestParam(required = false) String businessUnitName,
                                           @RequestParam(required = false) String coeCoreTeamName,
                                           @RequestParam(required = false) String branchName,
                                           @RequestParam(required = false) Integer status,
                                           @RequestParam(required = false) String fromDateStr,
                                           @RequestParam(required = false) String toDateStr,
                                           @RequestParam(defaultValue = "0") Integer no,
                                           @RequestParam(defaultValue = "10") Integer limit,
                                           @RequestParam(defaultValue = "name") String sortBy,
                                           @RequestParam(required = false) Boolean desc) {
        return BaseResponse.success(employeeService.searchEmployees(keyword, businessUnitName, coeCoreTeamName, branchName, status,
                fromDateStr, toDateStr, no, limit, sortBy, desc));
    }

    @GetMapping("/details/hccid/{hccId}")
    @ApiOperation("This api will return details employee information")
    public BaseResponse<IEmployeeDetails> getEmployeeDetailsByHccId(@PathVariable(name = "hccId", required = true) String hccId) {
        return BaseResponse.success(employeeService.getEmployeeDetailsByHccId(hccId));
    }

    @GetMapping("/member/{employeeId}")
    @ApiOperation("This api will return employee")
    public ResponseEntity<EmployeeModel> getEmployeeById(@PathVariable(name = "employeeId", required = true) Integer employeeId) {
        return new ResponseEntity<>(employeeService.getEmployeeById(employeeId), HttpStatus.OK);
    }

    @PostMapping("member/create")
    @ApiOperation("This api add employee will employee id")
    public BaseResponse<Object> createEmployee(@Validated @RequestBody EmployeeModel emp) {
        Map<String, String> response = new HashMap<>();
        response.put(Constants.ID, String.valueOf(employeeService.add(emp)));
        return BaseResponse.success(response);
    }

    @PutMapping("member/update")
    @ApiOperation("This api add employee will return status and employee id")
    public BaseResponse<Object> updateEmployee(@RequestBody EmployeeModel employeemodel) {
        Map<String, String> response = new HashMap<>();
        response.put(Constants.ID, String.valueOf(employeeService.updateEmployee(employeemodel)));
        return BaseResponse.success(response);
    }

    @DeleteMapping("delete-employee/{id}")
    @ApiOperation("This api will soft delete employee has id in params and save information user changed ")
    public BaseResponse<EmployeeStatusModel> deleteEmployeeById(@PathVariable("id") Integer id) {
        employeeService.deleteEmployeeById(id);
        return BaseResponse.success(employeeStatusService.deleteEmployeeById(id));
    }

    /**
     * Retrieves the quantity of employees in each department at a certain level within an organization.
     *
     * @author hchantran
     * @param branchId (optional) The ID of the branch to filter by.
     * @param groupId (optional) The ID of the group to filter by.
     * @param teamId (optional) The ID of the team to filter by.
     * @return a ResponseEntity containing the quantity of employees in each department at a certain level.
     */
    @GetMapping("/get-department-level")
    @ApiOperation("This api will calculate the quantity of each department of level for the employee")
    public ResponseEntity<List<IBarChartDepartmentModel>> getQuantityOfLevel(@RequestParam(required = false) Integer branchId, @RequestParam(required = false) Integer groupId, @RequestParam(required = false) Integer teamId) {
        return ResponseEntity.ok(employeeService.getQuantityEmployeeOfLevel(branchId, groupId, teamId));
    }


    /**
     *
     * Get the list of quantity for each skill, based on the provided branch, group,
     * and team IDs.
     *
     * @param branchId (optional) the ID of the branch to retrieve the data
     * @param groupId  (optional) the ID of the group within the branch to retrieve
     *                 the data
     * @param teamId   (optional) the ID of the team within the group to retrieve
     *                 the data
     * @param SkillIds (optional) The list Id of the skill to filter the data
     *                 Use format like as '1,2,...'
     * @return a ResponseEntity containing the list of quantity for each skill
     */
    @GetMapping("/get-quantity-of-each-skill")
    @ApiOperation("This api will return list of quantity for each skill")
    public ResponseEntity<?> getPercentofSkills(@RequestParam(required = false) Integer branchId,
                                                @RequestParam(required = false) Integer groupId, @RequestParam(required = false) Integer teamId,
                                                @RequestParam(required = false) List<Integer> skillIds) {
        return ResponseEntity
                .ok(employeeService.getQuantityOfEachSkillForBarChart(branchId, groupId, teamId, skillIds));
    }

    @GetMapping("/get-quantity-level")
    @ApiOperation("This api will calculate the quantity of level for the employee")
    public ResponseEntity <BarChartModel> getQuantityOfLevelForBarChart(@RequestParam(required = false) Integer branchId, @RequestParam(required = false) Integer groupId, @RequestParam(required = false) Integer teamId) {
        return ResponseEntity.ok(employeeService.getQuantityOfLevelForBarChart(branchId, groupId, teamId));
    }

    /**
     Get pie chart data of employee level distribution from database based on filter criteria
     @param branchId Branch ID
     @param coeCoreTeamId COE Core Team ID
     @param coeId COE ID
     @return List of pie chart model containing data for generating pie chart
     */
    @GetMapping("/level")
    public BaseResponse<List<IPieChartModel>> getLevelPieChart(
            @RequestParam(required = false) Integer branchId,
            @RequestParam(required = false) Integer coeCoreTeamId,
            @RequestParam(required = false) Integer coeId) {
        return new BaseResponse<>(HttpStatus.OK.value(), null,  employeeService.getLevelPieChart(branchId, coeCoreTeamId, coeId));
    }

    /**
     * This API search employee that match given condition.
     *
     * @param keyword The keyword for searching.
     * @param businessUnitId The project id of Project.
     * @param workingStatus The employee working status .
     * @param status The status of Employee.
     * @param no     The page number to retrieve.
     * @param limit   The maximum employee of results to return per page.
     * @param sortBy  The field to sort the results by.
     * @param desc    The field to sort desc or asc the results.
     * @return BaseResponse containing the page of employees.
     * @author tquangpham
     */
    @GetMapping("/search-advance")
    public BaseResponse<Page<IEmployeeSearchAdvance>> searchEmployeesAdvance(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer businessUnitId,
            @RequestParam(required = false) Integer coeCoreTeamId,
            @RequestParam(required = false) Integer branchId,
            @RequestParam(required = false) String workingStatus,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(defaultValue = "0") Integer no,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) Boolean desc) {
        return new BaseResponse<>(HttpStatus.OK.value(), null,
                employeeService.searchEmployeesAdvance(keyword, businessUnitId, coeCoreTeamId, branchId, workingStatus,
                        status, fromDate, toDate, no, limit, sortBy, desc));
    }

    /**
	 *
	 * Get the list of quantity of employees and count of each skill level for each skill, based on the provided branch, coe, skillIDs.
	 *
	 * @param branchId (optional) the ID of the branch to retrieve the data
	 * @param coeId  (optional) the ID of the Coe to retrieve the data 
	 * @param SkillIds  The list Id of the skill to filter the data
	 *                 Use format like as '1,2,...'
	 * @return a ResponseEntity containing a list skill name and datasets of each skill
     * @author SangBui
	 */
	@GetMapping("/get-count-of-each-skill")
	@ApiOperation("This api will return a list of skill name and datasets of each skill, including level count and BU name")
	public ResponseEntity<ChartSkillAndLevelModel> getQuantityOfEmployeesForEachSkill(@RequestParam(required = false) Integer branchId,
			@RequestParam(required = false) Integer coeId,
			@RequestParam(required = false) List<Integer> skillIds) {
		return ResponseEntity
				.ok(employeeService.getQuantityOfEmployeesForEachSkill(branchId, coeId, skillIds));
	}

    /**
    * Search employee by provided conditions.
    * @param teamId    The id of coe core team.
    * @return Employee DTO that matches the given conditions.
    * @author ThuyTrinhThanhLe
    */
    @GetMapping("/search-by-team/{teamId}")
    @ApiOperation("This api will return employee matches with teamId ")
    public ResponseEntity<List<EmployeeModel>> getEmployeesByTeam(@PathVariable("teamId") Integer teamId) {
        return new ResponseEntity<>(employeeService.getEmployeesByTeamId(teamId), HttpStatus.OK);
    }

    /**
     * Get specific fields of employee by teamId.
     * @param teamId    The id of coe core team.
     * @return a list of team with specific fields.
     * @author ThuyTrinhThanhLe
     */
    @GetMapping("/search-specific-field-by-team/{teamId}")
    @ApiOperation("This api will return details employee information with teamId")
    public BaseResponse<List<IEmployeeSpecificField>> getEmployeeSpecificFieldByTeam(@PathVariable(value="teamId", required = false) Integer teamId) {
        return new BaseResponse<>(HttpStatus.OK.value(), null, employeeService.getEmployeeSpecificFieldByTeam(teamId));
    }

    /**
	 * Get the count of total employees, employees on projects, employees on bench and new employees and calculate
	 * their difference from the previous month in percentage.
	 * @return a list of count of employees in each category and their different percentages
     * @author SangBui
	 */
	@GetMapping("/get-count-of-employees")
	@ApiOperation("This api will return the total employees, employees on projects, employees on bench, new employees in the current month and their difference from the previous month in percentage")
	public ResponseEntity<CountEmployeeModel> getCountOfEmployeesForCurrentMonth() {
		return ResponseEntity
				.ok(employeeService.getCountOfEmployeesForCurrentMonth());
	}

    /**
     * Filters employees based on the provided conditions.
     *
     * @param nameString       The name string to search for. If null, no name string
     *                      filtering will be applied.
     * @param coeId          The center of excellence id to filter by. If null, no filtering
     *                      by coeId will be applied.
     * @return A List of employees model that match the selected conditions.
     * @author SangBui
     * @see IEmployeeSearchForTeam
     */
    @GetMapping("/search-by-coe-and-name")
    @ApiOperation("This api will return information of employees that match the searching conditions")
	public BaseResponse<List<IEmployeeSearchForTeam>> searchEmployeesByCoeAndNameString(
                        @RequestParam(required = false) Integer coeId, 
                        @RequestParam(required = false) String nameString) {
        if(Objects.isNull(coeId) && Objects.isNull(nameString)) {
            return BaseResponse.badRequest("Input at least one parameter");
        }
        return BaseResponse.success(employeeService.searchEmployeesByCoeAndNameString(coeId, nameString));
    }
}
