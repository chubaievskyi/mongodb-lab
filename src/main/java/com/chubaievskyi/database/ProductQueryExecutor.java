package com.chubaievskyi.database;

import com.chubaievskyi.util.ConnectionManager;
import com.chubaievskyi.util.InputReader;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductQueryExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductQueryExecutor.class);
    private static final InputReader INPUT_READER = InputReader.getInstance();
    private static final String PRODUCT_TYPE = INPUT_READER.getProductType();
    private static final MongoDatabase database = ConnectionManager.getDatabase();
    private long startTime;
    private long endTime;

    public void findShopByCategory() {
        LOGGER.info("Method findShopByCategory() class ProductQueryExecutor start!");

        startTime = System.currentTimeMillis();
        List<ObjectId> productIds = findProductIdsByCategory();
        if (!productIds.isEmpty()) {
            Document shopId = findShopWithMaxQuantity(productIds);
            if (shopId != null) {
                endTime = System.currentTimeMillis();
                printShopInfo(shopId);
            } else {
                LOGGER.info("No shop found with the maximum quantity for the given category.");
            }
        } else {
            LOGGER.info("There are no products in the {} category", PRODUCT_TYPE);
        }
    }

    public List<ObjectId> findProductIdsByCategory() {
        MongoCollection<Document> productCollection = database.getCollection("products");

        return productCollection
                .find(Filters.eq("category", PRODUCT_TYPE))
                .map(document -> document.getObjectId("_id"))
                .into(new ArrayList<>());
    }

    public Document findShopWithMaxQuantity(List<ObjectId> productIds) {
        MongoCollection<Document> productsInShopsCollection = database.getCollection("products_in_shops");

        return productsInShopsCollection.aggregate(Arrays.asList(
                Aggregates.match(Filters.in("product_id", productIds)),
                Aggregates.group("$shop_id", Accumulators.sum("totalQuantity", "$quantity")),
                Aggregates.sort(Sorts.descending("totalQuantity")),
                Aggregates.limit(1)
        )).first();
    }

    public void printShopInfo(Document shop) {
        MongoCollection<Document> shopsCollection = database.getCollection("shops");
        Document shopDocument = shopsCollection.find(Filters.eq("_id", shop.getObjectId("_id"))).first();

        if (shopDocument != null) {
            String shopName = shopDocument.getString("name");
            String shopCity = shopDocument.getString("city");
            String shopStreet = shopDocument.getString("street");
            String shopNumber = shopDocument.getString("number");
            String quantity = shop.get("totalQuantity").toString();

            LOGGER.info("The largest number of products in the {} category is in the store: {}, {}, {}, {} - {}pcs",
                    PRODUCT_TYPE, shopName, shopCity, shopStreet, shopNumber, quantity);
        } else {
            LOGGER.info("Shop not found.");
        }
        long resultTime = endTime - startTime;
        LOGGER.info("Query execution time(ms): {}", resultTime);
    }
}
