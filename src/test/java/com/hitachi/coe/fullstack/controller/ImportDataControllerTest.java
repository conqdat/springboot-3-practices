package com.hitachi.coe.fullstack.controller;

import com.hitachi.coe.fullstack.exceptions.CoEException;
import com.hitachi.coe.fullstack.model.ExcelResponseModel;
import com.hitachi.coe.fullstack.model.ImportResponse;
import com.hitachi.coe.fullstack.model.common.BaseResponse;
import com.hitachi.coe.fullstack.model.common.ErrorLineModel;
import com.hitachi.coe.fullstack.model.common.ErrorModel;
import com.hitachi.coe.fullstack.service.EmployeeService;
import com.hitachi.coe.fullstack.service.EmployeeStatusService;
import com.hitachi.coe.fullstack.service.EmployeeUtilizationService;
import com.hitachi.coe.fullstack.service.ProjectService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-data-test.properties")
public class ImportDataControllerTest {

    @InjectMocks
    private ImportDataController importDataController;

    @MockBean
    private EmployeeUtilizationService employeeUtilizationService;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private EmployeeStatusService employeeStatusService;

    @Test
    @SneakyThrows
    public void testImportDataExcel_whenInvalidFileType_thenBadRequest(){

        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/files/SurveyDataTest.xlsx"));
        MultipartFile file = new MockMultipartFile("SurveyDataTest.xlsx", "SurveyDataTest.xlsx", contentType, content);
        BaseResponse<ImportResponse> response = importDataController.importData(file, "survey", null);
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        assertEquals("Not valid excel file.", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    @SneakyThrows
    public void testImportDataExcel_whenInvalidFile_thenBadRequest(){
        String contentType = "text/plain";
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/files/TestFileType.txt"));
        MultipartFile file = new MockMultipartFile("TestFileType.txt", "TestFileType.txt", contentType, content);
        BaseResponse<ImportResponse> response = importDataController.importData(file, "survey_data", null);
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        assertEquals("Please select excel or csv file.", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    @SneakyThrows
    public void testImportDataExcel_whenValidUtilizationFile_thenBadRequest(){
        ImportResponse importResponse = new ImportResponse();
        ErrorLineModel errorBillableHours = new ErrorLineModel("Billable hours", "Invalid cell value, please check if you violate any type config or validation");
        ErrorModel errorModel = new ErrorModel(5, List.of(errorBillableHours));

        importResponse.setTotalRows(2);
        importResponse.setErrorRows(1);
        importResponse.setSuccessRows(0);
        importResponse.setErrorList(List.of(errorModel));
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/files/UT tracking_updated_Test_2.xlsx"));
        MultipartFile file = new MockMultipartFile("UT tracking_updated_Test_2.xlsx", "UT tracking_updated_Test_2.xlsx", contentType, content);
        when(employeeUtilizationService.importEmployeeUtilization(any(ExcelResponseModel.class), any(String.class), any(InputStream.class), any(String.class))).thenReturn(importResponse);
        BaseResponse<ImportResponse> response = importDataController.importData(file, "utilization", "01 Aug 2023");
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        assertNull(response.getMessage());
        assertEquals(importResponse, response.getData());

    }

    @Test
    @SneakyThrows
    public void testImportDataExcel_whenEmployeeFile_thenSuccess(){
        ImportResponse importResponse = new ImportResponse();

        importResponse.setTotalRows(1);
        importResponse.setErrorRows(0);
        importResponse.setSuccessRows(1);
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/files/employeeAPITest.xlsx"));
        MultipartFile file = new MockMultipartFile("employeeAPITest.xlsx", "employeeAPITest.xlsx", contentType, content);
        when(employeeService.importUpdateEmployee(any(ExcelResponseModel.class))).thenReturn(importResponse);
        BaseResponse<ImportResponse> response = importDataController.importData(file, "employee", null);
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNull(response.getMessage());
        assertEquals(importResponse, response.getData());

    }
    @Test
    @SneakyThrows
    public void testImportDataCsv_whenInvalidFileType_thenBadRequest(){

        String contentType = "text/csv";
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/files/SurveyDataTest_csv.csv"));
        MultipartFile file = new MockMultipartFile("SurveyDataTest_csv.csv", "SurveyDataTest_csv.csv", contentType, content);
        BaseResponse<ImportResponse> response = importDataController.importData(file, "survey", null);
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        assertEquals("Not valid csv file.", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    @SneakyThrows
    public void testImportDataCsv_whenValidUtilizationFile_thenBadRequest(){
        ImportResponse importResponse = new ImportResponse();
        ErrorLineModel errorBillableHours = new ErrorLineModel("Billable hours", "Invalid cell value, please check if you violate any type config or validation");
        ErrorModel errorModel = new ErrorModel(5, List.of(errorBillableHours));

        importResponse.setTotalRows(2);
        importResponse.setErrorRows(1);
        importResponse.setSuccessRows(0);
        importResponse.setErrorList(List.of(errorModel));
        String contentType = "text/csv";
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/files/UT tracking_updated_Test_2_csv.csv"));
        MultipartFile file = new MockMultipartFile("UT tracking_updated_Test_2_csv.csv", "UT tracking_updated_Test_2_csv.csv", contentType, content);
        when(employeeUtilizationService.importEmployeeUtilization(any(ExcelResponseModel.class), any(String.class), any(InputStream.class), any(String.class))).thenReturn(importResponse);
        BaseResponse<ImportResponse> response = importDataController.importData(file, "utilization", "01 Aug 2023");
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        assertNull(response.getMessage());
        assertEquals(importResponse, response.getData());

    }

    @Test
    @SneakyThrows
    public void testImportDataCsv_whenInvalidEmployeeFile_thenThrowCoEException(){
        ImportResponse importResponse = new ImportResponse();

        importResponse.setTotalRows(1);
        importResponse.setErrorRows(0);
        importResponse.setSuccessRows(1);
        String contentType = "text/csv";
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/files/employeeAPITest_csv.csv"));
        MultipartFile file = new MockMultipartFile("employeeAPITest_csv.csv", "employeeAPITest_csv.csv", contentType, content);
        when(employeeService.importUpdateEmployee(any(ExcelResponseModel.class))).thenReturn(importResponse);

        Throwable throwable = assertThrows(CoEException.class,
                () -> importDataController.importData(file, "employee", null));

        //Verify
        assertEquals(CoEException.class, throwable.getClass());
        assertEquals("Wrong file or config is missing", throwable.getMessage());

    }

    @Test
    @SneakyThrows
    public void testImportExcel_whenValidProjectFile_thenSuccess(){

        ImportResponse importResponse = new ImportResponse();

        importResponse.setTotalRows(1);
        importResponse.setErrorRows(0);
        importResponse.setSuccessRows(1);
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/files/ProjectTest.xlsx"));
        MultipartFile file = new MockMultipartFile("ProjectTest.xlsx", "ProjectTest.xlsx", contentType, content);
        when(projectService.importProject(any(ExcelResponseModel.class))).thenReturn(importResponse);
        BaseResponse<ImportResponse> response = importDataController.importData(file, "project",null);
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNull(response.getMessage());
        assertEquals(importResponse, response.getData());
    }

    @Test
    @SneakyThrows
    public void testImportCSV_whenValidProjectFile_thenSuccess(){

        ImportResponse importResponse = new ImportResponse();

        importResponse.setTotalRows(1);
        importResponse.setErrorRows(0);
        importResponse.setSuccessRows(1);
        String contentType = "text/csv";
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/files/ProjectTest.csv"));
        MultipartFile file = new MockMultipartFile("ProjectTest.csv", "ProjectTest.csv", contentType, content);
        when(projectService.importProject(any(ExcelResponseModel.class))).thenReturn(importResponse);

        BaseResponse<ImportResponse> response = importDataController.importData(file, "project",null);
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNull(response.getMessage());
        assertEquals(importResponse, response.getData());
    }

    @Test
    @SneakyThrows
    public void testImportExcel_whenValidEmployeeInsertFile_thenSuccess(){

        ImportResponse importResponse = new ImportResponse();

        importResponse.setTotalRows(1);
        importResponse.setErrorRows(0);
        importResponse.setSuccessRows(1);
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/files/EmployeeInsertTest.xlsx"));
        MultipartFile file = new MockMultipartFile("EmployeeInsertTest.xlsx", "EmployeeInsertTest.xlsx", contentType, content);
        when(employeeService.importInsertEmployee(any(ExcelResponseModel.class))).thenReturn(importResponse);

        BaseResponse<ImportResponse> response = importDataController.importData(file, "import-employee",null);
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNull(response.getMessage());
        assertEquals(importResponse, response.getData());
    }

    @Test
    @SneakyThrows
    public void testImportCSV_whenValidEmployeeInsertFile_thenSuccess(){

        ImportResponse importResponse = new ImportResponse();

        importResponse.setTotalRows(1);
        importResponse.setErrorRows(0);
        importResponse.setSuccessRows(1);
        String contentType = "text/csv";
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/files/EmployeeInsertCSVTest.csv"));
        MultipartFile file = new MockMultipartFile("EmployeeInsertCSVTest.csv", "EmployeeInsertCSVTest.csv", contentType, content);
        when(employeeService.importInsertEmployee(any(ExcelResponseModel.class))).thenReturn(importResponse);

        BaseResponse<ImportResponse> response = importDataController.importData(file, "import-employee",null);
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNull(response.getMessage());
        assertEquals(importResponse, response.getData());
    }

    @Test
    @SneakyThrows
    public void testImportLeaveEmployee_WhenValidEmployeeLeaveInsertFileExcel_thenSuccess() {
        ImportResponse importResponse = new ImportResponse();
        importResponse.setTotalRows(3);
        importResponse.setSuccessRows(3);
        importResponse.setErrorRows(0);
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/files/EmployeeLeaveExelFile.xlsx"));
        MultipartFile file = new MockMultipartFile("EmployeeLeaveExelFile.xlsx", "EmployeeLeaveExelFile.xlsx", contentType, content);
        when(employeeStatusService.importLeaveEmployee(any(ExcelResponseModel.class))).thenReturn(importResponse);
        BaseResponse<ImportResponse> response = importDataController.importData(file, "import-leave-employee",null);
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNull(response.getMessage());
        assertEquals(importResponse, response.getData());
    }

    @Test
    @SneakyThrows
    public void testImportLeaveEmployee_WhenValidEmployeeLeaveInsertFileCsv_thenSuccess() {
        ImportResponse importResponse = new ImportResponse();

        importResponse.setTotalRows(3);
        importResponse.setErrorRows(0);
        importResponse.setSuccessRows(3);
        String contentType = "text/csv";
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/files/EmployeeLeaveCsvFile.csv"));
        MultipartFile file = new MockMultipartFile("EmployeeLeaveCsvFile.csv", "EmployeeLeaveCsvFile.csv", contentType, content);
        when(employeeStatusService.importLeaveEmployee(any(ExcelResponseModel.class))).thenReturn(importResponse);

        BaseResponse<ImportResponse> response = importDataController.importData(file, "import-leave-employee",null);
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNull(response.getMessage());
        assertEquals(importResponse, response.getData());
    }

    @Test
    @SneakyThrows
    public void testImportOnBenchEmployee_WhenValidEmployeeOnBenchFileExcel_thenSuccess() {
        ImportResponse importResponse = new ImportResponse();
        importResponse.setTotalRows(3);
        importResponse.setSuccessRows(3);
        importResponse.setErrorRows(0);
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/files/EmployeeOnBenchExcelFile.xlsx"));
        MultipartFile file = new MockMultipartFile("EmployeeOnBenchExcelFile.xlsx", "EmployeeOnBenchExcelFile.xlsx", contentType, content);
        when(employeeService.importOnBenchEmployee(any(ExcelResponseModel.class), eq(file))).thenReturn(importResponse);
        BaseResponse<ImportResponse> response = importDataController.importData(file, "import-on-bench-employee",null);
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNull(response.getMessage());
        assertEquals(importResponse, response.getData());
    }

    @Test
    @SneakyThrows
    public void testImportOnBenchEmployee_WhenValidEmployeeOnBenchFileCSV_thenSuccess() {
        ImportResponse importResponse = new ImportResponse();

        importResponse.setTotalRows(3);
        importResponse.setErrorRows(0);
        importResponse.setSuccessRows(3);
        String contentType = "text/csv";
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/files/EmployeeOnBenchCsvFile.csv"));
        MultipartFile file = new MockMultipartFile("EmployeeOnBenchCsvFile.csv", "EmployeeOnBenchCsvFile.csv", contentType, content);
        when(employeeService.importOnBenchEmployee(any(ExcelResponseModel.class), eq(file))).thenReturn(importResponse);

        BaseResponse<ImportResponse> response = importDataController.importData(file, "import-on-bench-employee",null);
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNull(response.getMessage());
        assertEquals(importResponse, response.getData());
    }

}
