package com.hitachi.coe.fullstack.service.impl;

import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.entity.CoeUtilization;
import com.hitachi.coe.fullstack.entity.Employee;
import com.hitachi.coe.fullstack.entity.EmployeeUtilization;
import com.hitachi.coe.fullstack.entity.Project;
import com.hitachi.coe.fullstack.exceptions.CoEException;
import com.hitachi.coe.fullstack.model.EmployeeUTModel;
import com.hitachi.coe.fullstack.model.EmployeeUtilizationFree;
import com.hitachi.coe.fullstack.model.EmployeeUtilizationModel;
import com.hitachi.coe.fullstack.model.EmployeeUtilizationModelResponse;
import com.hitachi.coe.fullstack.model.EmployeeUtilizationNoImport;
import com.hitachi.coe.fullstack.model.ExcelConfigModel;
import com.hitachi.coe.fullstack.model.ExcelErrorDetail;
import com.hitachi.coe.fullstack.model.ExcelResponseModel;
import com.hitachi.coe.fullstack.model.IEmployeeUTModel;
import com.hitachi.coe.fullstack.model.IEmployeeUtilizationDetail;
import com.hitachi.coe.fullstack.model.IEmployeeUtilizationDetailResponse;
import com.hitachi.coe.fullstack.model.IEmployeeUtilizationFree;
import com.hitachi.coe.fullstack.model.IEmployeeUtilizationModel;
import com.hitachi.coe.fullstack.model.AverageYearUTModel;
import com.hitachi.coe.fullstack.model.IEmployeeUtilizationNoImport;
import com.hitachi.coe.fullstack.model.IPieChartModel;
import com.hitachi.coe.fullstack.model.PieChart;
import com.hitachi.coe.fullstack.model.ImportResponse;
import com.hitachi.coe.fullstack.model.common.ErrorLineModel;
import com.hitachi.coe.fullstack.model.common.ErrorModel;
import com.hitachi.coe.fullstack.repository.CoeUtilizationRepository;
import com.hitachi.coe.fullstack.repository.EmployeeRepository;
import com.hitachi.coe.fullstack.repository.EmployeeUtilizationRepository;
import com.hitachi.coe.fullstack.repository.ProjectRepository;
import com.hitachi.coe.fullstack.service.CoeUtilizationService;
import com.hitachi.coe.fullstack.service.EmployeeUtilizationService;
import com.hitachi.coe.fullstack.transformation.EmployeeUtilizationTransformer;
import com.hitachi.coe.fullstack.util.CsvUtils;
import com.hitachi.coe.fullstack.util.DateFormatUtils;
import com.hitachi.coe.fullstack.util.ExcelUtils;
import com.hitachi.coe.fullstack.util.JsonUtils;
import com.hitachi.coe.fullstack.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class EmployeeUtilizationServiceImpl implements EmployeeUtilizationService {

    @Autowired
    private EmployeeUtilizationTransformer employeeUtilizationtransformer;

    @Autowired
    private EmployeeUtilizationRepository employeeUtilizationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CoeUtilizationRepository coeUtilizationRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CoeUtilizationService coeUtilizationService;

    @Override
    @Transactional(rollbackFor = {CoEException.class, Exception.class})
    public ImportResponse importEmployeeUtilization(ExcelResponseModel listOfEmployeeUT, String fileType, InputStream stream, String strDate) throws IOException {

        final ExcelConfigModel employeeUtilizationJsonConfigModel = JsonUtils.convertJsonToPojo(JsonUtils.readFileAsString("/jsonconfig/EmployeeUtilizationReadConfig.json"));
        final Integer row = employeeUtilizationJsonConfigModel.getCellRow();
        final Integer column = employeeUtilizationJsonConfigModel.getCellColumn();
        final String formatDate = employeeUtilizationJsonConfigModel.getCellInputFormat();
        final String sheetName = employeeUtilizationJsonConfigModel.getStyle().getSheetName();
        String duration;

        if (row == null || column == null || ObjectUtils.isEmpty(formatDate) || ObjectUtils.isEmpty(sheetName)) {
            throw new CoEException(ErrorConstant.CODE_READ_EXCEL_ERROR, ErrorConstant.MESSAGE_MISSING_ATTRIBUTE_JSON_CONFIG);
        }

        final ImportResponse importResponse = new ImportResponse();
        final List<EmployeeUtilization> employeeUtilizationList = new ArrayList<>();
        final HashMap<Integer, Object> dataList = listOfEmployeeUT.getData();
        final List<EmployeeUtilizationModel> employeeUtilizationModels = dataList.values().stream().map(EmployeeUtilizationModel.class::cast).collect(Collectors.toList());
        final List<ExcelErrorDetail> errorList = listOfEmployeeUT.getErrorDetails();
        int totalRows = listOfEmployeeUT.getTotalRows();

        if (fileType.equals("excel")) {
            try {
                duration = ExcelUtils.getSpecificCellStringValue(stream, row, column, sheetName);
            } catch (CoEException cex) {
                duration = "";
            }
            errorList.remove(errorList.size() - 1);
            totalRows--;
        } else {
            duration = CsvUtils.getSpecificCellStringValue(stream, row, column);
        }

        int sumOfBillableHours = employeeUtilizationModels.stream()
                .mapToInt(EmployeeUtilizationModel::getBillableHours)
                .sum();
        int sumOfAvailableHours = employeeUtilizationModels.stream()
                .mapToInt(EmployeeUtilizationModel::getAvailableHours)
                .sum();

        final Double totalUtilization = Math.round(((double) sumOfBillableHours / sumOfAvailableHours) * 1000.0) / 10.0;
        final CoeUtilization coeUtilization = coeUtilizationService.saveCoEUtilizationFromDuration(formatDate, duration.trim(), strDate, totalUtilization);
        final List<ErrorModel> errorModelList = ErrorModel.importErrorDetails(errorList);

        for (Map.Entry<Integer, Object> entry : dataList.entrySet()) {
            final EmployeeUtilizationModel employeeUtilizationModel = (EmployeeUtilizationModel) entry.getValue();
            final Employee employee = employeeRepository.findByHccId(String.valueOf(employeeUtilizationModel.getHccId()));
            final EmployeeUtilization employeeUtilization = employeeUtilizationtransformer.toEntity(employeeUtilizationModel,
                    employee, coeUtilization);
            final Project project = projectRepository.findByCode(employeeUtilizationModel.getOracleStaffedProject());
            final List<ErrorLineModel> errorLineModels = new ArrayList<>();

            if (ObjectUtils.isEmpty(employee)) {
                errorLineModels.add(new ErrorLineModel("ID", ErrorConstant.MESSAGE_HCC_ID_DO_NOT_EXIST));

            }

            if (ObjectUtils.isEmpty(project)) {
                errorLineModels.add(new ErrorLineModel("Oracle staffed Project", "Oracle staffed Project do not exist - " + employeeUtilizationModel.getOracleStaffedProject()));
            }

            if (!errorLineModels.isEmpty()) {
                errorModelList.add(new ErrorModel(entry.getKey(), errorLineModels));
            }
            employeeUtilizationList.add(employeeUtilization);
        }
        if (!errorModelList.isEmpty()) {
            ErrorModel.sortModelsByLine(errorModelList);
            importResponse.setTotalRows(totalRows);
            importResponse.setErrorRows(errorModelList.size());
            importResponse.setSuccessRows(0);
            importResponse.setErrorList(errorModelList);
            return importResponse;
        }

        coeUtilizationRepository.save(coeUtilization);
        employeeUtilizationRepository.saveAll(employeeUtilizationList);
        importResponse.setTotalRows(totalRows);
        importResponse.setErrorRows(errorList.size());
        importResponse.setSuccessRows(dataList.size());
        importResponse.setErrorList(errorModelList);
        return importResponse;
    }

    @Override
    public List<IPieChartModel> getUtilizationPieChart(Integer branchId, Integer coeId, Integer coeCoreTeamId,
                                                       String fromDateStr, String toDateStr) {
        if (coeId == null && (coeCoreTeamId != null)) {
            throw new CoEException(ErrorConstant.CODE_DATA_IS_EMPTY, ErrorConstant.MESSAGE_DATA_IS_EMPTY);

        }
        Timestamp fromDate = DateFormatUtils.convertTimestampFromString(fromDateStr);
        Timestamp toDate = DateFormatUtils.convertTimestampFromString(toDateStr);

        if (fromDate == null || toDate == null) {
            return employeeUtilizationRepository.getUtilizationPieChart(branchId, coeId, coeCoreTeamId, fromDate, toDate);
        }

        if ((Objects.requireNonNull(fromDate)).after(toDate)) {
            throw new CoEException(ErrorConstant.CODE_INVALID_START_DATE_END_DATE, ErrorConstant.MESSAGE_INVALID_START_DATE_END_DATE);
        }

        return employeeUtilizationRepository.getUtilizationPieChart(branchId, coeId, coeCoreTeamId, fromDate, toDate);

    }

    @Override
    public EmployeeUTModel searchEmployeeUtilization(String keyword, String billable, Integer branchId, Integer coeCoreTeamId,
                                                     Integer coeId, Integer coeUTId, String fromDateStr, String toDateStr, Integer no, Integer limit, String sortBy, Boolean desc) {
        if (coeId == null && coeCoreTeamId != null) {
            throw new CoEException(ErrorConstant.CODE_DATA_IS_EMPTY, ErrorConstant.MESSAGE_DATA_IS_EMPTY);
        }
        EmployeeUTModel employeeUTModel = new EmployeeUTModel();
        List<IEmployeeUTModel> employeeUtilizationList;
        String searchKw = null;
        Timestamp fromDate = DateFormatUtils.convertTimestampFromString(fromDateStr);
        Timestamp toDate = DateFormatUtils.convertTimestampFromString(toDateStr);

        if (keyword != null) {
            searchKw = StringUtil.removeUnknownSymbol(keyword);
        }

        if (fromDate != null && toDate != null && fromDate.after(toDate)) {
            throw new CoEException(ErrorConstant.CODE_INVALID_START_DATE_END_DATE, ErrorConstant.MESSAGE_INVALID_START_DATE_END_DATE);
        }

        if (coeUTId != null) {
            employeeUtilizationList = employeeUtilizationRepository.searchEmployeeUtilizationByUtilizationId(searchKw, branchId, coeCoreTeamId, coeId, coeUTId);
        } else {
            employeeUtilizationList = employeeUtilizationRepository.searchEmployeeUtilization(searchKw, branchId, coeCoreTeamId, coeId, fromDate, toDate);
        }

        if (employeeUtilizationList.isEmpty()) {
            employeeUTModel.setAvgBillable(0.0);
            employeeUTModel.setIEmployeeUTModels(new PageImpl<>(Collections.emptyList()));
            return employeeUTModel;
        }

        Sort sort = Sort.by(sortBy);

        if (desc != null) {
            sort = sort.descending();
        }

        final Pageable pageable = PageRequest.of(no, limit, sort);

        try {
            final double value = Double.parseDouble(billable);
            employeeUtilizationList = employeeUtilizationList.stream().filter(empUt ->
                    empUt.getBillable() >= value - 5 && empUt.getBillable() <= value + 10).collect(Collectors.toList());
        } catch (NumberFormatException | NullPointerException ignored) {
            // Code continues executing after the catch block
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + PageRequest.of(no, limit, sort).getPageSize()), employeeUtilizationList.size());
        double avgBillable = employeeUtilizationList.stream()
                .mapToDouble(IEmployeeUTModel::getBillable)
                .average()
                .orElse(0.0);
        employeeUTModel.setAvgBillable(Math.round((avgBillable * 10.0)) / 10.0);
        employeeUTModel.setIEmployeeUTModels(new PageImpl<>(employeeUtilizationList.subList(start, end), pageable, employeeUtilizationList.size()));
        return employeeUTModel;
    }

    @Override
    public EmployeeUtilizationModelResponse getEmployeeUtilizationDetailByHccId(String hccId) {

        List<IEmployeeUtilizationModel> iEmployeeUtilizationModels = employeeUtilizationRepository.getEmployeeUtilizationDetailByHccId(hccId);

        if (iEmployeeUtilizationModels.isEmpty()) {
            return new EmployeeUtilizationModelResponse();
        }

        EmployeeUtilizationModelResponse employeeUtilizationModelResponse = new EmployeeUtilizationModelResponse();

        double avgAvailableHours = iEmployeeUtilizationModels.stream()
                .mapToDouble(IEmployeeUtilizationModel::getAvailableHours)
                .average()
                .orElse(0.0);
        double avgBillableHours = iEmployeeUtilizationModels.stream()
                .mapToDouble(IEmployeeUtilizationModel::getBillableHours)
                .average()
                .orElse(0.0);
        double avgPtoOracle = iEmployeeUtilizationModels.stream()
                .mapToDouble(IEmployeeUtilizationModel::getPtoOracle)
                .average()
                .orElse(0.0);
        double avgLoggedHours = iEmployeeUtilizationModels.stream()
                .mapToDouble(IEmployeeUtilizationModel::getLoggedHours)
                .average()
                .orElse(0.0);
        double avgBillable = iEmployeeUtilizationModels.stream()
                .mapToDouble(IEmployeeUtilizationModel::getBillable)
                .average()
                .orElse(0.0);

        employeeUtilizationModelResponse.setAvgBillable(Math.round((avgBillable * 10.0)) / 10.0);
        employeeUtilizationModelResponse.setAvgBillableHours(Math.round((avgBillableHours * 10.0)) / 10.0);
        employeeUtilizationModelResponse.setAvgAvailableHours(Math.round((avgAvailableHours * 10.0)) / 10.0);
        employeeUtilizationModelResponse.setAvgLoggedHours(Math.round((avgLoggedHours * 10.0)) / 10.0);
        employeeUtilizationModelResponse.setAvgPtoOracle(Math.round((avgPtoOracle * 10.0)) / 10.0);
        employeeUtilizationModelResponse.setEmployeeUtilizationModels(iEmployeeUtilizationModels);

        return employeeUtilizationModelResponse;
    }

    @Override
    public IEmployeeUtilizationDetailResponse getProjectInformationByEmployeeUtilizationId(Integer empUtId) {

        IEmployeeUtilizationDetailResponse result = employeeUtilizationRepository.getProjectInformationByEmployeeUtilizationId(empUtId);

        if (ObjectUtils.isEmpty(result))
            throw new CoEException(ErrorConstant.CODE_EMPLOYEE_UTILIZATION_ID_NOT_FOUND, ErrorConstant.MESSAGE_EMPLOYEE_UTILIZATION_ID_NOT_FOUND);

        return result;
    }


    @Override
    public EmployeeUtilizationFree getListEmployeeUtilizationWithNoUT(Double billableThreshold, String fromDateStr, String toDateStr,
                                                                      Integer no, Integer limit, String sortBy, Boolean desc) {
        if (billableThreshold == null) {
            throw new CoEException(ErrorConstant.CODE_BILLABLE_IS_NULL, ErrorConstant.MESSAGE_BILLABLE_IS_NULL);
        }
        final Timestamp fromDate = DateFormatUtils.convertTimestampFromString(fromDateStr);
        final Timestamp toDate = DateFormatUtils.convertTimestampFromString(toDateStr);
        final Sort sort = Sort.by(
                Boolean.TRUE.equals(desc)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                sortBy);

        final Pageable pageable = PageRequest.of(no, limit, sort);
        if (fromDate != null && toDate != null && fromDate.after(toDate)) {
            throw new CoEException(ErrorConstant.CODE_INVALID_START_DATE_END_DATE, ErrorConstant.MESSAGE_INVALID_START_DATE_END_DATE);
        }
        final Page<IEmployeeUtilizationFree> result = employeeUtilizationRepository.getEmployeesUtilizationNoUT(billableThreshold, fromDate, toDate, pageable);
        final EmployeeUtilizationFree employeeUtilizationFree = new EmployeeUtilizationFree();
        employeeUtilizationFree.setBillable(billableThreshold);
        employeeUtilizationFree.setIEmployeeUtilizationFreePage(result);
        return employeeUtilizationFree;
    }

    @Override
    public IEmployeeUtilizationDetail getEmployeeUtilizationDetailByEmployeeUtilizationId(Integer id) {

        IEmployeeUtilizationDetail result = employeeUtilizationRepository.getEmployeeUtilizationDetailById(id);

        if (ObjectUtils.isEmpty(result))
            throw new CoEException(ErrorConstant.CODE_EMPLOYEE_UTILIZATION_ID_NOT_FOUND, ErrorConstant.MESSAGE_EMPLOYEE_UTILIZATION_ID_NOT_FOUND);

        return result;
    }
    
    @Override
    public List<EmployeeUtilizationNoImport> getListEmployeeUtilizationWithNoImport(String fromDateStr, String toDateStr) {
        if (fromDateStr == null || toDateStr == null) {
            // Handle null case appropriately, such as returning an empty list
            throw new CoEException(
                    ErrorConstant.CODE_FROM_DATE_OR_TO_DATE_NULL,
                    ErrorConstant.MESSAGE_FROM_DATE_OR_TO_DATE_NULL);
        }
        Timestamp fromDate = DateFormatUtils.convertTimestampFromString(fromDateStr);
        Timestamp toDate = DateFormatUtils.convertTimestampFromString(toDateStr);

        Integer year = fromDate.toLocalDateTime().getYear();
        List<Integer> months = DateFormatUtils.getMonthRange(fromDate, toDate);
        List<EmployeeUtilizationNoImport> employeeUtilizationNoImportList = new ArrayList<>();
        for (Integer month : months) {
            List<IEmployeeUtilizationNoImport> employeeUtilizationNoImports = employeeUtilizationRepository
                    .getEmployeeUtilizationNoImport(month, year);
            for (IEmployeeUtilizationNoImport employeeUtilization : employeeUtilizationNoImports) {
                List<Integer> monthNoImport = new ArrayList<>();
                monthNoImport.add(month);
                boolean existingEmployee = false;
                for (EmployeeUtilizationNoImport existingEmployeeUtilization : employeeUtilizationNoImportList) {
                    if (Objects.equals(existingEmployeeUtilization.getEmployeeId(),
                            employeeUtilization.getEmployeeId())) {
                        existingEmployeeUtilization.getMonthNoImport().add(month);
                        existingEmployee = true;
                        break;
                    }
                }
                if (!existingEmployee) {
                    EmployeeUtilizationNoImport convertedEmployeeUtilization = new EmployeeUtilizationNoImport(
                            employeeUtilization.getHccId(), employeeUtilization.getEmployeeId(),
                            employeeUtilization.getEmployeeName(), employeeUtilization.getEmail(),
                            employeeUtilization.getBranchName(), employeeUtilization.getBusinessName(),
                            employeeUtilization.getTeamName(), monthNoImport);
                    employeeUtilizationNoImportList.add(convertedEmployeeUtilization);
                }
            }
        }

        return employeeUtilizationNoImportList;
    }
    @Override
    public List<AverageYearUTModel> getListAverageMonthByUtilization(List<Integer> years, Integer branchId,
                                                                  Integer coeId,Integer coeCoreTeamId) {


        List<AverageYearUTModel> averageYearUTModelList = new ArrayList<>();
        for (Integer year: years) {
            AverageYearUTModel tempAverageYearUTModel = new AverageYearUTModel();
            List<IPieChartModel> listPieChart = new ArrayList<>();
            for (Month month :  Month.values()) {
                IPieChartModel pieChart =  employeeUtilizationRepository
                        .getAverageMonthUtilization(month.getValue(), year,branchId,coeId,coeCoreTeamId);
                pieChart = new PieChart( Month.of(month.getValue()).name(), pieChart == null ? 0.0 : pieChart.getData());
                listPieChart.add(pieChart);
            }
            tempAverageYearUTModel.setYear(year);
            tempAverageYearUTModel.setPieChartModelList(listPieChart);

            averageYearUTModelList.add(tempAverageYearUTModel);
        }
        return averageYearUTModelList;
    }
}