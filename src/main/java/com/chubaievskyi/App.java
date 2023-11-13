package com.chubaievskyi;

import com.chubaievskyi.database.DBCreator;
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
        new DBCreator().run();
//        new ProductQueryExecutor().findShopByCategory();
//        new DatabaseIndexManager().createIndex();
//        new ProductQueryExecutor().findShopByCategory();
        LOGGER.info("End of program!");
    }
}
