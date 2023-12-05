package com.chubaievskyi.util;

import com.chubaievskyi.dto.DTOGenerator;
import com.chubaievskyi.dto.ProductDTO;
import com.chubaievskyi.dto.ShopDTO;
import com.chubaievskyi.exceptions.DBExecutionException;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertManyResult;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class ValueGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValueGenerator.class);
    private final DTOGenerator dtoGenerator = new DTOGenerator();

    public List<Document> generateShopValue(MongoDatabase database, Validator validator, int numberOfShops) {
        List<Document> shopsData = new CopyOnWriteArrayList<>();
        try {
            MongoCollection<Document> shopsCollection = database.getCollection("shops");

            int shopCounter = 0;
            List<Document> batch = new CopyOnWriteArrayList<>();

            while (shopCounter < numberOfShops) {
                ShopDTO shop = dtoGenerator.generateRandomShop();
                if (checkDTOBeforeTransfer(shop, validator)) {
                    Document shopDocument = new Document("name", shop.getName())
                            .append("city", shop.getCity())
                            .append("street", shop.getStreet())
                            .append("number", shop.getNumber());

                    batch.add(shopDocument);
                    shopCounter++;
                }
            }
            InsertManyResult result = shopsCollection.insertMany(batch);
            LOGGER.info("{} documents added to 'shops' collection!", result.getInsertedIds().size());
            shopsData.addAll(batch);
        } catch (MongoException e) {
            throw new DBExecutionException("Error while generating shop values.", e);
        }
        return shopsData;
    }

    public List<Document> generateProductValue(MongoDatabase database, Validator validator, int numberOfProducts) {
        List<Document> productData = new CopyOnWriteArrayList<>();
        try {
            MongoCollection<Document> productCollection = database.getCollection("products");

            int productCounter = 0;
            List<Document> batch = new CopyOnWriteArrayList<>();

            while (productCounter < numberOfProducts) {
                ProductDTO product = dtoGenerator.generateRandomProduct();
                if (checkDTOBeforeTransfer(product, validator)) {
                    Document productDocument = new Document("name", product.getName())
                            .append("category", product.getCategory());

                    batch.add(productDocument);
                    productCounter++;
                }
            }
            InsertManyResult result = productCollection.insertMany(batch);
            LOGGER.info("{} documents added to 'products' collection!", result.getInsertedIds().size());
            productData.addAll(batch);
        } catch (MongoException e) {
            throw new DBExecutionException("Error while generating product values.", e);
        }
        return productData;
    }

    private boolean checkDTOBeforeTransfer(Object obj, Validator validator) {
        Set<ConstraintViolation<Object>> violations = validator.validate(obj);
        return violations.isEmpty();
    }
}