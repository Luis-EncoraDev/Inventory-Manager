package com.InventoryManager.InventoryManager.controller;

import com.InventoryManager.InventoryManager.dto.CategoryMetricsDTO;
import com.InventoryManager.InventoryManager.dto.ProductRequestDTO;
import com.InventoryManager.InventoryManager.dto.ProductResponseDTO;
import com.InventoryManager.InventoryManager.model.ProductModel;
import com.InventoryManager.InventoryManager.service.ProductService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest; // Make sure this is imported
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> category,
            @RequestParam(required = false) Boolean inStock,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<ProductResponseDTO> products = productService.getAllProducts(name, category, inStock, pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        var product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    };

    @GetMapping("/categoryTotalStock/{category}")
    public ResponseEntity<Integer> getTotalProductStockInCategory(@PathVariable String category) {
        return new ResponseEntity<Integer>(productService.getTotalProductsInStockInCategory(category), HttpStatus.OK);
    }

    @GetMapping("/categoryTotalValue/{category}")
    public ResponseEntity<Float> getTotalValueInCategory(@PathVariable String category) {
        return new ResponseEntity<Float>(productService.getTotalValueInCategory(category), HttpStatus.OK);
    }

    @GetMapping("/categoryAverageValue/{category}")
    public ResponseEntity<Float> getAverageValueInCategory(@PathVariable String category) {
        return new ResponseEntity<Float>(productService.getAverageValueInCategory(category), HttpStatus.OK);
    }

    @GetMapping("/averageValue")
    public ResponseEntity<Float> getAverageValue() {
        return new ResponseEntity<Float>(productService.getAverageValue(), HttpStatus.OK);
    }

    @GetMapping("/categoryMetrics/{category}")
    public ResponseEntity<CategoryMetricsDTO> getCategoryMetrics(@PathVariable String category) {
        return new ResponseEntity<CategoryMetricsDTO>(productService.getCategoryMetrics(category), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO createdProduct = productService.createProduct(productRequestDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/outofstock")
    public ResponseEntity<ProductResponseDTO> markProductOutOfStock(@PathVariable Long id) {
        ProductResponseDTO product = productService.markOutOfStock(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO updatedProduct = productService.updateProduct(id, productRequestDTO);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PutMapping("/{id}/instock")
    public ResponseEntity<ProductResponseDTO> markProductInStock(@PathVariable Long id, @RequestParam int quantity) {
        ProductResponseDTO product = productService.markInStock(id, quantity);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}