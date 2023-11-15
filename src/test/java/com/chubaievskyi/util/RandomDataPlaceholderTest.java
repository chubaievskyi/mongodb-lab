//package com.chubaievskyi.util;
//
//import com.chubaievskyi.TestConnectionManager;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoCursor;
//import com.mongodb.client.MongoDatabase;
//import de.flapdoodle.embed.mongo.MongodExecutable;
//import de.flapdoodle.embed.mongo.MongodStarter;
//import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
//import de.flapdoodle.embed.process.runtime.Network;
//import org.bson.Document;
//import org.jetbrains.annotations.NotNull;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import java.util.concurrent.atomic.AtomicInteger;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class RandomDataPlaceholderTest {
//
//    private static final MongoDatabase database = TestConnectionManager.getDatabase();
//
//    @Test
//    void testRun() {
//        MongoCollection<Document> productsInShopsV2Collection = getDocumentMongoCollection();
//        long numberOfDocuments = productsInShopsV2Collection.countDocuments();
//        assertEquals(3001, numberOfDocuments, "Unexpected number of documents in 'products_in_shops_v2' collection");
//
//        MongoCursor<Document> productsInShopsV2Cursor = productsInShopsV2Collection.find().limit(1).iterator();
//        assertTrue(productsInShopsV2Cursor.hasNext(), "No documents found in 'products_in_shops_v2' collection");
//        Document productInShopsV2Document = productsInShopsV2Cursor.next();
//        assertTrue(productInShopsV2Document.containsKey("shopV2"));
//        assertTrue(productInShopsV2Document.containsKey("productV2"));
//        assertTrue(productInShopsV2Document.containsKey("quantityV2"));
//    }
//
//    @NotNull
//    private static MongoCollection<Document> getDocumentMongoCollection() {
//        MongoDatabase database = TestConnectionManager.getDatabase();
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
//        return database.getCollection("products_in_shops_v2");
//    }
//
//    @AfterEach
//    public void cleanUpDatabase() {
//        database.drop();
//    }
//
//    @AfterAll
//    public static void tearDown() {
//        TestConnectionManager.close();
//    }
//
//}
