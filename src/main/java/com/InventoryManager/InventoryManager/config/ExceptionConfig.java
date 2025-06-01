package com.InventoryManager.InventoryManager.config;

import com.InventoryManager.InventoryManager.dto.ProductException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionConfig extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ProductException.class})
    protected ResponseEntity<ProductException> handleProductException(ProductException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getStatus());
    }
}

