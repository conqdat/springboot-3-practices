package com.hitachi.coe.fullstack.model;

import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.entity.Project;
import com.hitachi.coe.fullstack.exceptions.CoEException;
import com.hitachi.coe.fullstack.util.DateFormatUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class EmployeeProjectAddModelTest {

    Project project;

    Timestamp date;

    Timestamp startDate;

    Timestamp endDate;

    IEmployeeProjectModel iEmployeeProjectModel;

    @BeforeEach
    void setUp(){

        date = DateFormatUtils.convertTimestampFromString("2023-07-01");
        startDate = DateFormatUtils.convertTimestampFromString("2023-07-12");
        endDate = DateFormatUtils.convertTimestampFromString("2023-09-20");

        project = new Project();
        project.setId(1);
        project.setName("FSCMS");
        project.setDescription("FSCMS");
        project.setStartDate(DateFormatUtils.convertTimestampFromString("2023-07-12"));
        project.setEndDate(DateFormatUtils.convertTimestampFromString("2023-09-20"));

        iEmployeeProjectModel = mock(IEmployeeProjectModel.class);

    }

    @Test
    public void testModelEmployeeProjectAdd(){

        EmployeeProjectAddModel employee = new EmployeeProjectAddModel();
        employee.setEmployeeId(1);
        employee.setEmployeeType("Office");
        employee.setStartDate("2023-05-18");
        employee.setEndDate("2023-08-18");

        assertNotNull(employee);
        assertEquals(1, employee.getEmployeeId());
        assertEquals("Office", employee.getEmployeeType());
        assertEquals("2023-05-18", employee.getStartDate());
        assertEquals("2023-08-18", employee.getEndDate());

    }
}
