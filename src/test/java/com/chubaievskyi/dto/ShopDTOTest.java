package com.chubaievskyi.dto;

import com.chubaievskyi.validation.ShopValidation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShopDTOTest {

    @Test
    void testDefaultConstructor() {
        ShopDTO shopDTO = new ShopDTO();
        assertNull(shopDTO.getName());
        assertNull(shopDTO.getCity());
        assertNull(shopDTO.getStreet());
        assertNull(shopDTO.getNumber());
    }

    @Test
    void testParameterizedConstructor() {
        ShopDTO shopDTO = new ShopDTO("ShopName", "CityName", "StreetName", "123");
        assertEquals("ShopName", shopDTO.getName());
        assertEquals("CityName", shopDTO.getCity());
        assertEquals("StreetName", shopDTO.getStreet());
        assertEquals("123", shopDTO.getNumber());
    }

    @Test
    void testGettersAndSetters() {
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setName("NewName");
        shopDTO.setCity("NewCity");
        shopDTO.setStreet("NewStreet");
        shopDTO.setNumber("456");

        assertEquals("NewName", shopDTO.getName());
        assertEquals("NewCity", shopDTO.getCity());
        assertEquals("NewStreet", shopDTO.getStreet());
        assertEquals("456", shopDTO.getNumber());
    }

    @Test
    void testShopValidationAnnotation() {
        ShopDTO shopDTO = new ShopDTO();
        assertTrue(shopDTO.getClass().isAnnotationPresent(ShopValidation.class));
    }
}