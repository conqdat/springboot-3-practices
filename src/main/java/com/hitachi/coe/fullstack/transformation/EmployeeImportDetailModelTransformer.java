package com.hitachi.coe.fullstack.transformation;

import com.hitachi.coe.fullstack.entity.EmployeeImport;
import com.hitachi.coe.fullstack.entity.EmployeeImportDetail;
import com.hitachi.coe.fullstack.model.EmployeeImportDetailModel;
import com.hitachi.coe.fullstack.transformation.base.AbstractCopyPropertiesTransformer;
import com.hitachi.coe.fullstack.transformation.base.ModelToEntityTransformer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeeImportDetailModelTransformer extends AbstractCopyPropertiesTransformer<EmployeeImportDetailModel, EmployeeImportDetail>
        implements ModelToEntityTransformer<EmployeeImportDetailModel, EmployeeImportDetail, Long> {

    private final EmployeeImportModelTransformer employeeImportModelTransformer;

    @Override
    public EmployeeImportDetail apply(EmployeeImportDetailModel model) {
        EmployeeImportDetail entity = new EmployeeImportDetail();
        if (model == null) {
            log.debug("EmployeeImportDetailModelTransformer: model is null");
            return entity;
        }
        entity.setId(model.getId());
        entity.setBody(model.getBody());
        entity.setStatus(model.getStatus());
        entity.setLineNum(model.getLineNum());
        entity.setMessageLineList(model.getMesssageLineList());
        if (model.getEmployeeImportId() != null) {
            EmployeeImport employeeImport = new EmployeeImport();
            employeeImport.setId(model.getEmployeeImportId());
            entity.setEmployeeImport(employeeImport);
        }
        if (model.getEmployeeImportModel() != null) {
            EmployeeImport employeeImport = employeeImportModelTransformer.apply(model.getEmployeeImportModel());
            entity.setEmployeeImport(employeeImport);
        }
        entity.setCreated(model.getCreated());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdated(model.getUpdated());
        entity.setUpdatedBy(model.getUpdatedBy());
        return entity;
    }
}
