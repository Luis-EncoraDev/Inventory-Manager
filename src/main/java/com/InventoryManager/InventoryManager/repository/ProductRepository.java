package com.InventoryManager.InventoryManager.repository;
import com.InventoryManager.InventoryManager.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {
}
