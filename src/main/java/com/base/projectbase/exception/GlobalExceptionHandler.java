package com.base.projectbase.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Define a map to map actual field names to custom field names
    private static final Map<String, String> FIELD_NAME_MAPPING = new HashMap<>();

    static {
        FIELD_NAME_MAPPING.put("productName", "product_name");
        FIELD_NAME_MAPPING.put("productPrice", "product_price");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new TreeMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String customFieldName = FIELD_NAME_MAPPING.getOrDefault(fieldName, fieldName);
            String errorMessage = error.getDefaultMessage();
            errors.put(customFieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
