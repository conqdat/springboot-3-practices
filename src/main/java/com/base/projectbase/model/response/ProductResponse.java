package com.base.projectbase.model.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ProductResponse<T> {
    private HttpStatus status;
    private String message;
    private T data;

    public ProductResponse(HttpStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
