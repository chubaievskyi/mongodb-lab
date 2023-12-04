package com.chubaievskyi.util;

import com.chubaievskyi.exceptions.DBExecutionException;
import com.github.javafaker.Faker;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertManyResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class RandomDataPlaceholder implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RandomDataPlaceholder.class);
    private static final InputReader INPUT_READER = InputReader.getInstance();
    private static final int MAX_QUANTITY = INPUT_READER.getMaxProductQuantity();
    private static final int BATCH_SIZE = INPUT_READER.getBatchSize();
    private static final Faker RANDOM = new Faker();
    private final AtomicInteger rowCounter;
    private final int numberOfLines;
    private final MongoDatabase database;

    private List<Document> shopsData;
    private List<Document> productData;

    public RandomDataPlaceholder(int numberOfLines, AtomicInteger rowCounter, MongoDatabase database){
        this.numberOfLines = numberOfLines;
        this.rowCounter = rowCounter;
        this.database = database;
    }

    @Override
    public void run() {

        LOGGER.info("Method run() class RandomDataPlaceholder start!");
        shopsData = getAllShopsData(database);
        productData = getAllProductData(database);

        try {
            generateProductsInShops();
        } catch (MongoException e) {
            throw new DBExecutionException("Database query execution error.", e);
        }
    }

    protected void generateProductsInShops() {

        LOGGER.info("Method generateProductsInShops() class RandomDataPlaceholder start!");
        MongoCollection<Document> productsInShopsCollection = database.getCollection("products_in_shops");

        int batchSize = numberOfLines > BATCH_SIZE ? BATCH_SIZE : 1;
        int count = 0;

        while (rowCounter.get() < numberOfLines) {
            List<Document> batch = new CopyOnWriteArrayList<>();

            for (int i = 0; i < batchSize; i++) {
                rowCounter.incrementAndGet();

                Document randomShop = getRandomDocument(shopsData);
                ObjectId shopId = randomShop.getObjectId("_id");
                Document randomProduct = getRandomDocument(productData);
                ObjectId productId = randomProduct.getObjectId("_id");

                int quantity = RANDOM.number().numberBetween(1, MAX_QUANTITY);

                Document document = new Document()
                        .append("shop_id", shopId)
                        .append("product_id", productId)
                        .append("quantity", quantity);

                batch.add(document);
                count++;

                if (count % batchSize == 0 || rowCounter.get() >= numberOfLines) {
                    break;
                }
            }
            InsertManyResult result = productsInShopsCollection.insertMany(batch);
            LOGGER.info("{} documents added to 'products_in_shops' collection!", result.getInsertedIds().size());
        }
    }

    public static Document getRandomDocument(List<Document> documents) {
        Random random = new Random();
        int index = random.nextInt(documents.size());
        return documents.get(index);
    }

    private static List<Document> getAllShopsData(MongoDatabase database) {
        MongoCollection<Document> shopsCollection = database.getCollection("shops");
        return shopsCollection.find().into(new ArrayList<>());
    }

    private static List<Document> getAllProductData(MongoDatabase database) {
        MongoCollection<Document> productCollection = database.getCollection("products");
        return productCollection.find().into(new ArrayList<>());
    }
}
