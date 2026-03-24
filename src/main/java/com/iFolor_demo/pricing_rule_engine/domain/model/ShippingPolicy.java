package com.iFolor_demo.pricing_rule_engine.domain.model;

import java.util.Objects;

public class ShippingPolicy {

    private final String id;
    private final String name;
    private final String method;
    private final Money baseCost;
    private final Money freeShippingThreshold;

    public ShippingPolicy(String id, String name, String method, Money baseCost, Money freeShippingThreshold) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.method = Objects.requireNonNull(method);
        this.baseCost = Objects.requireNonNull(baseCost);
        this.freeShippingThreshold = freeShippingThreshold;
    }

    public boolean isFreeFor(Money orderTotal) {
        return freeShippingThreshold != null && orderTotal.isGreaterThanOrEqual(freeShippingThreshold);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getMethod() { return method; }
    public Money getBaseCost() { return baseCost; }
    public Money getFreeShippingThreshold() { return freeShippingThreshold; }
}
