package com.chubaievskyi.database;

import com.chubaievskyi.exceptions.DBExecutionException;
import com.mongodb.MongoException;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectionsGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionsGenerator.class);

    public void createCollections(MongoDatabase database) {
        LOGGER.info("Method createCollections() class CollectionsGenerator start!");
        try {
            deleteCollectionIfExists(database, "shops");
            deleteCollectionIfExists(database, "products");
            deleteCollectionIfExists(database, "products_in_shops");
            deleteCollectionIfExists(database, "products_in_shops_v2");

            database.createCollection("shops");
            LOGGER.info("Collection \"shops\" created.");
            database.createCollection("products");
            LOGGER.info("Collection \"products\" created.");
            database.createCollection("products_in_shops");
            LOGGER.info("Collection \"products_in_shops\" created.");
            database.createCollection("products_in_shops_v2");
            LOGGER.info("Collection \"products_in_shops_v2\" created.");

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
}
