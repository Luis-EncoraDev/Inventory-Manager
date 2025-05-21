package com.InventoryManager.InventoryManager.repository;
import com.InventoryManager.InventoryManager.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {
    Page<ProductModel> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
