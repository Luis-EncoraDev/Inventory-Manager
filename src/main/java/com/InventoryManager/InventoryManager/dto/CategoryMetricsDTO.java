package com.InventoryManager.InventoryManager.dto;

public class CategoryMetricsDTO {
    private Integer totalStock;
    private Float totalUnitPriceSum;
    private Float averageUnitPrice;

    public CategoryMetricsDTO(Integer totalStock, Float totalUnitPriceSum, Float averageUnitPrice) {
        this.totalStock = totalStock;
        this.totalUnitPriceSum = totalUnitPriceSum;
        this.averageUnitPrice = averageUnitPrice;
    }
}
