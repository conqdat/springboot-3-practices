package com.base.projectbase.service.impl;

import com.base.projectbase.exception.ResourceNotFoundException;
import com.base.projectbase.model.dto.ProductDTO;
import com.base.projectbase.entity.Product;
import com.base.projectbase.repository.ProductRepository;
import com.base.projectbase.service.ProductService;
import com.base.projectbase.transformation.ProductTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;
    private final ProductTransformer productTransformer;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductTransformer productTransformer) {
        this.productRepository = productRepository;
        this.productTransformer = productTransformer;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productTransformer::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = productTransformer.convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return productTransformer.convertToDTO(savedProduct);
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long id) {
        Product updatedProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        updatedProduct.setProductName(productDTO.getProductName());
        updatedProduct.setProductPrice(productDTO.getProductPrice());
        updatedProduct = productRepository.save(updatedProduct);

        return productTransformer.convertToDTO(updatedProduct);
    }


    @Override
    public ProductDTO getProduct(Long id) {
        Product currentProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productTransformer.convertToDTO(currentProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id).ifPresent(deletedProduct -> productRepository.deleteById(id));
    }
}
