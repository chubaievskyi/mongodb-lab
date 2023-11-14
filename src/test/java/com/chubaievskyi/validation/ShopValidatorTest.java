package com.chubaievskyi.validation;

import com.chubaievskyi.dto.ShopDTO;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ShopValidatorTest {

    private ShopValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    public void setUp() {
        validator = new ShopValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void testIsValidCity() {
        assertFalse(validator.isValid(new ShopDTO("Епіцентр №1", "К", "Театральна", "1"), context));
        assertFalse(validator.isValid(new ShopDTO("Епіцентр №1", "Ки", "Театральна", "1"), context));
        assertFalse(validator.isValid(new ShopDTO("Епіцентр №1", "Киї", "Театральна", "1"), context));
        assertTrue(validator.isValid(new ShopDTO("Епіцентр №1", "Київ", "Театральна", "1"), context));
    }

    @Test
    void testIsValidStreet() {
        assertFalse(validator.isValid(new ShopDTO("Епіцентр №1", "Київ", "Театральна7", "1"), context));
        assertFalse(validator.isValid(new ShopDTO("Епіцентр №1", "Київ", "Театральна 7", "1"), context));
        assertFalse(validator.isValid(new ShopDTO("Епіцентр №1", "Київ", "Театральна, 7", "1"), context));
        assertTrue(validator.isValid(new ShopDTO("Епіцентр №1", "Київ", "Театральна", "1"), context));
    }

    @Test
    void testIsValidNumber() {
        assertFalse(validator.isValid(new ShopDTO("Епіцентр №1", "Київ", "Театральна", "0"), context));
        assertFalse(validator.isValid(new ShopDTO("Епіцентр №1", "Київ", "Театральна", "1001"), context));
        assertFalse(validator.isValid(new ShopDTO("Епіцентр №1", "Київ", "Театральна", "2000"), context));
        assertTrue(validator.isValid(new ShopDTO("Епіцентр №1", "Київ", "Театральна", "77"), context));
    }

}