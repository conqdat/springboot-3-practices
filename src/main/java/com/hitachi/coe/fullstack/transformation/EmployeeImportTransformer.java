package com.hitachi.coe.fullstack.transformation;

import com.hitachi.coe.fullstack.entity.EmployeeImport;
import com.hitachi.coe.fullstack.model.EmployeeImportModel;
import com.hitachi.coe.fullstack.transformation.base.AbstractCopyPropertiesTransformer;
import com.hitachi.coe.fullstack.transformation.base.EntityToModelTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmployeeImportTransformer extends AbstractCopyPropertiesTransformer<EmployeeImport, EmployeeImportModel>
        implements EntityToModelTransformer<EmployeeImport, EmployeeImportModel, Integer> {

    @Override
    public EmployeeImportModel apply(EmployeeImport entity) {
        EmployeeImportModel model = new EmployeeImportModel();
        if (entity == null) {
            log.debug("EmployeeImportTransformer: entity is null");
            return model;
        }
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setType(entity.getType());
        model.setStatus(entity.getStatus());
        model.setMessage(entity.getMessage());
        model.setCreated(entity.getCreated());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdated(entity.getUpdated());
        model.setUpdatedBy(entity.getUpdatedBy());
        return model;
    }
}
