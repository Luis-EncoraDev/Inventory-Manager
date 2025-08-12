package com.InventoryManager.InventoryManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {
    private String name;
    private String category;
    private BigDecimal unitPrice;
    private LocalDate expirationDate;
    private Integer stockQuantity;
}

