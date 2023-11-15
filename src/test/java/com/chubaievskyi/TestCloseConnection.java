package com.chubaievskyi;

import org.junit.jupiter.api.AfterAll;

class TestCloseConnection {

    @AfterAll
    public static void tearDown() {
        TestConnectionManager.close();
    }
}
