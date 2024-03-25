package com.hitachi.coe.fullstack.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import com.hitachi.coe.fullstack.exceptions.CoEException;

@TestPropertySource("classpath:application-data-test.properties")
public class DateFormatUtilsTests {
	   
    @Test
    void testConvertTimestampFromStringWithValidInput() {
        String dateStr = "2023-06-12";
        LocalDate localDate = LocalDate.parse(dateStr);
        Timestamp expectedTimestamp = Timestamp.valueOf(localDate.atStartOfDay());
        Timestamp actualTimestamp = DateFormatUtils.convertTimestampFromString(dateStr);
        
        assertEquals(expectedTimestamp, actualTimestamp);
    }
    
    @Test
    void testConvertTimestampFromStringWithNullInput() {
        String dateStr = null;
        Timestamp actualTimestamp = DateFormatUtils.convertTimestampFromString(dateStr);
        assertNull(actualTimestamp);
    }
    
    @Test
    void testConvertTimestampFromStringWithBlankInput() {
        String dateStr = "   ";
        Timestamp actualTimestamp = DateFormatUtils.convertTimestampFromString(dateStr);
        assertNull(actualTimestamp);
    }
    
    @Test
    void testConvertTimestampFromStringWithInvalidInput() {
        String dateStr = "invalid date";
        assertThrows(CoEException.class, () -> {
        	DateFormatUtils.convertTimestampFromString(dateStr);
        });
    }
    @Test
    void testGetMonthRange() {
        LocalDateTime fromDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime toDate = LocalDateTime.of(2023, 3, 1, 0, 0);

        List<Integer> expectedMonths = new ArrayList<>();
        expectedMonths.add(1);
        expectedMonths.add(2);
        expectedMonths.add(3);

        Timestamp mockedFromTimestamp = mock(Timestamp.class);
        when(mockedFromTimestamp.toLocalDateTime()).thenReturn(fromDate);
        Timestamp mockedToTimestamp = mock(Timestamp.class);
        when(mockedToTimestamp.toLocalDateTime()).thenReturn(toDate);

        List<Integer> actualMonths = DateFormatUtils.getMonthRange(mockedFromTimestamp, mockedToTimestamp);
        assertEquals(expectedMonths, actualMonths);
    }

}
