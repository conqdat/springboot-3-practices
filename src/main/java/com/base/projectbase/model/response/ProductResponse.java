package com.base.projectbase.model.response;

import org.springframework.http.HttpStatus;

public record ProductResponse<T>(HttpStatus status, String message, T data) {
}
