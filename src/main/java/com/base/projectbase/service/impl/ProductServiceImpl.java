package com.base.projectbase.service.impl;

import com.base.projectbase.entity.Product;
import com.base.projectbase.exception.ResourceNotFoundException;
import com.base.projectbase.model.dto.ProductDTO;
import com.base.projectbase.repository.ProductRepository;
import com.base.projectbase.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .sorted(Comparator.comparing(Product::getId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = this.convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return this.convertToDTO(savedProduct);
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long id) {
        Product updatedProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        updatedProduct.setProductName(productDTO.getProductName());
        updatedProduct.setProductPrice(productDTO.getProductPrice());
        updatedProduct = productRepository.save(updatedProduct);

        return this.convertToDTO(updatedProduct);
    }


    @Override
    public ProductDTO getProduct(Long id) {
        Product currentProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return this.convertToDTO(currentProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id).ifPresent(deletedProduct -> productRepository.deleteById(id));
    }

    public ProductDTO convertToDTO(Product product) {
        return ProductDTO.builder()
                .Id(product.getId())
                .productPrice(product.getProductPrice())
                .productName(product.getProductName())
                .build();
    }

    public Product convertToEntity(ProductDTO productDTO) {
        return Product.builder()
                .Id(productDTO.getId())
                .productName(productDTO.getProductName())
                .productPrice(productDTO.getProductPrice())
                .build();
    }
}
