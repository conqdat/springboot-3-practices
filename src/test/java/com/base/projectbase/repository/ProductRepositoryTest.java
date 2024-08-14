package com.base.projectbase.repository;

import com.base.projectbase.entity.Product;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Disabled("Disabled until bug #99 has been fixed")
    @Test
    void test_ex() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            int zero = 0;
            int result = 1 / zero;
        });
    }

    @ParameterizedTest
    @CsvSource({
            "1,2,3",
            "2,2,4",
    })
    void testSum(int first, int second, int expected) {
        assertThat(first + second).isEqualTo(expected);
    }

    @Test
    void timeoutExceeded() {
        assertTimeout(Duration.ofMillis(1000), () -> {
            Thread.sleep(999); // 70 seconds sleep (1 minute 10 seconds)
        });
    }

    @Test
    public void testSaveProduct() {
        // Arrange
        Product product = Product.builder()
                .productCode("123")
                .productName("product_01")
                .build();

        // Action
        Product savedProduct = productRepository.save(product);

        // Assert
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getProductCode()).isEqualTo("123");
        assertThat(savedProduct.getProductName()).isEqualTo("product_01");
    }

    @Test
    public void testGetProductById() {
        // Arrange
        Product product = Product.builder()
                .Id(1100L)
                .productCode("01212")
                .productName("product_01")
                .build();
        // Action
        Product foundProduct = productRepository.save(product);
        // Assert
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getProductCode()).isEqualTo("01212");
        assertThat(foundProduct.getProductName()).isEqualTo("product_01");
    }

    @Test
    public void testGetProducts() {
        // Arrange
        Product product_01 = Product.builder()
                .productCode("123")
                .productName("product_01")
                .build();
        Product product_02 = Product.builder()
                .productCode("123")
                .productName("product_02")
                .build();

        // Action
        Product savedProduct_01 = productRepository.save(product_01);
        Product savedProduct_02 = productRepository.save(product_02);
        List<Product> productList = productRepository.findAll();
        // Assert
        assertThat(savedProduct_01.getProductName()).isEqualTo("product_01");
        assertThat(savedProduct_02.getProductName()).isEqualTo("product_02");
        assertThat(productList.size() == 2);
    }

    @Test
    public void testUpdateProduct() {
        // Arrange
        Product productToSave = Product.builder()
                .Id(1L)  // Change to lowercase id
                .productCode("123")
                .productName("product_01")
                .build();

        // Action
        Product foundProduct = productRepository.save(productToSave);
        foundProduct.setProductCode("1212123");
        foundProduct.setProductName("product_01_updated");
        Product savedProduct = productRepository.save(foundProduct);

        // Assert
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getProductCode()).isEqualTo("1212123");
        assertThat(savedProduct.getProductName()).isEqualTo("product_01_updated");
    }

    @Test
    public void testDeleteProduct() {
        // Arrange
        Product productToSave = Product.builder()
                .Id(1L)
                .productCode("123")
                .productName("product_01")
                .build();
        productRepository.save(productToSave);

        // Action
        productRepository.deleteById(1L);

        // Assert
        assertThat(productRepository.findById(1L)).isEmpty();
    }

}
