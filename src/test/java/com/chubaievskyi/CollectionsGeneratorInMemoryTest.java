//package com.chubaievskyi;
//
//import com.chubaievskyi.database.CollectionsGenerator;
//import com.mongodb.client.MongoCursor;
//import com.mongodb.client.MongoDatabase;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//class CollectionsGeneratorInMemoryTest {
//
//    private static final MongoDatabase database = TestConnectionManagerInMemoryTest.getDatabase();
//
//    @Test
//    void testCreateCollections() {
//
//        CollectionsGenerator collectionsGenerator = new CollectionsGenerator();
//        collectionsGenerator.createCollections(database);
//
//        assertTrue(collectionExists("shops"));
//        assertTrue(collectionExists("products"));
//        assertTrue(collectionExists("products_in_shops"));
//    }
//
//    private boolean collectionExists(String collectionName) {
//        try (MongoCursor<String> cursor = database.listCollectionNames().iterator()) {
//            while (cursor.hasNext()) {
//                if (cursor.next().equals(collectionName)) {
//                    return true;
//                }
//            }
//            return false;
//        }
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
//}
