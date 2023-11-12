package com.chubaievskyi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesLoader {

    public static final Logger LOGGER = LoggerFactory.getLogger(PropertiesLoader.class);
    private final Properties properties;

    public PropertiesLoader() {
        properties = new Properties();
    }

    public Properties loadProperties() {
        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties");

            if (input != null) {
                properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
                LOGGER.info("Loaded properties from application.properties in classpath");
            } else {
                LOGGER.info("application.properties not found in classpath, please check the classpath and file .properties");
                System.exit(0);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to read properties from file.", e);
        }

        return properties;
    }
}
