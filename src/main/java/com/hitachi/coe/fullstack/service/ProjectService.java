package com.hitachi.coe.fullstack.service;

import com.hitachi.coe.fullstack.model.ExcelResponseModel;
import com.hitachi.coe.fullstack.model.ImportResponse;
import com.hitachi.coe.fullstack.model.ProjectCountModel;
import com.hitachi.coe.fullstack.model.ProjectModel;
import com.hitachi.coe.fullstack.model.ProjectSearchModel;
import com.hitachi.coe.fullstack.model.ProjectUpdateModel;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProjectService {
	/**
	 * Searches for projects based on the provided conditions.
	 *
	 * @author ngocnv
	 * @param keyword         		The keyword to search for. If null, no keyword
	 *                        		filtering will be applied.
	 * @param projectManager   	The Project Manager to filter by. If null, no filtering
	 *                        		by Project Manager name will be applied.
	 * @param bdName 		The Business Domain to filter by. If null, no
	 *                        		filtering by Business Domain will be applied.
	 * @param status      	   		The Status name to filter by. If null, no filtering by
	 *                        		Status will be applied .
	 * @param fromDateStr     		The start date for filtering in string format (e.g.,
	 *                       		"yyyy-MM-dd HH:mm:ss"). If null, no filtering by start
	 *                        		date will be applied.
	 * @param toDateStr       		The end date for filtering in string format (e.g.,
	 *                       		"yyyy-MM-dd HH:mm:ss"). If null, no filtering by end
	 *                        		date will be applied.
	 * @param no              		The page number to retrieve.
	 * @param limit           		The maximum project of results to return per page.
	 * @param sortBy         		The field to sort the results by.
	 * @param desc           		The field to sort desc or asc the results.
	 * @exception Throw exception when input invalid time stamp format
	 * @return A page of employee models that match the selected conditions.
	 */
	 Page<ProjectSearchModel> searchProjects(String keyword, String bdName, String ptName, String projectManager, Integer status,
											 String fromDateStr, String toDateStr, Integer no, Integer limit, String sortBy, Boolean desc);

	/**
	 * Imports project data from an Excel/CSV file.
	 *
	 * @param listOfProject the list of Excel Response Model that contains data to import into the Project table.
	 * @return the ImportResponse object.
	 * @author tcongle
	 * @author tquangpham edited in 17/08//2023
	 */
	 ImportResponse importProject(ExcelResponseModel listOfProject);

	/**
	 * @author tien cong le
	 * @param projectModel data to create new project
	 * @return id of the new project
	 */
	 Integer add(ProjectModel projectModel);

	/**
	 * Update project based on the ProjectUpdateModel
	 *
	 * @author tquangpham
	 * @param projectModel The ProjectUpdateModel of the project to be updated.
	 * @return A ProjectModel representing the closed project.
	 */
	ProjectModel update(final ProjectUpdateModel projectModel);

	/**
	 * Update status for project based on the projectId provided
	 *
	 * @param projectId The ID of the project to be closed.
	 * @return A Project object representing the closed project.
	 */
	ProjectModel closeProject(Integer projectId);

	/**
	 * Get Detail of project by project id
	 *
	 * @param id The ID of the project.
	 * @return A ProjectModel.
	 */
	ProjectModel getProjectDetailById(Integer id);

	/**
	 * Update project status based on the ProjectUpdateModel
	 *
	 * @author tquangpham
	 * @param status The status of the project to be updated.
	 * @param projectId The id of the project to be updated.
	 * @return A ProjectModel representing the closed project.
	 */
	ProjectModel updateProjectStatus(final Integer projectId, final Integer status);

	/**
	 * Get newly created project by buId
	 *
	 * @param buId The ID of the business unit.
	 * @return list of A ProjectModel.
	 */
	List<ProjectModel> getNewlyCreatedProject(Integer buId);

	/**
	 * Get number of newly created project by buId
	 *
	 * @param buId The ID of the business unit.
	 * @return total number of newly created project based on BuId.
	 */
	Integer countNewlyCreatedProject(Integer buId);

	/**
	 * Get expired project by buId
	 *
	 * @param buId The ID of the business unit.
	 * @return list of A ProjectModel.
	 */
	List<ProjectModel> getExpiredProject(Integer buId);

	/**
	 * Get number of expired project by buId
	 *
	 * @param buId The ID of the business unit.
	 * @return total number of expired project based on BuId.
	 */
	Integer countExpiredProject(Integer buId);

	/**
	 * count projects according to buId and status
	 *
	 * @param buId The ID of the business unit.
	 * @return total of projects according to buId and status.
	 */
	Integer countProjectByBuIdAndStatus(Integer buId, Integer status);

	/**
	 * count total projects, newly created projects and close projects according to buId
	 *
	 * @param buId The ID of the business unit.
	 * @return lists of total projects, newly created projects and close projects according to buId.
	 */
	List<ProjectCountModel> countProjectsForPieChart(Integer buId);

}
