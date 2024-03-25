package com.hitachi.coe.fullstack.transformation;

import com.hitachi.coe.fullstack.entity.BusinessDomain;
import com.hitachi.coe.fullstack.model.BusinessDomainModel;
import com.hitachi.coe.fullstack.model.ProjectModel;
import com.hitachi.coe.fullstack.transformation.base.AbstractCopyPropertiesTransformer;
import com.hitachi.coe.fullstack.transformation.base.EntityToModelTransformer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The Class BusinessDomainTransformer is convert entity to DTO.
 *
 * @author thovo
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class BusinessDomainTransformer extends AbstractCopyPropertiesTransformer<BusinessDomain, BusinessDomainModel>
        implements EntityToModelTransformer<BusinessDomain, BusinessDomainModel, Integer> {
    /**
     * Transformer array entities to array DTO.
     *
     * @param entities {@link List} of {@link BusinessDomain}
     * @return {@link List} of {@link BusinessDomainModel}
     */

    public List<BusinessDomainModel> applyList(List<BusinessDomain> entities) {
        if (null == entities || entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities.stream().map(this::apply)
                .collect(Collectors.toList());
    }

    @Override
    public BusinessDomainModel apply(BusinessDomain businessDomain) {
        if (Objects.isNull(businessDomain)) {
            log.debug("BusinessDomainTransformer::apply -> BusinessDomain is null");
            return null;
        }

        List<ProjectModel> listProject = new ArrayList<>();
        if (businessDomain.getProjects() != null) {
            listProject = businessDomain.getProjects().stream().map(project -> {
                ProjectModel model = new ProjectModel();
                model.setId(project.getId());
                model.setCode(project.getCode());
                model.setCustomerName(project.getCustomerName());
                model.setDescription(project.getDescription());
                model.setEndDate(project.getEndDate());
                model.setName(project.getName());
                model.setProjectManager(project.getProjectManager());
                model.setStartDate(project.getStartDate());
                model.setStatus(project.getStatus() != null
                        ? project.getStatus().getValue()
                        : null);
                model.setCreatedBy(project.getCreatedBy());
                return model;
            }).collect(Collectors.toList());
        }
        BusinessDomainModel model = new BusinessDomainModel();
        model.setId(businessDomain.getId());
        model.setCode(businessDomain.getCode());
        model.setName(businessDomain.getName());
        model.setDescription(businessDomain.getDescription());
        model.setPracticeId(businessDomain.getPractice() != null
                ? businessDomain.getPractice().getId()
                : null);
        model.setProjects(listProject);
        model.setCreated(businessDomain.getCreated());
        model.setCreatedBy(businessDomain.getCreatedBy());
        model.setUpdated(businessDomain.getUpdated());
        model.setUpdatedBy(businessDomain.getUpdatedBy());
        return model;
    }
}
