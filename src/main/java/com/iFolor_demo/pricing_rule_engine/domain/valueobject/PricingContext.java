package com.iFolor_demo.pricing_rule_engine.domain.valueobject;

import com.iFolor_demo.pricing_rule_engine.domain.model.Cart;
import com.iFolor_demo.pricing_rule_engine.domain.model.Customer;

import java.util.Objects;

/**
 * Immutable snapshot of everything needed to compute a price quote.
 * Passed unchanged through the entire pricing pipeline.
 */
public record PricingContext(
    Cart cart,
    Customer customer,
    String voucherCode,
    ShippingInfo shippingInfo
) {
    public PricingContext {
        Objects.requireNonNull(cart, "Cart is required");
        Objects.requireNonNull(customer, "Customer is required");
        Objects.requireNonNull(shippingInfo, "ShippingInfo is required");
    }
}
