package com.iFolor_demo.pricing_rule_engine.domain.valueobject;

import com.iFolor_demo.pricing_rule_engine.domain.model.Money;

import java.util.List;

/**
 * The complete, self-contained result of a pricing calculation.
 * finalTotal = subtotal - totalDiscount + shippingCost + tax
 */
public record PriceQuote(
    Money subtotal,
    Money totalDiscount,
    Money shippingCost,
    Money tax,
    Money finalTotal,
    List<PriceComponent> breakdown,
    List<String> appliedRules,
    List<String> decisionTrail
) {}
