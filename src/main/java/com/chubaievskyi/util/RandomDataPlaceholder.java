package com.chubaievskyi.util;

import com.github.javafaker.Faker;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertManyResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class RandomDataPlaceholder implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RandomDataPlaceholder.class);
    private static final Random RANDOM = new Random();
    private static final InputReader INPUT_READER = InputReader.getInstance();
    private static final int MAX_QUANTITY = INPUT_READER.getMaxProductQuantity();
    private static final int BATCH_SIZE = INPUT_READER.getBatchSize();
    private static final Faker FAKER = new Faker();
    private final int numberOfLines;
    private final AtomicInteger counter;
    private final MongoDatabase database;
    private final List<Document> shopsData;
    private final List<Document> productData;

    public RandomDataPlaceholder(int numberOfLines, AtomicInteger counter, MongoDatabase database,
                                 List<Document> shopsData, List<Document> productData) {
        this.numberOfLines = numberOfLines;
        this.counter = counter;
        this.database = database;
        this.shopsData = shopsData;
        this.productData = productData;
    }

    @Override
    public void run() {
        LOGGER.info("Method run() class RandomDataPlaceholder start!");
        generateProductsInShops();
    }

    private void generateProductsInShops() {

        LOGGER.info("Method generateProductsInShops() class RandomDataPlaceholder start!");
        MongoCollection<Document> productsInShopsCollection = database.getCollection("products_in_shops");

        int batchSize = numberOfLines > BATCH_SIZE ? BATCH_SIZE : 1;
        int count = 0;

        while (counter.get() < numberOfLines) {
            List<Document> batch = new CopyOnWriteArrayList<>();

            for (int i = 0; i < batchSize; i++) {
                counter.incrementAndGet();

                Document randomShop = shopsData.get(RANDOM.nextInt(shopsData.size()));
                ObjectId shopId = randomShop.getObjectId("_id");
                Document randomProduct = productData.get(RANDOM.nextInt(productData.size()));
                ObjectId productId = randomProduct.getObjectId("_id");

                int quantity = FAKER.number().numberBetween(1, MAX_QUANTITY);

                Document document = new Document()
                        .append("shop_id", shopId)
                        .append("product_id", productId)
                        .append("quantity", quantity);

                batch.add(document);
                count++;

                if (count % batchSize == 0 || counter.get() >= numberOfLines) {
                    break;
                }
            }
            InsertManyResult result = productsInShopsCollection.insertMany(batch);
            LOGGER.info("{} documents added to 'products_in_shops' collection!", result.getInsertedIds().size());
        }
    }
}