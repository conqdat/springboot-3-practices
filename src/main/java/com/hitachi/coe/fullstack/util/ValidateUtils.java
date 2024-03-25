package com.hitachi.coe.fullstack.util;
/**
 * Utility class for validating objects
 *
 * @author tminhto
 */
public class ValidateUtils {
    private ValidateUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Check if the string is all numeric or have specific digits
     * 
     * @param str           the string to be checked
     * @param isMax    if true, then string length must be less than or equal to
     *                      digit, otherwise string length must be equal to digit
     * @param digit         the number of digits
     * @param fieldName     the field name to be displayed in the error message
     *                      exception
     * @author tminhto
     * @lastModifier sangvb
     * @throws IllegalArgumentException
     */
    public static void checkStringNumericWithSpecificDigits(String str, boolean isMax, Integer digit, String fieldName) {
        // string is required
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        // digit is required
        if (digit == null) {
            throw new IllegalArgumentException("digit is required");
        }
        // fieldName is required
        if (fieldName == null || fieldName.isEmpty()) {
            throw new IllegalArgumentException("fieldName is required");
        }

        // Checks if the string is all numbers
        if (!str.matches("\\d+")) {
            throw new IllegalArgumentException(fieldName + " must be numeric");
        }

        // Check if the string is allowed to exceed the maximum number of digits
        if (isMax && (str.length() > digit)) {
            throw new IllegalArgumentException(fieldName + " must be less than or equal to " + digit + " digits");
        }
        if (!isMax && (str.length() != digit)) {
            throw new IllegalArgumentException(fieldName + " must be " + digit + " digits");
        }       
    }

}
