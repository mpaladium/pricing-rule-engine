package com.iFolor_demo.pricing_rule_engine.domain.service;

import com.iFolor_demo.pricing_rule_engine.domain.model.Money;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PriceComponent;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PricingContext;

/**
 * Pluggable shipping strategy.
 *
 * <p>Rules run in Spring {@code @Order}. The first non-null result wins.
 */
public interface ShippingRule {

    /** Name shown in quote metadata (`appliedRules`). */
    String getName();

    /** Quick pre-check before apply(). */
    boolean isApplicable(PricingContext context);

    /**
     * Calculates shipping for the current cart state.
     *
     * @param afterDiscount cart total after discounts
     * @return SHIPPING component (zero means free shipping), or {@code null} to defer to next rule
     */
    PriceComponent apply(PricingContext context, Money afterDiscount);
}
