package com.chubaievskyi.database;

import com.chubaievskyi.exceptions.DBExecutionException;
import com.mongodb.MongoException;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectionGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionGenerator.class);

    public void createCollections(MongoDatabase database) {
        LOGGER.info("Method createCollections() class CollectionsGenerator start!");
        try {
            deleteCollectionIfExists(database, "shops");
            deleteCollectionIfExists(database, "products");
            deleteCollectionIfExists(database, "products_in_shops");

            createCollection(database, "shops");
            createCollection(database, "products");
            createCollection(database, "products_in_shops");

        } catch (MongoException e) {
            throw new DBExecutionException("Error while generating collections.", e);
        }
    }

    private void deleteCollectionIfExists(MongoDatabase database, String collectionName) {
        for (String existingCollection : database.listCollectionNames()) {
            if (existingCollection.equals(collectionName)) {
                LOGGER.info("Collection '{}' already exists. Deleting...", collectionName);
                database.getCollection(collectionName).drop();
                LOGGER.info("Collection '{}' has been deleted", collectionName);
            }
        }
    }

    private void createCollection(MongoDatabase database, String collectionName) {
        LOGGER.info("Creating collection '{}'", collectionName);
        database.createCollection(collectionName);
        LOGGER.info("Collection '{}' created.", collectionName);
    }
}