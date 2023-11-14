package com.chubaievskyi.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DTOGeneratorTest {

    @Test
    void testGenerateRandomShop() {
        DTOGenerator generator = new DTOGenerator();
        ShopDTO shopDTO = generator.generateRandomShop();

        assertNotNull(shopDTO);
        assertNotNull(shopDTO.getName());
        assertNotNull(shopDTO.getCity());
        assertNotNull(shopDTO.getStreet());
        assertNotNull(shopDTO.getNumber());
    }

    @Test
    void testGenerateRandomProduct() {
        DTOGenerator generator = new DTOGenerator();
        ProductDTO productDTO = generator.generateRandomProduct();

        assertNotNull(productDTO.getName());
        assertNotNull(productDTO.getCategory());
    }
}