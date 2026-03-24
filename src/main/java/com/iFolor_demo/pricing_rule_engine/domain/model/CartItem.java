package com.iFolor_demo.pricing_rule_engine.domain.model;

import java.util.Objects;

public class CartItem {

    private final String productId;
    private final String productName;
    private final String category;
    private final Money unitPrice;
    private final int quantity;

    public CartItem(String productId, String productName, String category, Money unitPrice, int quantity) {
        this.productId = Objects.requireNonNull(productId, "productId cannot be null");
        this.productName = Objects.requireNonNull(productName, "productName cannot be null");
        this.category = Objects.requireNonNull(category, "category cannot be null");
        this.unitPrice = Objects.requireNonNull(unitPrice, "unitPrice cannot be null");
        if (quantity < 1) throw new IllegalArgumentException("Quantity must be at least 1");
        this.quantity = quantity;
    }

    public Money subtotal() {
        return unitPrice.multiply(quantity);
    }

    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getCategory() { return category; }
    public Money getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }
}
