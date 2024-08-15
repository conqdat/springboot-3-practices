package com.base.projectbase.model.dto;

import com.base.projectbase.entity.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@Getter
@Setter
@AllArgsConstructor
public class ProductDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @JsonProperty("product_name")
    @NotEmpty(message = "product_name must be a required")
    private String productName;

    @JsonProperty("product_code")
    private String productCode;

    @JsonProperty("product_price")
    @NotNull(message = "product_price must be a required")
    @Positive(message = "product_price must be greater than zero")
    private Double productPrice;
}
