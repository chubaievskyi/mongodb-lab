package com.chubaievskyi.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;

public class ConnectionManager {

    private static final InputReader INPUT_READER = InputReader.getInstance();
    private static final String DB_URL = INPUT_READER.getUrl();
    private static final String DB_NAME = INPUT_READER.getDbName();
    private static final MongoClient mongoClient;

    @Getter
    private static final MongoDatabase database;

    static {
        mongoClient = MongoClients.create(DB_URL);
        database = mongoClient.getDatabase(DB_NAME);
    }

    private ConnectionManager() {
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}