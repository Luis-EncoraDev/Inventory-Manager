package com.InventoryManager.InventoryManager;

import com.InventoryManager.InventoryManager.model.ProductModel;
import com.InventoryManager.InventoryManager.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DatabaseInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            System.out.println("No products found in the database. Populating with sample data...");
            generateAndSaveSampleProducts(20);
            System.out.println("Successfully added 20 sample products to the database.");
        } else {
            System.out.println("Products already exist. Skipping database initialization.");
        }
    }

    private void generateAndSaveSampleProducts(int count) {
        Random random = new Random();
        String[] categories = {"Electronics", "Books", "Home Goods", "Groceries", "Apparel"};

        for (int i = 1; i <= count; i++) {
            ProductModel product = new ProductModel();

            product.setName("Sample Product " + i);
            product.setCategory(categories[random.nextInt(categories.length)]);
            product.setUnitPrice(BigDecimal.valueOf(10 + random.nextDouble() * 100));
            product.setStockQuantity(random.nextInt(200));

            if (random.nextBoolean()) {
                product.setExpirationDate(LocalDate.now().plusDays(random.nextInt(365)));
            } else {
                product.setExpirationDate(null);
            }

            productRepository.save(product);
        }
    }
}

