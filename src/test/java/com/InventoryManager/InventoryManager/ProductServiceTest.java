package com.InventoryManager.InventoryManager;

import com.InventoryManager.InventoryManager.model.ProductModel;
import com.InventoryManager.InventoryManager.repository.ProductRepository;
import com.InventoryManager.InventoryManager.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private PageRequest pageable;
    private List<ProductModel> mockProducts;
    private Page<ProductModel> mockPage;

    @BeforeEach
    void setUp () {
        pageable = PageRequest.of(0, 10);

        ProductModel product1 = new ProductModel();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setCategory("Electronics");

        ProductModel product2 = new ProductModel();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setCategory("Books");

        mockProducts = Arrays.asList(product1, product2);
        mockPage = new PageImpl<>(mockProducts, pageable, mockProducts.size());
    }

    @Test
    @DisplayName("Should return products")
    void getProducts() {
        String name = "Product";
        List<String> categories = Arrays.asList("Electronics");
        Boolean inStock = true;

        when(productRepository.findProductsByFilters(name, categories, inStock, pageable)).thenReturn(mockPage);
        Page<ProductModel> products = productService.getAllProducts(name, categories, inStock, pageable);

        assertNotNull(products);
        verify(productRepository, times(1)).findProductsByFilters(name, categories, inStock, pageable);
    }

    @Test
    @DisplayName("Should return product when valid ID is provided")
    void getProductById() {
        ProductModel expectedProduct = new ProductModel();
        expectedProduct.setId(1L);
        expectedProduct.setName("Test Product");
        expectedProduct.setCategory("Electronics");

        when(productRepository.findById(1L)).thenReturn(Optional.of(expectedProduct));

        ProductModel result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals("Electronics", result.getCategory());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create product successfully")
    void createProduct() {
        ProductModel inputProduct = new ProductModel();
        inputProduct.setName("New Product");
        inputProduct.setCategory("Electronics");
        inputProduct.setUnitPrice(new BigDecimal("100.00"));

        ProductModel savedProduct = new ProductModel();
        savedProduct.setId(1L);
        savedProduct.setName("New Product");
        savedProduct.setCategory("Electronics");
        savedProduct.setUnitPrice(new BigDecimal("100.00"));

        when(productRepository.save(inputProduct)).thenReturn(savedProduct);

        ProductModel result = productService.createProduct(inputProduct);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Product", result.getName());
        verify(productRepository, times(1)).save(inputProduct);
    }

    @Test
    @DisplayName("Should update product successfully")
    void updateProduct() {
        Long productId = 1L;
        ProductModel existingProduct = new ProductModel();
        existingProduct.setId(productId);
        existingProduct.setName("Old Product");
        existingProduct.setCategory("Books");

        ProductModel updateData = new ProductModel();
        updateData.setName("Updated Product");
        updateData.setCategory("Electronics");
        updateData.setUnitPrice(new BigDecimal("150.0"));

        ProductModel updatedProduct = new ProductModel();
        updatedProduct.setId(productId);
        updatedProduct.setName("Updated Product");
        updatedProduct.setCategory("Electronics");

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(ProductModel.class))).thenReturn(updatedProduct);

        ProductModel result = productService.updateProduct(productId, updateData);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        assertEquals("Electronics", result.getCategory());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(any(ProductModel.class));
    }

    @Test
    @DisplayName("Should delete product successfully")
    void deleteProduct() {
        Long productId = 1L;

        when(productRepository.existsById(productId)).thenReturn(true);

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).existsById(productId);
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    @DisplayName("Should mark product out of stock")
    void markOutOfStock() {
        Long productId = 1L;
        ProductModel existingProduct = new ProductModel();
        existingProduct.setId(productId);
        existingProduct.setName("Test Product");
        //existingProduct.setInStock(true);

        ProductModel outOfStockProduct = new ProductModel();
        outOfStockProduct.setId(productId);
        outOfStockProduct.setName("Test Product");
        //outOfStockProduct.setInStock(false);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(ProductModel.class))).thenReturn(outOfStockProduct);

        ProductModel result = productService.markOutOfStock(productId);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(any(ProductModel.class));
    }

    @Test
    @DisplayName("Should mark product in stock with quantity")
    void markInStock() {
        Long productId = 1L;
        int quantity = 50;
        ProductModel existingProduct = new ProductModel();
        existingProduct.setId(productId);
        existingProduct.setName("Test Product");

        ProductModel inStockProduct = new ProductModel();
        inStockProduct.setId(productId);
        inStockProduct.setName("Test Product");
        inStockProduct.setStockQuantity(quantity);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(ProductModel.class))).thenReturn(inStockProduct);

        ProductModel result = productService.markInStock(productId, quantity);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals(quantity, result.getStockQuantity());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(any(ProductModel.class));
    }

    @Test
    @DisplayName("Should return total products in stock for category")
    void getTotalProductsInStockInCategory() {
        String category = "Electronics";
        Integer expectedTotal = 100;

        when(productRepository.getTotalProductStockInCategory(category)).thenReturn(expectedTotal);

        Integer result = productService.getTotalProductsInStockInCategory(category);

        assertNotNull(result);
        assertEquals(expectedTotal, result);
        verify(productRepository, times(1)).getTotalProductStockInCategory(category);
    }

    @Test
    @DisplayName("Should return total value in category")
    void getTotalValueInCategory() {
        String category = "Electronics";
        Float expectedValue = 1500.0f;

        when(productRepository.getTotalValueInCategory(category)).thenReturn(expectedValue);

        Float result = productService.getTotalValueInCategory(category);

        assertNotNull(result);
        assertEquals(expectedValue, result);
        verify(productRepository, times(1)).getTotalValueInCategory(category);
    }

    @Test
    @DisplayName("Should return average value in category")
    void getAverageValueInCategory() {
        String category = "Electronics";
        Float expectedAverage = 125.5f;

        when(productRepository.getAverageValueInCategory(category)).thenReturn(expectedAverage);

        Float result = productService.getAverageValueInCategory(category);

        assertNotNull(result);
        assertEquals(expectedAverage, result);
        verify(productRepository, times(1)).getAverageValueInCategory(category);
    }

    @Test
    @DisplayName("Should return overall average value")
    void getAverageValue() {
        Float expectedAverage = 98.75f;

        when(productRepository.getAverageValue()).thenReturn(expectedAverage);

        Float result = productService.getAverageValue();

        assertNotNull(result);
        assertEquals(expectedAverage, result);
        verify(productRepository, times(1)).getAverageValue();
    }
}
