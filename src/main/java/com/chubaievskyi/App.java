package com.chubaievskyi;

import com.chubaievskyi.util.ConnectionManager;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        LOGGER.info("Program start!");

        MongoDatabase database = ConnectionManager.getDatabase();

        // Вибір колекції
        MongoCollection<Document> collection = database.getCollection("mycollection");

        // Вставка документа в колекцію
        Document document = new Document("name", "John")
                .append("age", 30);
        collection.insertOne(document);

        // Закриття підключення до MongoDB
        ConnectionManager.close();

        LOGGER.info("End of program!");
    }
}
