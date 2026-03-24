package com.iFolor_demo.pricing_rule_engine.domain.valueobject;

import com.iFolor_demo.pricing_rule_engine.domain.model.Money;

/**
 * A single line item in a price breakdown.
 * type is one of: BASE, DISCOUNT, SHIPPING, TAX
 * For DISCOUNT components, amount is the positive savings value.
 * For all other types, amount is the positive cost contribution.
 */
public record PriceComponent(
    String name,
    AdjustmentType type,
    Money amount,
    String description
) {}
