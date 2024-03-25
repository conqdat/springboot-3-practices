package com.hitachi.coe.fullstack.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


public class ValidateUtilsTest {
    @Nested
    class TestCheckStringNumericWithSpecificDigits {
        @Test
        void testCheckStringNumericWithSpecificDigits_whenStrIsEmpty_thenThrowException() {
            String str = "";
            boolean isMax = true;
            Integer digits = 8;
            String fieldName = "Ldap";

            assertThrows(IllegalArgumentException.class,() -> ValidateUtils.checkStringNumericWithSpecificDigits(str, isMax, digits, fieldName));
        }

        @Test
        void testCheckStringNumericWithSpecificDigits_whenStrIsNull_thenThrowException() {
            String str = null;
            boolean isMax = true;
            Integer digits = 8;
            String fieldName = "Ldap";

            assertThrows(IllegalArgumentException.class,() -> ValidateUtils.checkStringNumericWithSpecificDigits(str, isMax, digits, fieldName));
        }

        @Test
        void testCheckStringNumericWithSpecificDigits_whenStrIsNotAllNumeric_thenThrowException() {
            String str = "121s3462";
            boolean isMax = true;
            Integer digits = 8;
            String fieldName = "Ldap";

            assertThrows(IllegalArgumentException.class,() -> ValidateUtils.checkStringNumericWithSpecificDigits(str, isMax, digits, fieldName));
        }

        @Test
        void testCheckStringNumericWithSpecificDigits_whenDigitsIsNull_thenThrowException() {
            String str = "12153462";
            boolean isMax = true;
            Integer digits = null;
            String fieldName = "Ldap";

            assertThrows(IllegalArgumentException.class,() -> ValidateUtils.checkStringNumericWithSpecificDigits(str, isMax, digits, fieldName));
        }

        @Test
        void testCheckStringNumericWithSpecificDigits_whenFieldNameIsNull_thenThrowException() {
            String str = "12153462";
            boolean isMax = true;
            Integer digits = 8;
            String fieldName = null;

            assertThrows(IllegalArgumentException.class,() -> ValidateUtils.checkStringNumericWithSpecificDigits(str, isMax, digits, fieldName));
        }

        @Test
        void testCheckStringNumericWithSpecificDigits_whenNumberInStrEqualDigitsAndIsEqualMaxTrue_thenNoException() {
            String str = "12345678";
            boolean isMax = true;
            Integer digits = 8;
            String fieldName = "Ldap";

            assertDoesNotThrow(() -> ValidateUtils.checkStringNumericWithSpecificDigits(str, isMax, digits, fieldName));
        }

        @Test
        void testCheckStringNumericWithSpecificDigits_whenNumberInStrLessThanDigitsAndIsEqualMaxTrue_thenNoException() {
            String str = "053462";
            boolean isMax = true;
            Integer digits = 8;
            String fieldName = "Ldap";

            assertDoesNotThrow(() -> ValidateUtils.checkStringNumericWithSpecificDigits(str, isMax, digits, fieldName));
        }

        @Test
        void testCheckStringNumericWithSpecificDigits_whenNumberInStrGreaterThanDigitsAndIsEqualMaxTrue_thenThrowException() {
            String str = "05346212121";
            boolean isMax = true;
            Integer digits = 8;
            String fieldName = "Ldap";

            assertThrows(IllegalArgumentException.class,() -> ValidateUtils.checkStringNumericWithSpecificDigits(str, isMax, digits, fieldName));
        }

        @Test
        void testCheckStringNumericWithSpecificDigits_whenNumberInStrEqualDigitsAndIsEqualMaxFalse_thenNoException() {
            String str = "12345678";
            boolean isMax = false;
            Integer digits = 8;
            String fieldName = "Ldap";

            assertDoesNotThrow(() -> ValidateUtils.checkStringNumericWithSpecificDigits(str, isMax, digits, fieldName));
        }

        @Test
        void testCheckStringNumericWithSpecificDigits_whenNumberInStrLessThanDigitsAndIsEqualMaxFalse_thenThrowException() {
            String str = "053462";
            boolean isMax = false;
            Integer digits = 8;
            String fieldName = "Ldap";

            assertThrows(IllegalArgumentException.class,() -> ValidateUtils.checkStringNumericWithSpecificDigits(str, isMax, digits, fieldName));
        }

        @Test
        void testCheckStringNumericWithSpecificDigits_whenNumberInStrGreaterThanDigitsAndIsEqualMaxFalse_thenThrowException() {
            String str = "05346212121";
            boolean isMax = false;
            Integer digits = 8;
            String fieldName = "Ldap";

            assertThrows(IllegalArgumentException.class,() -> ValidateUtils.checkStringNumericWithSpecificDigits(str, isMax, digits, fieldName));
        }
    }
}
