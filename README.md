# Inventory Manager API

A Spring Boot REST API for managing product inventory, category management, and comprehensive product metrics.

## Features

- Product CRUD operations
- Stock management (in-stock/out-of-stock)
- Filtering and sorting
- Product metrics
- Pagination
- Real-time inventory tracking
- H2 in-memory database with web console

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Git

## Database Setup (H2)

This application uses H2 in-memory database for development and testing purposes.

### Configuration

The H2 database is automatically configured with the following settings in `application.properties`:

```
spring.application.name=InventoryManager
server.port = 9090
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.url=jdbc:h2:mem:productsdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```

### Accessing H2 Console

1. Start the application
2. Navigate to: `http://localhost:9090/h2-console`
3. Use the following connection details:
   - **JDBC URL**: `jdbc:h2:mem:productsdb`
   - **Username**: `sa`
   - **Password**: (leave empty)
   - **Driver Class**: `org.h2.Driver`

### Database Schema

The application automatically creates a `ProductModel` table with the following structure:

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | Primary Key, Auto-increment |
| name | VARCHAR(120) | NOT NULL, Max 120 characters |
| category | VARCHAR(255) | NOT NULL |
| unit_price | DECIMAL | NOT NULL, Min 0.01 |
| expiration_date | DATE | Optional |
| stock_quantity | INTEGER | NOT NULL, Min 0 |
| creation_date | DATE | Auto-generated |
| update_date | DATE | Auto-updated |

## Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/Luis-EncoraDev/Inventory-Manager.git
   cd InventoryManager
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```
   Or use the wrapper instead
   ```bash
   ./mvnw clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
   Or use the wrapper instead
   ```
   ./mvnw spring-boot:run
   ```

5. **Verify installation**
   - Application runs on: `http://localhost:9090`
   - H2 Console: `http://localhost:9090/h2-console`
   - API Base URL: `http://localhost:9090/api/products`

## API Documentation

### Base URL
```
http://localhost:9090/api/products
```

### Endpoints

#### 1. Get All Products (with filtering and pagination)
```http
GET /api/products
```

**Query Parameters:**
- `name` (optional): Filter by product name
- `category` (optional): Filter by category (can be multiple)
- `inStock` (optional): Filter by stock status (true/false)
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 10)

**Example:**
```http
GET /api/products?name=Apple&category=Fruits&inStock=true&page=0&size=5
```


#### 2. Get Product by ID
```http
GET /api/products/{id}
```

**Example:**
```http
GET /api/products/1
```

#### 3. Create New Product
```http
POST /api/products
Content-Type: application/json
```

#### 4. Update Product
```http
PUT /api/products/{id}
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Updated Orange",
  "category": "Fruits",
  "unitPrice": 2.50,
  "expirationDate": "2024-12-31",
  "stockQuantity": 75
}
```

#### 5. Delete Product
```http
DELETE /api/products/{id}
```

#### 6. Mark Product Out of Stock
```http
POST /api/products/{id}/outofstock
```

#### 7. Mark Product In Stock
```http
PUT /api/products/{id}/instock?quantity={quantity}
```
You can ommit quantity and it will default to 10

**Example:**
```http
PUT /api/products/1/instock?quantity=100
```

### Category Metrics Endpoints

#### 8. Get Total Stock in Category
```http
GET /api/products/categoryTotalStock/{category}
```

**Response:** Integer value of total stock

#### 9. Get Total Value in Category
```http
GET /api/products/categoryTotalValue/{category}
```

**Response:** Float value of total inventory value

#### 10. Get Average Value in Category
```http
GET /api/products/categoryAverageValue/{category}
```

**Response:** Float value of average product value

#### 11. Get Overall Average Value
```http
GET /api/products/averageValue
```

**Response:** Float value of overall average

#### 12. Get Category Metrics
```http
GET /api/products/categoryMetrics/{category}
```

## Sample Data

You can populate the database with sample data using the H2 console or by making POST requests:

```sql
INSERT INTO PRODUCT_MODEL (name, category, unit_price, expiration_date, stock_quantity, creation_date, update_date) VALUES
('Laptop', 'Electronics', 1299.99, '2026-03-15', 8, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Mouse', 'Electronics', 25.50, NULL, 15, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Chair', 'Furniture', 199.00, NULL, 3, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Desk', 'Furniture', 249.75, NULL, 12, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Coffee', 'Grocery', 12.99, '2025-07-20', 5, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Bread', 'Grocery', 3.20, '2025-05-25', 20, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Tshirt', 'Apparel', 15.00, NULL, 9, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Jeans', 'Apparel', 45.99, NULL, 18, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Shoes', 'Footwear', 79.95, NULL, 4, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Wallet', 'Accessories', 35.00, NULL, 11, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Smartphone', 'Electronics', 1499.00, '2026-01-30', 7, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Keyboard', 'Electronics', 89.99, NULL, 16, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Bookshelf', 'Furniture', 119.50, NULL, 2, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Lamp', 'Furniture', 39.99, NULL, 14, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Tea', 'Grocery', 4.50, '2025-09-10', 6, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Chocolate', 'Grocery', 2.80, '2025-06-01', 22, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Dress', 'Apparel', 39.99, NULL, 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Socks', 'Apparel', 9.75, NULL, 19, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Boots', 'Footwear', 99.00, NULL, 10, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Sunglasses', 'Accessories', 55.00, NULL, 5, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
```
