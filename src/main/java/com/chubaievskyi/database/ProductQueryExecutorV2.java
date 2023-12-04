package com.chubaievskyi.database;

import com.chubaievskyi.util.ConnectionManager;
import com.chubaievskyi.util.InputReader;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class ProductQueryExecutorV2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductQueryExecutorV2.class);
    private static final InputReader INPUT_READER = InputReader.getInstance();
    private static final String PRODUCT_TYPE = INPUT_READER.getProductType();
    private static final MongoDatabase database = ConnectionManager.getDatabase();

    public void findShopByCategory() {
        LOGGER.info("Method findShopByCategoryV2() class ProductQueryExecutor start!");
        MongoCollection<Document> productsCollection = database.getCollection("products");

        long startTime = System.currentTimeMillis();

        Document document = productsCollection.aggregate(Arrays.asList(
                Aggregates.match(Filters.eq("category", PRODUCT_TYPE)),
                Aggregates.lookup("products_in_shops", "_id", "product_id", "shops_with_max_quantity"),
                Aggregates.unwind("$shops_with_max_quantity"),
                Aggregates.group("$shops_with_max_quantity.shop_id",
                        Accumulators.sum("totalQuantity", "$shops_with_max_quantity.quantity")),
                Aggregates.lookup("shops", "_id", "_id", "shop_info"),
                Aggregates.unwind("$shop_info"),
                Aggregates.sort(Sorts.descending("totalQuantity")),
                Aggregates.limit(1),
                Aggregates.project(
                        Projections.fields(
                                Projections.include("_id", "totalQuantity"),
                                Projections.computed("shop_name", "$shop_info.name"),
                                Projections.computed("shop_city", "$shop_info.city"),
                                Projections.computed("shop_street", "$shop_info.street"),
                                Projections.computed("shop_number", "$shop_info.number")
                        )
                )
        )).first();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;

        if (document != null) {
            String shopName = document.get("shop_name", String.class);
            String shopCity = document.get("shop_city", String.class);
            String shopStreet = document.get("shop_street", String.class);
            String shopNumber = document.get("shop_number", String.class);

            String quantity = document.get("totalQuantity").toString();
            LOGGER.info("The largest number of products in the {} category is in the store: {}, {}, {}, {} - {}pcs",
                    PRODUCT_TYPE, shopName, shopCity, shopStreet, shopNumber, quantity);
            LOGGER.info("Query execution time(ms): {}", resultTime);
        } else {
            LOGGER.info("There are no products in the {} category", PRODUCT_TYPE);
        }
    }
}
