package com.chubaievskyi.util;

import com.chubaievskyi.TestConnectionManager;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValueGeneratorTest {

    private static final MongoDatabase database = TestConnectionManager.getDatabase();

    @Test
    void testGenerateValue() {

        ValueGenerator valueGenerator = new ValueGenerator();
        valueGenerator.setDatabase(database);
        valueGenerator.generateValue();

        MongoCollection<Document> shopsCollection = database.getCollection("shops");
        long numberOfShopsDocuments = shopsCollection.countDocuments();
        assertEquals(99, numberOfShopsDocuments, "Unexpected number of documents in 'shops' collection");

        MongoCursor<Document> shopsCursor = shopsCollection.find().limit(1).iterator();
        assertTrue(shopsCursor.hasNext(), "No documents found in 'shops' collection");
        Document shopDocument = shopsCursor.next();
        assertTrue(shopDocument.containsKey("name"));
        assertTrue(shopDocument.containsKey("city"));
        assertTrue(shopDocument.containsKey("street"));
        assertTrue(shopDocument.containsKey("number"));

        MongoCollection<Document> productsCollection = database.getCollection("products");
        long numberOfProductsDocuments = productsCollection.countDocuments();
        assertEquals(999, numberOfProductsDocuments, "Unexpected number of documents in 'products' collection");

        MongoCursor<Document> productsCursor = productsCollection.find().limit(1).iterator();
        assertTrue(productsCursor.hasNext(), "No documents found in 'products' collection");
        Document productDocument = productsCursor.next();
        assertTrue(productDocument.containsKey("name"));
        assertTrue(productDocument.containsKey("category"));
    }

    @AfterEach
    public void cleanUpDatabase() {
        database.drop();
    }

    @AfterAll
    public static void tearDown() {
        TestConnectionManager.close();
    }
}