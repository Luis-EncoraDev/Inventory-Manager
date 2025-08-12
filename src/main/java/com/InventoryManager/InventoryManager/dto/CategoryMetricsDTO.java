package com.InventoryManager.InventoryManager.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryMetricsDTO {
    private Integer totalStock;
    private Float totalUnitPriceSum;
    private Float averageUnitPrice;
}
