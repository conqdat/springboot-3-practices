package com.hitachi.coe.fullstack.transformation;

import com.hitachi.coe.fullstack.entity.EmployeeOnBench;
import com.hitachi.coe.fullstack.model.EmployeeOnBenchModel;
import com.hitachi.coe.fullstack.transformation.base.AbstractCopyPropertiesTransformer;
import com.hitachi.coe.fullstack.transformation.base.EntityToModelTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmployeeOnBenchTransformer extends AbstractCopyPropertiesTransformer<EmployeeOnBench, EmployeeOnBenchModel>
        implements EntityToModelTransformer<EmployeeOnBench, EmployeeOnBenchModel, Integer> {

    @Override
    public EmployeeOnBenchModel apply(EmployeeOnBench entity) {
        EmployeeOnBenchModel model = new EmployeeOnBenchModel();
        if (entity == null) {
            log.debug("EmployeeOnBenchTransformer: entity is null");
            return model;
        }
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setStartDate(entity.getStartDate());
        model.setEndDate(entity.getEndDate());
        model.setCreated(entity.getCreated());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdated(entity.getUpdated());
        model.setUpdatedBy(entity.getUpdatedBy());
        return model;
    }
}
