package com.chubaievskyi;

import com.chubaievskyi.database.DBCreator;
import com.chubaievskyi.database.DatabaseIndexManager;
import com.chubaievskyi.database.ProductQueryExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        LOGGER.info("Program start!");
        new DBCreator().run();
        new ProductQueryExecutor().findShopByCategory();
//        new ProductQueryExecutor().findShopByCategoryV2();
        new DatabaseIndexManager().createIndexes();
        new ProductQueryExecutor().findShopByCategory();
//        new ProductQueryExecutor().findShopByCategoryV2();
        LOGGER.info("End of program!");
    }
}
