package com.chubaievskyi.validation;

import com.chubaievskyi.dto.ProductDTO;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ProductValidatorTest {

    private ProductValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    public void setUp() {
        validator = new ProductValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void testIsValidName() {
        assertFalse(validator.isValid(new ProductDTO("Світил", "Освітлення"), context));
        assertFalse(validator.isValid(new ProductDTO("111", "Освітлення"), context));
        assertTrue(validator.isValid(new ProductDTO("Світильник", "Освітлення"), context));
    }

    @Test
    void testIsValidCategory() {
        assertFalse(validator.isValid(new ProductDTO("Світильник", "Освітлення7"), context));
        assertFalse(validator.isValid(new ProductDTO("Світильник", "Освітлення1"), context));
        assertTrue(validator.isValid(new ProductDTO("Світильник", "Освітлення"), context));
    }
}