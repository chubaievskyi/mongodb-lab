package com.chubaievskyi.dto;

import com.github.javafaker.Faker;

import java.util.Locale;

public class DTOGenerator {

    private static final String NAME = "Епіцентр №";
    private static final int MIN_COUNT = 0;
    private static final int MAX_COUNT = 10000;

    private final Faker faker;

    public DTOGenerator() {
        this.faker = new Faker(new Locale("uk"));
    }

    public ShopDTO generateRandomShop() {

        String name = NAME + faker.number().numberBetween(MIN_COUNT, MAX_COUNT);
        String city = faker.address().cityName();
        String street = faker.address().streetName();
        String number = faker.address().buildingNumber();

        return new ShopDTO(name, city, street, number);
    }

    public ProductDTO generateRandomProduct() {

        String name = faker.commerce().productName();
        String category = faker.commerce().department();

        return new ProductDTO(name, category);
    }
}
