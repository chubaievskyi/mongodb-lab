package com.chubaievskyi.database;

import com.chubaievskyi.util.ConnectionManager;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseIndexManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseIndexManager.class);
    private static final MongoDatabase database = ConnectionManager.getDatabase();
    private static final MongoCollection<Document> collection = database.getCollection("products_in_shops");

    public void createIndexes() {
        LOGGER.info("Creating indexes!");

        Document idIndex = new Document("_id", 1);
        collection.createIndex(idIndex);
        LOGGER.info("Index on \"_id\" created.");

        Document categoryIndex = new Document("product_id", 1);
        collection.createIndex(categoryIndex);
        LOGGER.info("Index on \"product_id\" created.");
    }
}
