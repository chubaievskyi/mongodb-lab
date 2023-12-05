package com.chubaievskyi.database;

import com.chubaievskyi.util.ConnectionManager;
import com.chubaievskyi.util.InputReader;
import com.chubaievskyi.util.RandomDataPlaceholder;
import com.chubaievskyi.util.ValueGenerator;
import com.mongodb.client.MongoDatabase;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DBCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBCreator.class);
    private static final InputReader INPUT_READER = InputReader.getInstance();
    private static final int NUMBER_OF_LINES = INPUT_READER.getTotalNumberOfLines();
    private static final int NUMBER_OF_THREADS = INPUT_READER.getNumberOfThreads();
    private static final int NUMBER_OF_SHOPS = INPUT_READER.getNumberOfShops();
    private static final int NUMBER_OF_PRODUCTS = INPUT_READER.getNumberOfProduct();
    private final AtomicInteger counter = new AtomicInteger(0);
    private final MongoDatabase database = ConnectionManager.getDatabase();
    private final Validator validator = initializeValidator();

    private List<Document> shopsData;
    private List<Document> productData;

    public void run() {
        LOGGER.info("Method run() class DBCreator start!");
        createCollectionsAndValue();

        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        long startTimeExecutor = System.currentTimeMillis();
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            executor.submit(new RandomDataPlaceholder(NUMBER_OF_LINES, counter, database, shopsData, productData));
        }

        shutdownAndAwaitTermination(executor);
        LOGGER.info("{} rows of data added!", counter.get());
        long endTimeExecutor = System.currentTimeMillis();

        printResult(startTimeExecutor, endTimeExecutor);
    }

    private void createCollectionsAndValue() {
        new CollectionGenerator().createCollections(database);
        shopsData = new ValueGenerator().generateShopValue(database, validator, NUMBER_OF_SHOPS);
        productData = new ValueGenerator().generateProductValue(database, validator, NUMBER_OF_PRODUCTS);
    }

    private void shutdownAndAwaitTermination(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination( Long.MAX_VALUE, TimeUnit.SECONDS)) {
                LOGGER.error("Not all executor threads have terminated.");
            }
        } catch (InterruptedException e) {
            LOGGER.debug("Executor service interrupted for executor threads.", e);
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private Validator initializeValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            return factory.getValidator();
        }
    }

    private void printResult(long startTimeExecutor, long endTimeExecutor) {
        double executorTime = (double) (endTimeExecutor - startTimeExecutor) / 1000;
        LOGGER.info("Product generation time in stores - (sec) - {}", executorTime);

        double averageGenerateSpeed = NUMBER_OF_LINES / executorTime;
        String formattedAverageGenerateSpeed = String.format("%.2f", averageGenerateSpeed);
        LOGGER.info("Average speed of generate lines (lines per second) - {}", formattedAverageGenerateSpeed);
    }
}