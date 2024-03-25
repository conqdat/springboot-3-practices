package com.hitachi.coe.fullstack.transformation;

import com.hitachi.coe.fullstack.entity.Employee;
import com.hitachi.coe.fullstack.entity.EmployeeOnBench;
import com.hitachi.coe.fullstack.entity.EmployeeOnBenchDetail;
import com.hitachi.coe.fullstack.model.EmployeeOnBenchDetailModel;
import com.hitachi.coe.fullstack.transformation.base.AbstractCopyPropertiesTransformer;
import com.hitachi.coe.fullstack.transformation.base.ModelToEntityTransformer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeeOnBenchDetailModelTransformer extends AbstractCopyPropertiesTransformer<EmployeeOnBenchDetailModel, EmployeeOnBenchDetail>
        implements ModelToEntityTransformer<EmployeeOnBenchDetailModel, EmployeeOnBenchDetail, Long> {

    private final EmployeeOnBenchModelTransformer employeeOnBenchModelTransformer;

    @Override
    public EmployeeOnBenchDetail apply(EmployeeOnBenchDetailModel model) {
        EmployeeOnBenchDetail entity = new EmployeeOnBenchDetail();
        if (model == null) {
            log.debug("EmployeeOnBenchDetailModelTransformer: model is null");
            return entity;
        }
        entity.setId(model.getId());
        if (model.getEmployeeId() != null) {
            Employee employee = new Employee();
            employee.setId(model.getEmployeeId());
            entity.setEmployee(employee);
        }
        if (model.getEmployeeOnBenchId() != null) {
            EmployeeOnBench employeeOnBench = new EmployeeOnBench();
            employeeOnBench.setId(model.getEmployeeOnBenchId());
            entity.setEmployeeOnBench(employeeOnBench);
        }
        if (model.getEmployeeOnBenchModel() != null) {
            EmployeeOnBench employeeOnBench = employeeOnBenchModelTransformer.apply(model.getEmployeeOnBenchModel());
            entity.setEmployeeOnBench(employeeOnBench);
        }
        entity.setBenchDays(model.getBenchDays());
        entity.setCategoryCode(model.getCategoryCode());
        entity.setDateOfJoin(model.getDateOfJoin());
        entity.setStatusChangeDate(model.getStatusChangeDate());
        entity.setCreated(model.getCreated());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdated(model.getUpdated());
        entity.setUpdatedBy(model.getUpdatedBy());
        return entity;
    }
}
