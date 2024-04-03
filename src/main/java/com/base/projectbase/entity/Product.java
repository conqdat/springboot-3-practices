package com.base.projectbase.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "products")
public class Product extends BaseEntity {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private double productPrice;

    @Column(name = "product_code")
    private String productCode;
}
