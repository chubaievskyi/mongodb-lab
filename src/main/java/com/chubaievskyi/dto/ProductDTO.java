package com.chubaievskyi.dto;

import com.chubaievskyi.validation.ProductValidation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ProductValidation
public class ProductDTO {

    private String name;
    private String category;

    public ProductDTO() {
    }

    public ProductDTO(String name, String category) {
        this.name = name;
        this.category = category;
    }
}