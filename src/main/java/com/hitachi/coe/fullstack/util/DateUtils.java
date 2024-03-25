package com.hitachi.coe.fullstack.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private DateUtils() {
    }

    /**
     * Convert LocalDate to Date using Java 9+ syntax
     * 
     * @param dateToConvert of type {@link Date} to convert
     * @return a {@link LocalDate} object
     */
    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return LocalDate.ofInstant(
                dateToConvert.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Convert LocalDateTime to Date using Java 9+ syntax
     * 
     * @param dateToConvert of type {@link Date} to convert
     * @return a {@link LocalDateTime} object
     */
    public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return LocalDateTime.ofInstant(
                dateToConvert.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Check if date1 is before date2
     * 
     * @param date1 of type {@link Date} to check
     * @param date2 of type {@link Date} to check
     * @return true if date1 is before date2, false otherwise
     */
    public static boolean isBefore(Date date1, Date date2) {
        return date1.before(date2);
    }

    /**
     * Check if date1 is after date2
     * 
     * @param date1 of type {@link Date} to check
     * @param date2 of type {@link Date} to check
     * @return true if date1 is after date2, false otherwise
     */
    public static boolean isAfter(Date date1, Date date2) {
        return date1.after(date2);
    }

    /**
     * Check if date1 is equal to date2
     * 
     * @param date1 of type {@link Date} to check
     * @param date2 of type {@link Date} to check
     * @return
     */
    public static boolean isEqual(Date date1, Date date2) {
        LocalDate localDate1 = convertToLocalDate(date1);
        LocalDate localDate2 = convertToLocalDate(date2);
        return localDate1.equals(localDate2);
    }

    /**
     * Check if date1 is between date2 and date3
     * 
     * @param date of type {@link Date} to check
     * @param start of type {@link Date} to check
     * @param end of type {@link Date} to check
     * @return true if date1 is between date2 and date3, false otherwise
     */
    public static boolean isBetween(Date date, Date start, Date end) {
        return !date.before(start) && !date.after(end);
    }

    /**
     * Returns the start of the current month as a Date object.
     * The start of the month is defined as the first day of the month at 00:00:00.
     * For example, if the current date is October 26, 2023, this method will return
     * October 1, 2023 at 00:00:00.
     * 
     * @return a Date object representing the start of the current month
     * @author tminhto
     */
    public static Date getStartOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        resetTimeToZero(calendar);
        return calendar.getTime();
    }

    /**
     * Returns the end of the current month as a Date object.
     * The end of the month is defined as the last day of the month at 23:59:59.
     * For example, if the current date is October 26, 2023, this method will return
     * October 31, 2023 at 00:00:00.
     * 
     * @return a Date object representing the end of the current month
     * @author tminhto
     */
    public static Date getEndOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        resetTimeToZero(calendar);
        return calendar.getTime();
    }

    /**
     * Reset time to zero for a Calendar object.
     * @param calendar the Calendar object to reset time to zero
     * @return void
     * @author tminhto
     */
    private static void resetTimeToZero(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}