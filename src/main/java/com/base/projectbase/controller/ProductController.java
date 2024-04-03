package com.base.projectbase.controller;

import com.base.projectbase.model.dto.ProductDTO;
import com.base.projectbase.model.response.ProductResponse;
import com.base.projectbase.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Slf4j
@Tag(name = "Product", description = "Product Resource APIs")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ProductResponse<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> productDTOs = productService.getAllProducts();
        log.info("Get list of Products with size: " + productDTOs.size());
        return new ProductResponse<>(HttpStatus.OK, "List of products", productDTOs);
    }

    @GetMapping("/{id}")
    public ProductResponse<ProductDTO> getProduct(@PathVariable Long id) {
        ProductDTO productDTO = productService.getProduct(id);
        log.info("Get a product with name: " + productDTO.getProductName());
        return new ProductResponse<>(HttpStatus.OK, "Product found", productDTO);
    }

    @PutMapping("/{id}")
    public ProductResponse<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        ProductDTO currentProduct = productService.getProduct(id);
        ProductDTO updatedProduct = productService.updateProduct(currentProduct, id);
        log.info("Updated a product with name: " + updatedProduct.getProductName());
        return new ProductResponse<>(HttpStatus.OK, "Product updated", updatedProduct);
    }

    @PostMapping
    public ProductResponse<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        log.info("Created a product with name: " + createdProduct.getProductName());
        return new ProductResponse<>(HttpStatus.OK, "Product Created", createdProduct);
    }

    @DeleteMapping("/{id}")
    public ProductResponse<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        log.info("Deleted a product with ID: " + id);
        return new ProductResponse<>(HttpStatus.OK, "Product deleted", "Deleted Product with ID " + id);
    }
}
