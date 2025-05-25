package com.InventoryManager.InventoryManager.repository;
import com.InventoryManager.InventoryManager.model.ProductModel;
import com.InventoryManager.InventoryManager.dto.CategoryMetricsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {
    Page<ProductModel> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<ProductModel> findByCategoryIn(List<String> categories, Pageable pageable);

    @Query("SELECT new com.InventoryManager.InventoryManager.dto.CategoryMetricsDTO(" +
            "CAST(SUM(p.stockQuantity) AS integer), " +
            "CAST(SUM(p.unitPrice) AS float )," +
            "CAST(AVG(p.unitPrice) AS float )) FROM ProductModel p WHERE (p.category = :category) GROUP BY p.category")
    CategoryMetricsDTO getCategoryMetrics(@Param("category") String category);

    @Query("SELECT SUM(p.stockQuantity) FROM ProductModel p WHERE (p.category = :category)")
    Integer getTotalProductStockInCategory(@Param("category") String category);

    @Query("SELECT SUM(p.unitPrice) FROM ProductModel p WHERE (p.category = :category)")
    Float getTotalValueInCategory(@Param("category") String category);

    @Query("SELECT AVG(p.unitPrice) FROM ProductModel p WHERE (p.category = :category)")
    Float getAverageValueInCategory(@Param("category") String category);

    @Query("SELECT AVG(p.unitPrice) FROM ProductModel p")
    Float getAverageValue();

    @Query("SELECT p FROM ProductModel p WHERE (:inStock is null OR (p.stockQuantity > 0 AND :inStock = true) OR (p.stockQuantity = 0 AND :inStock = false))")
    Page<ProductModel> findByStockAvailability(@Param("inStock") Boolean inStock, Pageable pageable);

    @Query("SELECT p FROM ProductModel p WHERE " +
            "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:categories IS NULL OR p.category IN :categories) AND " +
            "(:inStock IS NULL OR (p.stockQuantity > 0 AND :inStock = TRUE) OR (p.stockQuantity = 0 AND :inStock = FALSE))")
    Page<ProductModel> findProductsByFilters(
            @Param("name") String name,
            @Param("categories") List<String> categories,
            @Param("inStock") Boolean inStock,
            Pageable pageable);
}
