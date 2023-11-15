package com.chubaievskyi.database;

import com.chubaievskyi.util.ConnectionManager;
import com.chubaievskyi.util.InputReader;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class ProductQueryExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductQueryExecutor.class);
    private static final InputReader INPUT_READER = InputReader.getInstance();
    private static final String PRODUCT_TYPE = INPUT_READER.getProductType();
    private static final MongoDatabase database = ConnectionManager.getDatabase();

    public void findShopByCategory() {

        LOGGER.info("Method findShopByCategory() class ProductQueryExecutor start!");
        MongoCollection<Document> collection = database.getCollection("products_in_shops_v2");

        long startTime = System.currentTimeMillis();

        Document document = collection.aggregate(Arrays.asList(
                Aggregates.match(Filters.eq("productV2.category", PRODUCT_TYPE)),
                Aggregates.group("$shopV2._id",
                        Accumulators.first("shop_name", "$shopV2.name"),
                        Accumulators.first("shop_city", "$shopV2.city"),
                        Accumulators.first("shop_street", "$shopV2.street"),
                        Accumulators.first("shop_number", "$shopV2.number"),
                        Accumulators.sum("totalQuantity", "$quantityV2")),
                Aggregates.sort(Sorts.descending("totalQuantity")),
                Aggregates.limit(1)
        )).first();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;

        if (document != null) {
            String results = document.get("shop_name").toString() + ", " + document.get("shop_city").toString() + ", " +
                    document.get("shop_street").toString() + ", " + document.get("shop_number").toString();
            String quantity = document.get("totalQuantity").toString();
            LOGGER.info("The largest number of products in the {} category is in the store: {} - {}pcs",
                    PRODUCT_TYPE, results, quantity);
            LOGGER.info("Query execution time(ms) : {}", resultTime);
        } else {
            LOGGER.info("There are no products in the {} category", PRODUCT_TYPE);
        }


    }
}
