package com.chubaievskyi.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class PropertyLoaderTest {

    private PropertyLoader propertyLoader;

    @BeforeEach
    public void setup() {
        propertyLoader = new PropertyLoader();
    }

    @Test
    void loadPropertiesWhenPropertiesFileExists() {

        propertyLoader.loadProperties();
        Properties properties = propertyLoader.loadProperties();

        assertEquals("mongodb://localhost:27017", properties.getProperty("db.url"));
        assertEquals("mongo", properties.getProperty("db.username"));
        assertEquals("mongo", properties.getProperty("db.password"));
        assertEquals("5", properties.getProperty("db.pool.size"));
    }
}