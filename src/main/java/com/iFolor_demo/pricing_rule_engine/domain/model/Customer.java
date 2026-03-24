package com.iFolor_demo.pricing_rule_engine.domain.model;

import java.util.Objects;

public class Customer {

    private final String id;
    private final String name;
    private final CustomerSegment segment;

    public Customer(String id, String name, CustomerSegment segment) {
        this.id = Objects.requireNonNull(id, "Customer id cannot be null");
        this.name = Objects.requireNonNull(name, "Customer name cannot be null");
        this.segment = Objects.requireNonNull(segment, "Customer segment cannot be null");
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public CustomerSegment getSegment() { return segment; }
}
