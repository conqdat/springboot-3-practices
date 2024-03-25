package com.hitachi.coe.fullstack.transformation;

import com.hitachi.coe.fullstack.entity.EmployeeOnBench;
import com.hitachi.coe.fullstack.model.EmployeeOnBenchModel;
import com.hitachi.coe.fullstack.transformation.base.AbstractCopyPropertiesTransformer;
import com.hitachi.coe.fullstack.transformation.base.ModelToEntityTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmployeeOnBenchModelTransformer extends AbstractCopyPropertiesTransformer<EmployeeOnBenchModel, EmployeeOnBench>
        implements ModelToEntityTransformer<EmployeeOnBenchModel, EmployeeOnBench, Integer> {

    @Override
    public EmployeeOnBench apply(EmployeeOnBenchModel model) {
        EmployeeOnBench entity = new EmployeeOnBench();
        if (model == null) {
            log.debug("EmployeeOnBenchModelTransformer: model is null");
            return entity;
        }
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setStartDate(model.getStartDate());
        entity.setEndDate(model.getEndDate());
        entity.setCreated(model.getCreated());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdated(model.getUpdated());
        entity.setUpdatedBy(model.getUpdatedBy());
        return entity;
    }
}
