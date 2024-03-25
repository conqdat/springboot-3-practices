package com.hitachi.coe.fullstack.service.impl;

import com.hitachi.coe.fullstack.constant.CommonConstant;
import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.entity.Project;
import com.hitachi.coe.fullstack.entity.ProjectTech;
import com.hitachi.coe.fullstack.entity.SkillSet;
import com.hitachi.coe.fullstack.exceptions.CoEException;
import com.hitachi.coe.fullstack.exceptions.InvalidDataException;
import com.hitachi.coe.fullstack.model.SkillSetModel;
import com.hitachi.coe.fullstack.repository.ProjectTechRepository;
import com.hitachi.coe.fullstack.repository.SkillSetRepository;
import com.hitachi.coe.fullstack.service.ProjectTechService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectTechServiceImp implements ProjectTechService {

    @Autowired
    SkillSetRepository skillSetRepository;

    @Autowired
    ProjectTechRepository projectTechRepository;
    
    @Override
    public void addProjectSkill(Project project, SkillSetModel skillSetModel) {
        ProjectTech projectTech = new ProjectTech();
        Optional<SkillSet> skillSetOpt = skillSetRepository.findById(skillSetModel.getId());
        if (skillSetOpt.isEmpty()) {
            throw new CoEException(
                    ErrorConstant.CODE_SKILL_SET_DO_NOT_EXIST,
                    ErrorConstant.MESSAGE_SKILL_SET_DO_NOT_EXIST);
        }
        projectTech.setSkillSet(skillSetOpt.get());
        projectTech.setProject(project);
        projectTechRepository.save(projectTech);
    }

    @Override
    public void addProjectListSkill(Project project, String[] tech) {
        for (String word : tech) {
            ProjectTech projectTech = new ProjectTech();
            SkillSet skillSet = skillSetRepository.getSkillSetByCode(word.trim());
            if (!ObjectUtils.isEmpty(skillSet)) {
                projectTech.setSkillSet(skillSet);
                projectTech.setProject(project);
                projectTech.setCreatedBy(CommonConstant.CREATED_BY_ADMINISTRATOR);
                projectTech.setCreated(new Date());
                projectTechRepository.save(projectTech);
            }
        }
    }

    @Override
    public void deleteProjectTechByProject(final Integer projectId){
        if(ObjectUtils.isEmpty(projectId)){
            throw new CoEException(ErrorConstant.CODE_PROJECT_NULL, ErrorConstant.MESSAGE_PROJECT_NULL);
        }

        List<ProjectTech> projectTechList = projectTechRepository.findByProjectId(projectId);

        if(ObjectUtils.isEmpty(projectTechList)){
            return;
        }

        projectTechRepository.deleteByProject(projectId);
    }

    @Override
    public void saveProjectTech(final ProjectTech projectTech){
        if (ObjectUtils.isEmpty(projectTech)){
            throw new CoEException(ErrorConstant.CODE_PROJECT_TECH_DO_NOT_EXIST, ErrorConstant.MESSAGE_PROJECT_TECH_DO_NOT_EXIST);
        }
        projectTechRepository.save(projectTech);
    }
}
