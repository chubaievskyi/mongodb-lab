package com.chubaievskyi.dto;

import com.chubaievskyi.validation.ProductValidation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductDTOTest {

    @Test
    void testDefaultConstructor() {
        ProductDTO productDTO = new ProductDTO();
        assertNull(productDTO.getName());
        assertNull(productDTO.getCategory());
    }

    @Test
    void testParameterizedConstructor() {
        ProductDTO productDTO = new ProductDTO("ProductName", "CategoryName");
        assertEquals("ProductName", productDTO.getName());
        assertEquals("CategoryName", productDTO.getCategory());
    }

    @Test
    void testGettersAndSetters() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("NewName");
        productDTO.setCategory("NewCategory");

        assertEquals("NewName", productDTO.getName());
        assertEquals("NewCategory", productDTO.getCategory());
    }

    @Test
    void testProductValidationAnnotation() {
        ProductDTO productDTO = new ProductDTO();
        assertTrue(productDTO.getClass().isAnnotationPresent(ProductValidation.class));
    }
}