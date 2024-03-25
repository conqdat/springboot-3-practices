package com.hitachi.coe.fullstack.transformation;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.hitachi.coe.fullstack.model.CenterOfExcellenceModel;
import com.hitachi.coe.fullstack.model.EmployeeModel;
import com.hitachi.coe.fullstack.repository.EmployeeRepository;

import org.springframework.stereotype.Component;

import com.hitachi.coe.fullstack.entity.CenterOfExcellence;
import com.hitachi.coe.fullstack.entity.CoeCoreTeam;
import com.hitachi.coe.fullstack.entity.Employee;
import com.hitachi.coe.fullstack.model.CoeCoreTeamModel;
import com.hitachi.coe.fullstack.transformation.base.AbstractCopyPropertiesTransformer;
import com.hitachi.coe.fullstack.transformation.base.EntityToModelTransformer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This CoeCoreTeamTransformer is transform entity to DTO.
 *
 * @author lphanhoangle
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CoeCoreTeamTransformer extends AbstractCopyPropertiesTransformer<CoeCoreTeam, CoeCoreTeamModel>
        implements EntityToModelTransformer<CoeCoreTeam, CoeCoreTeamModel, Integer> {

    private final CenterOfExcellenceTransformer centerOfExcellenceTransformer;

    private final EmployeeRepository employeeRepository;

    /**
     * Transform array entities to array DTO.
     *
     * @param entities {@link List} of {@link CoeCoreTeam}
     * @return {@link List} of {@link CoeCoreTeamModel}
     */
    public List<CoeCoreTeamModel> applyList(List<CoeCoreTeam> entities) {
        if (null == entities || entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities.stream().map(this::apply).collect(Collectors.toList());
    }

    /**
     * Transform entity to DTO.
     *
     * @param coeCoreTeam {@link CoeCoreTeam} entity
     * @return {@link CoeCoreTeamModel} DTO
     */
    @Override
    public CoeCoreTeamModel apply(CoeCoreTeam entity) {
        if (entity == null) {
            log.debug("CoeCoreTeamTransformer::apply -> CoeCoreTeam is null");
            return null;
        }

        CoeCoreTeamModel model = new CoeCoreTeamModel();
        model.setId(entity.getId());
        model.setCode(entity.getCode());
        model.setName(entity.getName());
        model.setStatus(entity.getStatus());
        if (entity.getSubLeaderId() != null) {
            EmployeeModel employeeModel = new EmployeeModel();
            employeeModel.setId(entity.getSubLeaderId());
            Employee employee = employeeRepository.getById(entity.getSubLeaderId());
            employeeModel.setName(employee.getName());
            model.setSubLeader(employeeModel);
        }

        CenterOfExcellence centerOfExcellence = entity.getCenterOfExcellence();
        if (centerOfExcellence != null) {
            CenterOfExcellenceModel centerOfExcellenceModel = centerOfExcellenceTransformer.apply(centerOfExcellence);
            model.setCenterOfExcellence(centerOfExcellenceModel);
        }

        model.setCreated(entity.getCreated());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdated(entity.getUpdated());
        model.setUpdatedBy(entity.getUpdatedBy());
        return model;
    }
}
