package com.hitachi.coe.fullstack.controller;

import com.hitachi.coe.fullstack.constant.Constants;
import com.hitachi.coe.fullstack.model.ProjectCountModel;
import com.hitachi.coe.fullstack.model.ProjectModel;
import com.hitachi.coe.fullstack.model.ProjectUpdateModel;
import com.hitachi.coe.fullstack.model.common.BaseResponse;
import com.hitachi.coe.fullstack.service.ProjectService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @PostMapping("add")
    @ApiOperation("This api add project  will return project Id")
    public BaseResponse<Object> createProjectFeedback(@Validated @RequestBody ProjectModel projectModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
        {
            return BaseResponse.badRequest( bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        Map<String, String> response = new HashMap<>();
        response.put(Constants.ID, String.valueOf(projectService.add(projectModel)));

        return BaseResponse.success(response);
    }

	@GetMapping("/search")
	@ApiOperation("This api will return list of Project matches with one or many conditions ")
	public BaseResponse<?> searchProjects(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) String bdName,@RequestParam(required = false) String ptName,
			@RequestParam(required = false) String projectManager,@RequestParam(required = false) Integer status,
			@RequestParam(required = false) String fromDateStr,@RequestParam(required = false) String toDateStr,
			@RequestParam(defaultValue = "0") Integer no,@RequestParam(defaultValue = "10") Integer limit,
			@RequestParam(defaultValue = "name") String sortBy,@RequestParam(required = false) Boolean desc) {

			return BaseResponse.success(projectService.searchProjects(keyword, bdName, ptName, projectManager, status,
					fromDateStr, toDateStr, no, limit, sortBy,desc));
	}

    /**
     * This api will update project
     *
     * @author tquangpham
     * @param projectModel The model of the ProjectUpdateModel model to update.
     * @return A BaseResponse object with a status code, a message and data.
     */
	@PostMapping("/update")
    @ApiOperation("This api update project will return ProjectModel")
    public BaseResponse<ProjectModel> updateProject(@Valid @RequestBody ProjectUpdateModel projectModel) {
        return new BaseResponse<>(HttpStatus.OK.value(), null, projectService.update(projectModel));
    }

	@PostMapping("/close/{projectId}")
	@ApiOperation("This api update status project will return project")
	public BaseResponse<ProjectModel> closeProject(@PathVariable(name = "projectId") Integer projectId) {
        return new BaseResponse<>(HttpStatus.OK.value(), null,projectService.closeProject(projectId));

    }

    /**
     * This method is used to create an API to get list of employees utilization detail.
     *
     * @return A BaseResponse object with a status code, a message and data.
     * @author tquangpham
     */
    @GetMapping("/detail/{projectId}")
    @ApiOperation("This API will return a detail of project.")
    public BaseResponse<ProjectModel> getProjectDetailById(@PathVariable(name = "projectId") Integer projectId) {
        return new BaseResponse<>(HttpStatus.OK.value(), null,projectService.getProjectDetailById(projectId));
    }

    /**
     * This api will update project status
     *
     * @author tquangpham
     * @param projectId The id of the project.
     * @param status The status of the project.
     * @return A BaseResponse object with a status code, a message and data.
     */
    @PostMapping("/update/status/{projectId}/{status}")
    @ApiOperation("This api update project status will return project id")
    public BaseResponse<ProjectModel> updateProjectStatus(@PathVariable(name = "projectId") Integer projectId, @PathVariable(name = "status") Integer status) {
        return new BaseResponse<>(HttpStatus.OK.value(), null, projectService.updateProjectStatus(projectId, status));
    }

    /**
     * Return a list of new created project
     * @param buId (optional) the ID of the project to retrieve the data
     * @author ThuyTrinhThanhLe
     */
    @GetMapping("/get-new-project")
    @ApiOperation("This api will return list of newly created projects")
    public BaseResponse<List<ProjectModel>> getNewlyCreatedProject(@RequestParam(value="buId", required = false) Integer buId) {
        return BaseResponse.success(projectService.getNewlyCreatedProject(buId));
    }

    /**
     * Return number of newly created projects
     * @param buId (optional) the ID of the project to retrieve the data
     * @author ThuyTrinhThanhLe
     */
    @GetMapping("/count-new-project")
    @ApiOperation("This api will return total number of newly created projects")
    public ResponseEntity<Integer> countNewlyCreatedProject(@RequestParam(value="buId", required = false) Integer buId) {
        Integer result = projectService.countNewlyCreatedProject(buId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Return a list of expired project
     * @param buId (optional) the ID of the project to retrieve the data
     * @author ThuyTrinhThanhLe
     */
    @GetMapping("/get-expired-project")
    @ApiOperation("This api will return list of expired projects")
    public BaseResponse<List<ProjectModel>> getExpiredProject(@RequestParam(value="buId", required = false) Integer buId) {
        return BaseResponse.success(projectService.getExpiredProject(buId));
    }

    /**
     * Return number of expired projects
     * @param buId (optional) the ID of the project to retrieve the data
     * @author ThuyTrinhThanhLe
     */
    @GetMapping("/count-expired-project")
    @ApiOperation("This api will return total number of expired projects")
    public ResponseEntity<Integer> countExpiredProject(@RequestParam(value="buId", required = false) Integer buId) {
        Integer result = projectService.countExpiredProject(buId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Return number of projects by buId and status
     * @param buId the ID of the project to retrieve the data
     * @param status the status of the project to retrieve the data
     * @author ThuyTrinhThanhLe
     */
    @GetMapping("/count-project")
    @ApiOperation("This api will return total number of expired projects")
    public ResponseEntity<Integer> countProjectByBuIdAndStatus(@RequestParam(value="buId", required = true) Integer buId,
                                                               @RequestParam(value="status", required = true) Integer status) {
        Integer result = projectService.countProjectByBuIdAndStatus(buId, status);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Return total number of projects, newly created projects and close projects according to buId
     * @param buId (optional) the ID of the project to retrieve the data
     * @author ThuyTrinhThanhLe
     */
    @GetMapping("/count-total-new-expired-projects")
    @ApiOperation("This api will return number of projects conditions")
    public ResponseEntity<List<ProjectCountModel>> countProjectsForPieChart(@RequestParam(value="buId", required = false) Integer buId) {
        return new ResponseEntity<>(projectService.countProjectsForPieChart(buId), HttpStatus.OK);
    }
}
