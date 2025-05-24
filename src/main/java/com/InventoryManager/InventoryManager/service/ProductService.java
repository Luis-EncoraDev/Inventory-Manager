package com.InventoryManager.InventoryManager.service;
import com.InventoryManager.InventoryManager.dto.exception.ProductException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.InventoryManager.InventoryManager.model.ProductModel;
import com.InventoryManager.InventoryManager.repository.ProductRepository;
import java.util.List;
import java.util.Optional; // Import for Optional

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<ProductModel> getAllProducts(
            String name,
            List<String> categories,
            Boolean inStock,
            Pageable pageable) {
        try {
            return productRepository.findProductsByFilters(name, categories, inStock, pageable);
        } catch (Exception ex) {
            throw new ProductException("Error fetching products: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ProductModel getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductException("Did not find product with id " + id, HttpStatus.NOT_FOUND));
    }

    public ProductModel createProduct(@Valid ProductModel product) {
        ProductModel createdProduct;
        try {
            createdProduct = productRepository.save(product);
        } catch (Exception ex) {
            throw new ProductException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  createdProduct;
    }

    public ProductModel updateProduct(Long id, @Valid ProductModel product) {
        ProductModel updatedProduct;
        try {
            updatedProduct = productRepository.findById(id)
                    .map((toUpdateProduct) -> {
                        toUpdateProduct.setName(product.getName());
                        toUpdateProduct.setCategory(product.getCategory());
                        toUpdateProduct.setUnitPrice(product.getUnitPrice());
                        toUpdateProduct.setExpirationDate(product.getExpirationDate());
                        toUpdateProduct.setStockQuantity(product.getStockQuantity());
                        return toUpdateProduct;
                    }).orElseThrow(() -> new ProductException("Didn't find product with id: " + id, HttpStatus.NOT_FOUND));
            return productRepository.save(updatedProduct);
        } catch (Exception ex) {
            throw new ProductException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteProduct(Long id) {
        // Check if product exists before deleting
        if (!productRepository.existsById(id)) {
            throw new ProductException("Product not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        productRepository.deleteById(id);
    }

    public ProductModel markOutOfStock(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.markOutOfStock();
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new ProductException("Product not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    public ProductModel markInStock(Long id, int quantity) {
        return productRepository.findById(id)
                .map(product -> {
                    product.markInStock(quantity);
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new ProductException("Product not found with id: " + id, HttpStatus.NOT_FOUND));
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

 }