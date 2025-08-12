package com.InventoryManager.InventoryManager.service;
import com.InventoryManager.InventoryManager.dto.CategoryMetricsDTO;
import com.InventoryManager.InventoryManager.dto.ProductException;
import com.InventoryManager.InventoryManager.dto.ProductRequestDTO;
import com.InventoryManager.InventoryManager.dto.ProductResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.InventoryManager.InventoryManager.model.ProductModel;
import com.InventoryManager.InventoryManager.repository.ProductRepository;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private ProductModel toProductModel(ProductRequestDTO dto) {
        ProductModel product = new ProductModel();
        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setUnitPrice(dto.getUnitPrice());
        product.setExpirationDate(dto.getExpirationDate());
        product.setStockQuantity(dto.getStockQuantity());
        return product;
    }

    private ProductResponseDTO toProductResponseDTO(ProductModel product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCategory(product.getCategory());
        dto.setUnitPrice(product.getUnitPrice());
        dto.setExpirationDate(product.getExpirationDate());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setCreationDate(product.getCreationDate());
        dto.setUpdateDate(product.getUpdateDate());
        dto.setInStock(product.isInStock());
        return dto;
    }

    public Page<ProductResponseDTO> getAllProducts(
            String name,
            List<String> categories,
            Boolean inStock,
            Pageable pageable) {
        return productRepository.findProductsByFilters(name, categories, inStock, pageable)
                .map(this::toProductResponseDTO);
    }

    public ProductResponseDTO getProductById(Long id) {
        ProductModel product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException("Did not find product with id " + id, HttpStatus.NOT_FOUND));
        return toProductResponseDTO(product);
    }

    public ProductResponseDTO createProduct(@Valid ProductRequestDTO productRequestDTO) {
        ProductModel product = toProductModel(productRequestDTO);
        ProductModel createdProduct = productRepository.save(product);
        return toProductResponseDTO(createdProduct);
    }

    public ProductResponseDTO updateProduct(Long id, @Valid ProductRequestDTO productRequestDTO) {
        ProductModel updatedProduct = productRepository.findById(id)
                .map((toUpdateProduct) -> {
                    toUpdateProduct.setName(productRequestDTO.getName());
                    toUpdateProduct.setCategory(productRequestDTO.getCategory());
                    toUpdateProduct.setUnitPrice(productRequestDTO.getUnitPrice());
                    toUpdateProduct.setExpirationDate(productRequestDTO.getExpirationDate());
                    toUpdateProduct.setStockQuantity(productRequestDTO.getStockQuantity());
                    return toUpdateProduct;
                }).orElseThrow(() -> new ProductException("Didn't find product with id: " + id, HttpStatus.NOT_FOUND));
        return toProductResponseDTO(productRepository.save(updatedProduct));
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductException("Product not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        productRepository.deleteById(id);
    }

    public ProductResponseDTO markOutOfStock(Long id) {
        ProductModel product = productRepository.findById(id)
                .map(p -> {
                    p.markOutOfStock();
                    return productRepository.save(p);
                })
                .orElseThrow(() -> new ProductException("Product not found with id: " + id, HttpStatus.NOT_FOUND));
        return toProductResponseDTO(product);
    }

    public ProductResponseDTO markInStock(Long id, int quantity) {
        ProductModel product = productRepository.findById(id)
                .map(p -> {
                    p.markInStock(quantity);
                    return productRepository.save(p);
                })
                .orElseThrow(() -> new ProductException("Product not found with id: " + id, HttpStatus.NOT_FOUND));
        return toProductResponseDTO(product);
    }

    public Integer getTotalProductsInStockInCategory(String category) {
        Integer total = productRepository.getTotalProductStockInCategory(category);
        if (total == null) {
            throw new ProductException("No products found in category: " + category, HttpStatus.NOT_FOUND);
        }
        return total;
    }

    public Float getTotalValueInCategory(String category) {
        Float total = productRepository.getTotalValueInCategory(category);
        if (total == null) {
            throw new ProductException("No products found in category: " + category, HttpStatus.NOT_FOUND);
        }
        return total;
    }

    public Float getAverageValueInCategory(String category) {
        Float avg = productRepository.getAverageValueInCategory(category);
        if (avg == null) {
            throw new ProductException("No products found in category: " + category, HttpStatus.NOT_FOUND);
        }
        return avg;
    }

    public Float getAverageValue() {
        Float avg = productRepository.getAverageValue();
        if (avg == null) {
            throw new ProductException("No products found.", HttpStatus.NOT_FOUND);
        }
        return avg;
    }

    public CategoryMetricsDTO getCategoryMetrics(String category) {
        CategoryMetricsDTO metrics = productRepository.getCategoryMetrics(category);
        if (metrics == null) {
            throw new ProductException("No products found in category: " + category, HttpStatus.NOT_FOUND);
        }
        return metrics;
    }
 }