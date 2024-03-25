package com.hitachi.coe.fullstack.transformation;

import com.hitachi.coe.fullstack.entity.EmployeeImportDetail;
import com.hitachi.coe.fullstack.model.EmployeeImportDetailModel;
import com.hitachi.coe.fullstack.transformation.base.AbstractCopyPropertiesTransformer;
import com.hitachi.coe.fullstack.transformation.base.EntityToModelTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmployeeImportDetailTransformer extends AbstractCopyPropertiesTransformer<EmployeeImportDetail, EmployeeImportDetailModel>
        implements EntityToModelTransformer<EmployeeImportDetail, EmployeeImportDetailModel, Long> {

    @Override
    public EmployeeImportDetailModel apply(EmployeeImportDetail entity) {
        EmployeeImportDetailModel model = new EmployeeImportDetailModel();
        if (entity == null) {
            log.debug("EmployeeImportDetailTransformer: entity is null");
            return model;
        }
        model.setId(entity.getId());
        model.setBody(entity.getBody());
        model.setStatus(entity.getStatus());
        model.setLineNum(entity.getLineNum());
        model.setMesssageLineList(entity.getMessageLineList());
        model.setEmployeeImportId(entity.getEmployeeImport().getId());
        // don't do this, it'll cause stackoverflow due to loop, only 1 way transform flow
        // model.setEmployeeImportModel(new EmployeeImportTransformer().apply(entity.getEmployeeImport()));
        model.setCreated(entity.getCreated());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdated(entity.getUpdated());
        model.setUpdatedBy(entity.getUpdatedBy());
        return model;
    }
}
