package com.InventoryManager.InventoryManager.dto.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductException extends RuntimeException {

    private final HttpStatus status;
    public ProductException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
