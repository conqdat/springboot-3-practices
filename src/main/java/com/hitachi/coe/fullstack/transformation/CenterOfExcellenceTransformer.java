package com.hitachi.coe.fullstack.transformation;

import com.hitachi.coe.fullstack.entity.CenterOfExcellence;
import com.hitachi.coe.fullstack.model.CenterOfExcellenceModel;
import com.hitachi.coe.fullstack.transformation.base.AbstractCopyPropertiesTransformer;
import com.hitachi.coe.fullstack.transformation.base.EntityToModelTransformer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This CenterOfExcellenceTransformer is transform entity to DTO.
 *
 * @author lphanhoangle
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CenterOfExcellenceTransformer
        extends AbstractCopyPropertiesTransformer<CenterOfExcellence, CenterOfExcellenceModel>
        implements EntityToModelTransformer<CenterOfExcellence, CenterOfExcellenceModel, Integer> {

    /**
     * Transformer array entities to array DTO.
     *
     * @param entities {@link List} of {@link CenterOfExcellence}
     * @return {@link List} of {@link CenterOfExcellenceModel}
     */
    public List<CenterOfExcellenceModel> applyList(List<CenterOfExcellence> entities) {
        if (null == entities || entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities.stream().map(this::apply)
                .collect(Collectors.toList());
    }

    @Override
    public CenterOfExcellenceModel apply(CenterOfExcellence entity) {
        if (entity == null) {
            log.debug("CenterOfExcellenceTransformer::apply -> CenterOfExcellence is null");
            return null;
        }
        CenterOfExcellenceModel centerOfExcellence = new CenterOfExcellenceModel();
        centerOfExcellence.setId(entity.getId());
        centerOfExcellence.setCode(entity.getCode());
        centerOfExcellence.setName(entity.getName());
        centerOfExcellence.setCreated(entity.getCreated());
        centerOfExcellence.setCreatedBy(entity.getCreatedBy());
        centerOfExcellence.setUpdated(entity.getUpdated());
        centerOfExcellence.setUpdatedBy(entity.getUpdatedBy());
        // don't do this in coeCoreTeamTransformer::apply it call centerOfExcellenceTransformer.apply(centerOfExcellence)
        // it will cause infinite loop
        // if (entity.getCoeCoreTeams() != null) {
        //     // List<CoeCoreTeamModel> coeCoreTeams = entity.getCoeCoreTeams().stream()
        //     //         .map(coeCoreTeamTransformer::apply)
        //     //         .collect(Collectors.toList());
        //     // centerOfExcellence.setCoeCoreTeamModelList(coeCoreTeams);
        // }
        return centerOfExcellence;
    }
}
