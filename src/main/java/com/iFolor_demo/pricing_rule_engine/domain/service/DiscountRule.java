package com.iFolor_demo.pricing_rule_engine.domain.service;

import com.iFolor_demo.pricing_rule_engine.domain.model.Money;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PriceComponent;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PricingContext;

/**
 * Pluggable discount rule.
 *
 * <p>Implementations are discovered by Spring and evaluated by {@link PricingRuleEngine}
 * in descending {@link #getPriority()} order.
 */
public interface DiscountRule {

    /** Name shown in quote metadata (`appliedRules`). */
    String getName();

    /** Higher value runs first. */
    int getPriority();

    /**
     * Whether this rule can combine with other exclusive rules.
     * Return {@code false} for promotions that must be applied alone.
     */
    boolean isStackable();

    /** Cheap pre-check before applying the rule. */
    boolean isApplicable(PricingContext context);

    /**
     * Creates the discount component for this rule.
     *
     * @param currentSubtotal original cart subtotal before rule discounts
     * @return DISCOUNT component with positive savings, or {@code null} when not applicable
     */
    PriceComponent apply(PricingContext context, Money currentSubtotal);
}
