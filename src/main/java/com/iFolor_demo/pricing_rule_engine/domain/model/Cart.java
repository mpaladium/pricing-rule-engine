package com.iFolor_demo.pricing_rule_engine.domain.model;

import java.util.List;
import java.util.Objects;

public class Cart {

    private final String id;
    private final List<CartItem> items;
    private final String currency;

    public Cart(String id, List<CartItem> items, String currency) {
        this.id = Objects.requireNonNull(id, "Cart id cannot be null");
        this.items = List.copyOf(Objects.requireNonNull(items, "Items cannot be null"));
        this.currency = Objects.requireNonNull(currency, "Currency cannot be null");
        if (items.isEmpty()) throw new IllegalArgumentException("Cart must have at least one item");
    }

    public Money subtotal() {
        return items.stream()
            .map(CartItem::subtotal)
            .reduce(Money.zero(currency), Money::add);
    }

    public String getId() { return id; }
    public List<CartItem> getItems() { return items; }
    public String getCurrency() { return currency; }
}
