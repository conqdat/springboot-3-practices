package com.hitachi.coe.fullstack.service;

import com.hitachi.coe.fullstack.entity.Project;
import com.hitachi.coe.fullstack.entity.ProjectTech;
import com.hitachi.coe.fullstack.model.SkillSetModel;

public interface ProjectTechService {
    void addProjectSkill(Project project, SkillSetModel skillSetModel);

    /**
     * Add project techs based on the Project id and list of project code
     *
     * @param project The project need to add tech.
     * @param skillSetModel The list of skillSetModel to be added.
     * @author tquangpham
     */
    void addProjectListSkill(Project project, String[] skillSetModel);

    /**
     * Delete project tech based on the Project id
     *
     * @author tquangpham
     * @param projectId The id of the project to be deleted.
     */
    void deleteProjectTechByProject(final Integer projectId);

    /**
     * Save project tech based on the ProjectTech entity
     *
     * @author tquangpham
     * @param projectTech The ProjectTech of the Project Tech table.
     */
    void saveProjectTech(final ProjectTech projectTech);
}
