package com.base.projectbase.service.impl;

import com.base.projectbase.entity.Product;
import com.base.projectbase.exception.ResourceNotFoundException;
import com.base.projectbase.model.dto.ProductDTO;
import com.base.projectbase.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void ProductService_GetAllProducts_ReturnProductsDto() {
        // Given
        Product product1 = Product.builder().Id(1L).productName("Product 1").productPrice(100.0).build();
        Product product2 = Product.builder().Id(2L).productName("Product 2").productPrice(200.0).build();
        List<Product> products = Arrays.asList(product1, product2);

        // When
        when(productRepository.findAll()).thenReturn(products);
        List<ProductDTO> result = productService.getAllProducts();

        // Then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getProductName()).isEqualTo("Product 1");
        assertThat(result.get(1).getProductName()).isEqualTo("Product 2");
    }

    @Test
    void ProductService_GetProductById_ReturnProductDto() {
        // Given
        Product product = Product.builder().Id(1L).productName("Product 1").productPrice(100.0).build();

        // When
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        ProductDTO productDTO = productService.getProduct(1L);

        // Then
        assertThat(productDTO.getProductName()).isEqualTo("Product 1");
    }

    @Test
    void ProductService_GetProductById_ReturnNotFound() {
        // Given
        Long invalidId = 100L;

        // When
        when(productRepository.findById(invalidId))
                .thenThrow(new ResourceNotFoundException("Product not found with id: " + invalidId));

        // Then
        assertThatThrownBy(() -> productService.getProduct(invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product not found with id: " + invalidId);
    }

    @Test
    void ProductService_UpdateProduct_UpdateProductDto() {
        // Given
        Long productId = 1L;
        ProductDTO productDTO = ProductDTO.builder().productName("Updated Product").productPrice(150.0).build();
        Product existingProduct = Product.builder().Id(productId).productName("Product 1").productPrice(100.0).build();

        // When
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        ProductDTO result = productService.updateProduct(productDTO, productId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductName()).isEqualTo("Updated Product");
        assertThat(result.getProductPrice()).isEqualTo(150.0);
    }

    @Test
    void ProductService_DeleteProduct_ReturnVoid() {
        // Given
        Long productId = 1L;
        Product product = Product.builder().Id(productId).productName("Product 1").productPrice(100.0).build();

        // When
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById(productId);

        // Then
        Assertions.assertAll(() -> productService.deleteProduct(productId));
    }

    @Test
    void ProductService_CreateProduct_CreateProductDto() {
        // Given
        ProductDTO productDTO = ProductDTO.builder().Id(1L).productName("Product 1").productPrice(100.0).build();
        Product product = Product.builder().Id(1L).productName("Product 1").productPrice(100.0).build();

        // When
        when(productRepository.save(product)).thenReturn(product);

        ProductDTO result = productService.createProduct(productDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductName()).isEqualTo("Product 1");
        assertThat(result.getProductPrice()).isEqualTo(100.0);
    }
}
