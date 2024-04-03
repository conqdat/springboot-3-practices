package com.base.projectbase.transformation;

import com.base.projectbase.model.dto.ProductDTO;
import com.base.projectbase.entity.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductTransformer {

    @Autowired
    public ProductTransformer(ModelMapper modelMapper) {
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
