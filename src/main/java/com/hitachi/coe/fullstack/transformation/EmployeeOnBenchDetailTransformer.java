package com.hitachi.coe.fullstack.transformation;

import com.hitachi.coe.fullstack.entity.EmployeeOnBenchDetail;
import com.hitachi.coe.fullstack.model.EmployeeOnBenchDetailModel;
import com.hitachi.coe.fullstack.transformation.base.AbstractCopyPropertiesTransformer;
import com.hitachi.coe.fullstack.transformation.base.EntityToModelTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmployeeOnBenchDetailTransformer extends AbstractCopyPropertiesTransformer<EmployeeOnBenchDetail, EmployeeOnBenchDetailModel>
        implements EntityToModelTransformer<EmployeeOnBenchDetail, EmployeeOnBenchDetailModel, Long> {

    @Override
    public EmployeeOnBenchDetailModel apply(EmployeeOnBenchDetail entity) {
        EmployeeOnBenchDetailModel model = new EmployeeOnBenchDetailModel();
        if (entity == null) {
            log.debug("EmployeeOnBenchDetailTransformer: entity is null");
            return model;
        }
        model.setId(entity.getId());
        model.setEmployeeId(entity.getEmployee().getId());
        model.setEmployeeOnBenchId(entity.getEmployeeOnBench().getId());
        model.setCategoryCode(entity.getCategoryCode());
        model.setBenchDays(entity.getBenchDays());
        model.setDateOfJoin(entity.getDateOfJoin());
        model.setStatusChangeDate(entity.getStatusChangeDate());
        model.setCreated(entity.getCreated());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdated(entity.getUpdated());
        model.setUpdatedBy(entity.getUpdatedBy());
        return model;
    }
}
