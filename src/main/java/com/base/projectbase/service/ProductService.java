package com.base.projectbase.service;

import com.base.projectbase.dto.ProductDTO;
import com.base.projectbase.entity.Product;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO updateProduct(ProductDTO productDTO, Long id);
    ProductDTO getProduct(Long id);
    void deleteProduct(Long id);
}
