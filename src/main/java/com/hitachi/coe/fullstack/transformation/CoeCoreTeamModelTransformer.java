package com.hitachi.coe.fullstack.transformation;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.hitachi.coe.fullstack.entity.CenterOfExcellence;
import org.springframework.stereotype.Component;

import com.hitachi.coe.fullstack.entity.CoeCoreTeam;
import com.hitachi.coe.fullstack.model.CoeCoreTeamModel;
import com.hitachi.coe.fullstack.transformation.base.AbstractCopyPropertiesTransformer;
import com.hitachi.coe.fullstack.transformation.base.ModelToEntityTransformer;

/**
 * This CoeCoreTeamTransformer is transform entity to DTO.
 *
 * @author lphanhoangle
 */
@Component
public class CoeCoreTeamModelTransformer extends AbstractCopyPropertiesTransformer<CoeCoreTeamModel, CoeCoreTeam>
        implements ModelToEntityTransformer<CoeCoreTeamModel, CoeCoreTeam, Integer> {

    /**
     * Transformer array entities to array DTO.
     *
     * @param entities {@link List} of {@link CoeCoreTeam}
     * @return {@link List} of {@link CoeCoreTeamModel}
     */
    public List<CoeCoreTeam> applyList(List<CoeCoreTeamModel> entities) {
        if (null == entities || entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities.stream().map(this)
                .collect(Collectors.toList());
    }

    @Override
    public CoeCoreTeam apply(CoeCoreTeamModel coeModel) {
        CoeCoreTeam team = super.apply(coeModel);
        if(coeModel.getCenterOfExcellence() != null) {
            CenterOfExcellence coe = new CenterOfExcellence();
            coe.setId(coeModel.getCenterOfExcellence().getId());
            team.setCenterOfExcellence(coe);
        }
        if(coeModel.getSubLeader() != null) {
            team.setSubLeaderId(coeModel.getSubLeader().getId());
        }
        return team;
    }
}
