package com.chubaievskyi.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputReaderTest {

    private InputReader inputReader;

    @BeforeEach
    public void setUp() {
        inputReader = InputReader.getInstance();
    }

    @Test
    void testPropertiesReader() {
        assertNotNull(inputReader.getUrl());
        assertNotNull(inputReader.getUsername());
        assertNotNull(inputReader.getPassword());
        assertTrue(inputReader.getPoolSize() > 0);
        assertNotNull(inputReader.getProductType());
        assertTrue(inputReader.getNumberOfShops() > 0);
        assertTrue(inputReader.getNumberOfProduct() > 0);
        assertTrue(inputReader.getTotalNumberOfLines() > 0);
        assertTrue(inputReader.getNumberOfThreads() > 0);
        assertTrue(inputReader.getMaxProductQuantity() > 0);
        assertTrue(inputReader.getBatchSize() > 0);
    }
}