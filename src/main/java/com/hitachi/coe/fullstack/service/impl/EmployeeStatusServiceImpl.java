package com.hitachi.coe.fullstack.service.impl;

import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.constant.StatusConstant;
import com.hitachi.coe.fullstack.entity.Employee;
import com.hitachi.coe.fullstack.entity.EmployeeStatus;
import com.hitachi.coe.fullstack.model.EmployeeLeaveModel;
import com.hitachi.coe.fullstack.model.EmployeeStatusModel;
import com.hitachi.coe.fullstack.model.ExcelErrorDetail;
import com.hitachi.coe.fullstack.model.ExcelResponseModel;
import com.hitachi.coe.fullstack.model.ImportResponse;
import com.hitachi.coe.fullstack.model.common.ErrorLineModel;
import com.hitachi.coe.fullstack.model.common.ErrorModel;
import com.hitachi.coe.fullstack.repository.EmployeeRepository;
import com.hitachi.coe.fullstack.repository.EmployeeStatusRepository;
import com.hitachi.coe.fullstack.service.EmployeeStatusService;
import com.hitachi.coe.fullstack.transformation.EmployeeStatusModelTransformer;
import com.hitachi.coe.fullstack.transformation.EmployeeStatusTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeStatusServiceImpl implements EmployeeStatusService {

    private final EmployeeStatusRepository employeeStatusRepository;
    private final EmployeeStatusTransformer employeeStatusTransformer;
    private final EmployeeStatusModelTransformer employeeStatusModelTransformer;
    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeStatusModel deleteEmployeeById(Integer id) {
        EmployeeStatus employeeStatus = employeeStatusRepository.createDeleteStatusEmployee(id);
        return employeeStatusTransformer.apply(employeeStatus);
    }

    @Override
    public ImportResponse importLeaveEmployee(ExcelResponseModel listOfEmployee) {
        final ImportResponse importResponse = new ImportResponse();
        final HashMap<Integer, Object> dataList = listOfEmployee.getData();
        final List<ExcelErrorDetail> errorList = listOfEmployee.getErrorDetails();
        final List<ErrorModel> errorModelList = ErrorModel.importErrorDetails(errorList);
        int totalSuccess = 0;

        for (final Map.Entry<Integer, Object> entry : dataList.entrySet()) {
            final Integer key = entry.getKey();
            final Object value = entry.getValue();
            final EmployeeLeaveModel em = (EmployeeLeaveModel) value;
            final Employee employee = employeeRepository.findByLdap(em.getLdap()).orElse(null);
            if (employee == null) {
                errorModelList.add(new ErrorModel(
                        key,
                        ErrorLineModel.createErrorDetails("LDAP", ErrorConstant.MESSAGE_EMPLOYEE_DO_NOT_EXIST)));
                continue;
            }
            try {
                final boolean isAlreadyExist = employeeStatusRepository
                        .existsByEmployeeIdAndStatusDate(employee.getId(), em.getLeaveDate());
                if (isAlreadyExist) {
                    errorModelList.add(new ErrorModel(
                            key,
                            ErrorLineModel.createErrorDetails("Leave Date", ErrorConstant.MESSAGE_LEAVE_DATE_IS_ALREADY_EXISTS)));
                    continue;
                }
                final Optional<EmployeeStatus> lastStatusOptional = employeeStatusRepository
                        .findFirstByEmployeeIdOrderByStatusDateDesc(employee.getId());
                final boolean isLastestStatusAlreadyDeleted = lastStatusOptional
                        .filter(s -> s.getStatus() == StatusConstant.STATUS_DELETED)
                        .isPresent();
                if (isLastestStatusAlreadyDeleted) {
                    // The last status is already 0 (leaved/deleted/deactivated), skip inserting the new status
                    errorModelList.add(new ErrorModel(
                            key,
                            ErrorLineModel.createErrorDetails("LDAP", ErrorConstant.MESSAGE_EMPLOYEE_ALREADY_LEAVED)));
                    continue;
                }
                final EmployeeStatusModel employeeStatusModel = new EmployeeStatusModel();
                employeeStatusModel.setEmployeeId(employee.getId());
                employeeStatusModel.setStatusDate(em.getLeaveDate());
                employeeStatusModel.setDescription(em.getReason());
                employeeStatusModel.setStatus(StatusConstant.STATUS_DELETED);
                employeeStatusRepository.save(employeeStatusModelTransformer.apply(employeeStatusModel));
            } catch (Exception e) {
                errorModelList.add(new ErrorModel(
                        key,
                        ErrorLineModel.createErrorDetails("LDAP", ErrorConstant.MESSAGE_ERROR_WHEN_SAVE)));
            }
            totalSuccess++;
        }
        ErrorModel.sortModelsByLine(errorModelList);
        importResponse.setTotalRows(listOfEmployee.getTotalRows());
        importResponse.setErrorRows(errorModelList.size());
        importResponse.setSuccessRows(totalSuccess);
        importResponse.setErrorList(errorModelList);
        return importResponse;
    }

}
