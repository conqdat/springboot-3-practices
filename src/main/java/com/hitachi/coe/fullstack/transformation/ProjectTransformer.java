package com.hitachi.coe.fullstack.transformation;

import com.hitachi.coe.fullstack.entity.Project;
import com.hitachi.coe.fullstack.model.ProjectModel;
import com.hitachi.coe.fullstack.transformation.base.AbstractCopyPropertiesTransformer;
import com.hitachi.coe.fullstack.transformation.base.EntityToModelTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectTransformer extends AbstractCopyPropertiesTransformer<Project, ProjectModel>
                implements EntityToModelTransformer<Project, ProjectModel, Integer> {

        private final ProjectTypeTransformer projectTypeTransformer;

        private final BusinessDomainTransformer businessDomainTransformer;

        private final BusinessUnitTransformer businessUnitTransformer;

        public List<ProjectModel> applyList(List<Project> entities) {
                if (null == entities || entities.isEmpty()) {
                        return Collections.emptyList();
                }

                return entities.stream().map(this::apply)
                                .collect(Collectors.toList());
        }

        @Override
        public ProjectModel apply(Project entity) {
                if (entity == null) {
                        log.debug("ProjectTransformer::apply -> Project is null");
                        return null;
                }
                ProjectModel model = new ProjectModel();
                model.setId(entity.getId());
                model.setCode(entity.getCode());
                model.setCustomerName(entity.getCustomerName());
                model.setDescription(entity.getDescription());
                model.setEndDate(entity.getEndDate());
                model.setName(entity.getName());
                model.setProjectManager(entity.getProjectManager());
                model.setStartDate(entity.getStartDate());
                model.setStatus(entity.getStatus() != null
                                ? entity.getStatus().getValue()
                                : null);
                model.setProjectType(entity.getProjectType() != null
                                ? projectTypeTransformer.apply(entity.getProjectType())
                                : null);
                model.setBusinessDomain(entity.getBusinessDomain() != null
                                ? businessDomainTransformer.apply(entity.getBusinessDomain())
                                : null);
                model.setBusinessUnitId(entity.getBusinessUnit() != null
                                ? entity.getBusinessUnit().getId()
                                : null);
                model.setCreated(entity.getCreated());
                model.setCreatedBy(entity.getCreatedBy());
                model.setUpdated(entity.getUpdated());
                model.setUpdatedBy(entity.getUpdatedBy());

                return model;
        }

}
