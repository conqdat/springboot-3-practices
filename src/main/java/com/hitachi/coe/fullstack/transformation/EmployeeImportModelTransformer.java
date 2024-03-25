package com.hitachi.coe.fullstack.transformation;

import com.hitachi.coe.fullstack.entity.EmployeeImport;
import com.hitachi.coe.fullstack.model.EmployeeImportModel;
import com.hitachi.coe.fullstack.transformation.base.AbstractCopyPropertiesTransformer;
import com.hitachi.coe.fullstack.transformation.base.ModelToEntityTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmployeeImportModelTransformer extends AbstractCopyPropertiesTransformer<EmployeeImportModel, EmployeeImport>
        implements ModelToEntityTransformer<EmployeeImportModel, EmployeeImport, Integer> {

    @Override
    public EmployeeImport apply(EmployeeImportModel model) {
        EmployeeImport entity = new EmployeeImport();
        if (model == null) {
            log.debug("EmployeeImportModelTransformer: model is null");
            return entity;
        }
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setType(model.getType());
        entity.setStatus(model.getStatus());
        entity.setMessage(model.getMessage());
        entity.setCreated(model.getCreated());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdated(model.getUpdated());
        entity.setUpdatedBy(model.getUpdatedBy());
        return entity;
    }
}
