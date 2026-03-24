package com.iFolor_demo.pricing_rule_engine.domain.model;

import java.util.Objects;

public class Product {

    private final String id;
    private final String name;
    private final String category;
    private final Money basePrice;

    public Product(String id, String name, String category, Money basePrice) {
        this.id = Objects.requireNonNull(id, "Product id cannot be null");
        this.name = Objects.requireNonNull(name, "Product name cannot be null");
        this.category = Objects.requireNonNull(category, "Product category cannot be null");
        this.basePrice = Objects.requireNonNull(basePrice, "Product basePrice cannot be null");
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public Money getBasePrice() { return basePrice; }
}
