package com.base.projectbase.service.impl;

import com.base.projectbase.dto.ProductDTO;
import com.base.projectbase.entity.Product;
import com.base.projectbase.repository.ProductRepository;
import com.base.projectbase.service.ProductService;
import com.base.projectbase.transformation.ProductTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductTransformer productTransformer;

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
        Product updatedProduct = productRepository.findById(id).orElse(null);
        if(updatedProduct != null) {
            updatedProduct.setProductName(updatedProduct.getProductName());
            updatedProduct.setProductCode(updatedProduct.getProductCode());
        }
        return productTransformer.convertToDTO(updatedProduct);
    }

    @Override
    public ProductDTO getProduct(Long id) {
        Product currentProduct = productRepository.findById(id).orElse(null);
        return productTransformer.convertToDTO(currentProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id).ifPresent(deletedProduct -> productRepository.deleteById(id));
    }
}
