//package com.chubaievskyi;
//
//import com.chubaievskyi.util.RandomDataPlaceholder;
//import com.chubaievskyi.util.ValueGenerator;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoCursor;
//import com.mongodb.client.MongoDatabase;
//import org.bson.Document;
//import org.jetbrains.annotations.NotNull;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.concurrent.atomic.AtomicInteger;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class RandomDataPlaceholderInMemoryTest {
//
//    private static final MongoDatabase database = TestConnectionManagerInMemoryTest.getDatabase();
//
//    @Test
//    void testRun() {
//        MongoCollection<Document> productsInShopsCollection = getDocumentMongoCollection();
//        long numberOfDocuments = productsInShopsCollection.countDocuments();
//        assertEquals(3001, numberOfDocuments, "Unexpected number of documents in 'products_in_shops' collection");
//
//        MongoCursor<Document> productsInShopsCursor = productsInShopsCollection.find().limit(1).iterator();
//        assertTrue(productsInShopsCursor.hasNext(), "No documents found in 'products_in_shops' collection");
//        Document productInShopsV2Document = productsInShopsCursor.next();
//        assertTrue(productInShopsV2Document.containsKey("shop_id"));
//        assertTrue(productInShopsV2Document.containsKey("product_id"));
//        assertTrue(productInShopsV2Document.containsKey("quantity"));
//    }
//
//    @NotNull
//    private static MongoCollection<Document> getDocumentMongoCollection() {
//        MongoDatabase database = TestConnectionManagerInMemoryTest.getDatabase();
//
//        ValueGenerator valueGenerator = new ValueGenerator();
//        valueGenerator.setDatabase(database);
//        valueGenerator.generateValue();
//
//        AtomicInteger rowCounter = new AtomicInteger(0);
//        AtomicInteger rowCounter2 = new AtomicInteger(0);
//        RandomDataPlaceholder randomDataPlaceholder = new RandomDataPlaceholder(3001, rowCounter, database, rowCounter2);
//
//        randomDataPlaceholder.run();
//
//        return database.getCollection("products_in_shops");
//    }
//
//    @AfterEach
//    public void cleanUpDatabase() {
//        database.drop();
//    }
//
//    @AfterAll
//    public static void tearDown() {
//        TestConnectionManagerInMemoryTest.close();
//    }
//
//}
