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
        try {
            return productRepository.findProductsByFilters(name, categories, inStock, pageable)
                    .map(this::toProductResponseDTO);
        } catch (Exception ex) {
            throw new ProductException("Error fetching products: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ProductResponseDTO getProductById(Long id) {
        ProductModel product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException("Did not find product with id " + id, HttpStatus.NOT_FOUND));
        return toProductResponseDTO(product);
    }

    public ProductResponseDTO createProduct(@Valid ProductRequestDTO productRequestDTO) {
        ProductModel product = toProductModel(productRequestDTO);
        ProductModel createdProduct;
        try {
            createdProduct = productRepository.save(product);
        } catch (Exception ex) {
            throw new ProductException(ex.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return toProductResponseDTO(createdProduct);
    }

    public ProductResponseDTO updateProduct(Long id, @Valid ProductRequestDTO productRequestDTO) {
        ProductModel updatedProduct;
        try {
            updatedProduct = productRepository.findById(id)
                    .map((toUpdateProduct) -> {
                        toUpdateProduct.setName(productRequestDTO.getName());
                        toUpdateProduct.setCategory(productRequestDTO.getCategory());
                        toUpdateProduct.setUnitPrice(productRequestDTO.getUnitPrice());
                        toUpdateProduct.setExpirationDate(productRequestDTO.getExpirationDate());
                        toUpdateProduct.setStockQuantity(productRequestDTO.getStockQuantity());
                        return toUpdateProduct;
                    }).orElseThrow(() -> new ProductException("Didn't find product with id: " + id, HttpStatus.NOT_FOUND));
            return toProductResponseDTO(productRepository.save(updatedProduct));
        } catch (Exception ex) {
            throw new ProductException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        try {
            return productRepository.getTotalProductStockInCategory(category);
        } catch (Exception ex) { throw  new ProductException(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    public Float getTotalValueInCategory(String category) {
        try {
            return productRepository.getTotalValueInCategory(category);
        } catch (Exception ex) { throw  new ProductException(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    public Float getAverageValueInCategory(String category) {
        try {
            return productRepository.getAverageValueInCategory(category);
        } catch (Exception ex) { throw new ProductException(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    public Float getAverageValue() {
        try {
            return productRepository.getAverageValue();
        } catch (Exception ex) { throw new ProductException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    public CategoryMetricsDTO getCategoryMetrics(String category) {
        try {
            return productRepository.getCategoryMetrics(category);
        } catch (Exception ex) { throw  new ProductException(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
 }