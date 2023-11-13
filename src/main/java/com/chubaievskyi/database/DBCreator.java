package com.chubaievskyi.database;

import com.chubaievskyi.util.ConnectionManager;
import com.chubaievskyi.util.InputReader;
import com.chubaievskyi.util.RandomDataPlaceholder;
import com.chubaievskyi.util.ValueGenerator;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DBCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBCreator.class);
    private static final InputReader INPUT_READER = InputReader.getInstance();
    private static final int NUMBER_OF_LINES = INPUT_READER.getTotalNumberOfLines();
    private static final int NUMBER_OF_THREADS = INPUT_READER.getNumberOfThreads();
    private final AtomicInteger rowCounter = new AtomicInteger(0);
    private final MongoDatabase database = ConnectionManager.getDatabase();

    public void run() {
        LOGGER.info("Method run() class DBCreator start!");

        createCollectionsAndValue();

        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        long startTimeExecutor = System.currentTimeMillis();
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            RandomDataPlaceholder randomDataPlaceholder = new RandomDataPlaceholder(NUMBER_OF_LINES, rowCounter, database);
            executor.submit(randomDataPlaceholder);
        }

        shutdownAndAwaitTermination(executor);
        LOGGER.info("{} rows of data added!", rowCounter.get());
        long endTimeExecutor = System.currentTimeMillis();

        printResult(startTimeExecutor, endTimeExecutor);
    }

    private void createCollectionsAndValue() {

        CollectionsGenerator collectionsGenerator = new CollectionsGenerator();
        collectionsGenerator.createCollections(database);

        ValueGenerator valueGenerator = new ValueGenerator();
        valueGenerator.generateValue();
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

    private void printResult(long startTimeExecutor, long endTimeExecutor) {
        double executorTime = (double) (endTimeExecutor - startTimeExecutor) / 1000;
        LOGGER.info("Product generation time in stores - (sec) - {}", executorTime);
        LOGGER.info("Number of generate lines - {}", NUMBER_OF_LINES);

        double averageGenerateSpeed = NUMBER_OF_LINES / executorTime;
        String formattedAverageGenerateSpeed = String.format("%.2f", averageGenerateSpeed);
        LOGGER.info("Average speed of generate lines (lines per second) - {}", formattedAverageGenerateSpeed);
    }
}
