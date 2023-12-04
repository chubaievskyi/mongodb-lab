package com.chubaievskyi.database;

import com.chubaievskyi.util.ConnectionManager;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseIndexManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseIndexManager.class);

    public void createIndexes() {
        LOGGER.info("Creating indexes!");

        createIndex("_id");
        createIndex("product_id");
    }

    private void createIndex(String fieldName) {
        MongoDatabase database = ConnectionManager.getDatabase();
        MongoCollection<Document> collection = database.getCollection("products_in_shops");

        LOGGER.info("Creating index on {}.", fieldName);
        Document index = new Document(fieldName, 1);
        collection.createIndex(index);
        LOGGER.info("Index on {} created.", fieldName);
    }
}