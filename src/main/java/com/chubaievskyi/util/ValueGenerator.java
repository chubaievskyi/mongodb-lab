package com.chubaievskyi.util;

import com.chubaievskyi.dto.DTOGenerator;
import com.chubaievskyi.dto.ProductDTO;
import com.chubaievskyi.dto.ShopDTO;
import com.chubaievskyi.exceptions.DBExecutionException;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Set;

public class ValueGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValueGenerator.class);
    private static final InputReader INPUT_READER = InputReader.getInstance();
    private static final int NUMBER_OF_SHOPS = INPUT_READER.getNumberOfShops();
    private static final int NUMBER_OF_PRODUCTS = INPUT_READER.getNumberOfProduct();
    private static final DTOGenerator dtoGenerator = new DTOGenerator();
    private final Validator validator = initializeValidator();

    public void generateValue() {

        LOGGER.info("Method generateValue() class ValueGenerator start!");

        try {
            generateShopValue();
            generateProductValue();
        } catch (MongoException e) {
            throw new DBExecutionException("Error database query execution (class ValueGenerator).", e);
        }
    }

    private void generateShopValue() {
        try {
            MongoDatabase database = ConnectionManager.getDatabase();
            MongoCollection<Document> shopsCollection = database.getCollection("shops");

            int shopCounter = 0;

            while (shopCounter < NUMBER_OF_SHOPS) {
                ShopDTO shop = dtoGenerator.generateRandomShop();
                if (checkDTOBeforeTransfer(shop, validator)) {
                    Document shopDocument = new Document("name", shop.getName())
                            .append("city", shop.getCity())
                            .append("street", shop.getStreet())
                            .append("number", shop.getNumber());

                    shopsCollection.insertOne(shopDocument);
                    shopCounter++;
                }
            }
        } catch (MongoException e) {
            throw new DBExecutionException("Error while generating shop values.", e);
//        } finally {
//            ConnectionManager.close();
        }
    }

    private void generateProductValue() {
        try {
            MongoDatabase database = ConnectionManager.getDatabase();
            MongoCollection<Document> productsCollection = database.getCollection("products");

            int productCounter = 0;

            while (productCounter < NUMBER_OF_PRODUCTS) {
                ProductDTO product = dtoGenerator.generateRandomProduct();
                if (checkDTOBeforeTransfer(product, validator)) {
                    Document productDocument = new Document("name", product.getName())
                            .append("category", product.getCategory());

                    productsCollection.insertOne(productDocument);
                    productCounter++;
                }
            }
        } catch (MongoException e) {
            throw new DBExecutionException("Error while generating product values.", e);
//        } finally {
//            ConnectionManager.close();
        }
    }

    private boolean checkDTOBeforeTransfer(Object obj, Validator validator) {
        Set<ConstraintViolation<Object>> violations = validator.validate(obj);
        return violations.isEmpty();
    }

    private Validator initializeValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            return factory.getValidator();
        }
    }
}
