package com.hitachi.coe.fullstack.transformation;

import com.hitachi.coe.fullstack.entity.BusinessUnit;
import com.hitachi.coe.fullstack.model.BusinessUnitModel;
import com.hitachi.coe.fullstack.transformation.base.AbstractCopyPropertiesTransformer;
import com.hitachi.coe.fullstack.transformation.base.EntityToModelTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The class BusinessUnitTransformer is convert entity to DTO.
 *
 * @author lphanhoangle
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessUnitTransformer extends AbstractCopyPropertiesTransformer<BusinessUnit, BusinessUnitModel>
        implements EntityToModelTransformer<BusinessUnit, BusinessUnitModel, Integer> {

    /**
     * Transformer array entities to array DTO.
     *
     * @param entities {@link List} of {@link BusinessUnit}
     * @return {@link List} of {@link BusinessUnitModel}
     */
    public List<BusinessUnitModel> applyList(List<BusinessUnit> entities) {
        if (null == entities || entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities.stream().map(this::apply)
                .collect(Collectors.toList());
    }

    @Override
    public BusinessUnitModel apply(BusinessUnit entity) {
        if (Objects.isNull(entity)) {
            log.debug("BusinessUnitTransformer::apply -> BusinessUnit is null");
            return null;
        }
        BusinessUnitModel model = new BusinessUnitModel();
        model.setId(entity.getId());
        model.setCode(entity.getCode());
        model.setName(entity.getName());
        model.setManager(entity.getManager());
        model.setDescription(entity.getDescription());
        model.setCreated(entity.getCreated());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdated(entity.getUpdated());
        model.setUpdatedBy(entity.getUpdatedBy());
        return model;
    }

}
