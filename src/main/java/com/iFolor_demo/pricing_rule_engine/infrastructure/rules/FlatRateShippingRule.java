package com.iFolor_demo.pricing_rule_engine.infrastructure.rules;

import com.iFolor_demo.pricing_rule_engine.domain.model.Money;
import com.iFolor_demo.pricing_rule_engine.domain.service.ShippingRule;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.AdjustmentType;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PriceComponent;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PricingContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Fallback flat-rate shipping of $5.99 applied when no higher-priority
 * shipping rule fires (e.g. free shipping threshold was not reached).
 *
 * <p>Always returns a non-null component, ensuring every order gets a
 * shipping line item in the breakdown.
 */
@Component
@Order(2)
public class FlatRateShippingRule implements ShippingRule {

    private static final BigDecimal FLAT_RATE = new BigDecimal("5.99");

    @Override
    public String getName() { return "Standard Flat-Rate Shipping ($5.99)"; }

    @Override
    public boolean isApplicable(PricingContext context) {
        return true;
    }

    @Override
    public PriceComponent apply(PricingContext context, Money afterDiscount) {
        Money shippingCost = Money.of(FLAT_RATE, afterDiscount.currency());
        return new PriceComponent(getName(), AdjustmentType.SHIPPING, shippingCost,
            "Standard flat-rate shipping fee");
    }
}
