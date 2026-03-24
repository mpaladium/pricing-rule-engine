package com.iFolor_demo.pricing_rule_engine.domain.valueobject;

public record ShippingInfo(
    String method,
    String country,
    String postalCode
) {
    public ShippingInfo {
        if (method == null || method.isBlank()) throw new IllegalArgumentException("Shipping method is required");
        if (country == null || country.isBlank()) throw new IllegalArgumentException("Country is required");
    }
}
