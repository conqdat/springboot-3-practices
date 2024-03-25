package com.hitachi.coe.fullstack.transformation;

import com.hitachi.coe.fullstack.entity.Employee;
import com.hitachi.coe.fullstack.entity.EmployeeStatus;
import com.hitachi.coe.fullstack.model.EmployeeStatusModel;
import com.hitachi.coe.fullstack.transformation.base.AbstractCopyPropertiesTransformer;
import com.hitachi.coe.fullstack.transformation.base.ModelToEntityTransformer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class EmployeeStatusModelTransformer extends AbstractCopyPropertiesTransformer<EmployeeStatusModel, EmployeeStatus>
        implements ModelToEntityTransformer<EmployeeStatusModel, EmployeeStatus, Integer> {
    /**
     * Transformer array DTO models to array entities.
     *
     * @param models {@link List} of {@link EmployeeStatusModel}
     * @return {@link List} of {@link EmployeeStatus}
     */
    public List<EmployeeStatus> applyList(List<EmployeeStatusModel> models) {

        if (null == models || models.isEmpty()) {
            return Collections.emptyList();
        }
        return models.stream()
                .map(this::apply)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeStatus apply(EmployeeStatusModel model) {
        EmployeeStatus employeeStatus = new EmployeeStatus();
        Optional<EmployeeStatusModel> modelOptional = Optional.ofNullable(model);
        if (modelOptional.isEmpty()) {
            return employeeStatus;
        }
        if (modelOptional.get().getId() != null) {
            employeeStatus.setId(model.getId());
        }
        if (modelOptional.get().getEmployeeId() != null) {
            Employee employee = new Employee();
            employee.setId(modelOptional.get().getEmployeeId());
            employeeStatus.setEmployee(employee);
        }
        employeeStatus.setStatus(modelOptional.get().getStatus());
        employeeStatus.setStatusDate(modelOptional.get().getStatusDate());
        employeeStatus.setDescription(modelOptional.get().getDescription());
        return employeeStatus;
    }
}
