//package com.chubaievskyi;
//
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoDatabase;
//import de.flapdoodle.embed.mongo.MongodExecutable;
//import de.flapdoodle.embed.mongo.MongodStarter;
//import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
//import de.flapdoodle.embed.mongo.distribution.Version;
//import de.flapdoodle.embed.process.runtime.Network;
//
//import java.io.IOException;
//
//import static com.chubaievskyi.util.PropertiesLoader.LOGGER;
//
//public class TestConnectionManager {
//
//    private static final String DATABASE_NAME = "test_database";
//    private static final int MONGO_PORT = 27018;
//
//    private static final MongoClient mongoClient;
//    private static MongodExecutable mongodExecutable;
//
//    static {
//        try {
//            startEmbeddedMongo();
//            mongoClient = MongoClients.create("mongodb://localhost:" + MONGO_PORT);
//        } catch (IOException e) {
//            throw new RuntimeException("Error initializing embedded MongoDB for testing", e);
//        }
//    }
//
//    private static void startEmbeddedMongo() throws IOException {
//        MongodStarter starter = MongodStarter.getDefaultInstance();
//
//        mongodExecutable = starter.prepare(
//                ImmutableMongodConfig.builder()
//                        .version(Version.Main.PRODUCTION)
//                        .net(new de.flapdoodle.embed.mongo.config.Net(MONGO_PORT, Network.localhostIsIPv6()))
//                        .build()
//        );
//
//        mongodExecutable.start();
//    }
//
//    public static MongoDatabase getDatabase() {
//        return mongoClient.getDatabase(DATABASE_NAME);
//    }
//
//    public static void close() {
//        if (mongoClient != null) {
//            mongoClient.close();
//            LOGGER.info("MongoDB client closed successfully.");
//        }
//        if (mongodExecutable != null) {
//            try {
//                mongodExecutable.stop();
//                LOGGER.info("Embedded MongoDB server stopped successfully.");
//            } catch (Exception e) {
//                LOGGER.error("Error stopping embedded MongoDB server", e);
//            }
//        }
//    }
//}
//
