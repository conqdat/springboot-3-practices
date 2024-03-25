package com.hitachi.coe.fullstack.transformation;

import com.hitachi.coe.fullstack.entity.BusinessDomain;
import com.hitachi.coe.fullstack.entity.BusinessUnit;
import com.hitachi.coe.fullstack.entity.Project;
import com.hitachi.coe.fullstack.entity.ProjectStatus;
import com.hitachi.coe.fullstack.entity.ProjectType;
import com.hitachi.coe.fullstack.model.BusinessUnitModel;
import com.hitachi.coe.fullstack.model.ProjectModel;
import com.hitachi.coe.fullstack.transformation.base.AbstractCopyPropertiesTransformer;
import com.hitachi.coe.fullstack.transformation.base.ModelToEntityTransformer;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class BusinessUnitTransformer is convert entity to DTO.
 *
 * @author lphanhoangle
 */
@Slf4j
@Component
public class ProjectModelTransformer extends AbstractCopyPropertiesTransformer<ProjectModel, Project>
        implements ModelToEntityTransformer<ProjectModel, Project, Integer> {
    /**
     * Transformer array entities to array DTO.
     *
     * @param entities {@link List} of {@link BusinessUnit}
     * @return {@link List} of {@link BusinessUnitModel}
     */
    public List<Project> applyList(List<ProjectModel> entities) {
        if (null == entities || entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities.stream().map(this::apply)
                .collect(Collectors.toList());
    }

    @Override
    public Project apply(ProjectModel model) {
        if (model == null) {
            log.debug("ProjectModelTransformer::apply -> model is null");
            return null;
        }
        Project project = super.apply(model);
        if (model.getStatus() != null) {
            ProjectStatus status = ProjectStatus.valueOf(model.getStatus());
            project.setStatus(status);
        }
        if (model.getProjectType() != null) {
            ProjectType projectType = new ProjectType();
            projectType.setId(model.getProjectType().getId());
            project.setProjectType(projectType);
        }
        if (model.getBusinessDomain() != null) {
            BusinessDomain businessDomain = new BusinessDomain();
            businessDomain.setId(model.getBusinessDomain().getId());
            project.setBusinessDomain(businessDomain);
        }
        if (model.getBusinessUnitId() != null) {
            BusinessUnit businessUnit = new BusinessUnit();
            businessUnit.setId(model.getBusinessUnitId());
            project.setBusinessUnit(businessUnit);
        }
        project.setId(model.getId());
        project.setCreatedBy(model.getCreatedBy());
        return project;
    }
}
