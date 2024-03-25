package com.hitachi.coe.fullstack.transformation;

import com.hitachi.coe.fullstack.entity.ProjectType;
import com.hitachi.coe.fullstack.model.ProjectTypeModel;
import com.hitachi.coe.fullstack.transformation.base.AbstractCopyPropertiesTransformer;
import com.hitachi.coe.fullstack.transformation.base.EntityToModelTransformer;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProjectTypeTransformer extends AbstractCopyPropertiesTransformer<ProjectType, ProjectTypeModel>
        implements EntityToModelTransformer<ProjectType, ProjectTypeModel, Integer> {

    public List<ProjectTypeModel> applyList(List<ProjectType> entities) {
        if (null == entities || entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities.stream().map(this::apply).collect(Collectors.toList());
    }

    @Override
    public ProjectTypeModel apply(ProjectType projectType) {
        if (projectType == null) {
            log.debug("ProjectTypeTransformer::apply -> ProjectType is null");
            return null;
        }
        ProjectTypeModel projectTypeModel = new ProjectTypeModel();
        projectTypeModel.setId(projectType.getId());
        projectTypeModel.setCode(projectType.getCode());
        projectTypeModel.setName(projectType.getName());
        projectTypeModel.setCreated(projectType.getCreated());
        projectTypeModel.setCreatedBy(projectType.getCreatedBy());

        return projectTypeModel;
    }
}
