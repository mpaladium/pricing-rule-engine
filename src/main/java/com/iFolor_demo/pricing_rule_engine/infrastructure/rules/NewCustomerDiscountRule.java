package com.iFolor_demo.pricing_rule_engine.infrastructure.rules;

import com.iFolor_demo.pricing_rule_engine.domain.model.CustomerSegment;
import com.iFolor_demo.pricing_rule_engine.domain.model.Money;
import com.iFolor_demo.pricing_rule_engine.domain.service.DiscountRule;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.AdjustmentType;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PriceComponent;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PricingContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * $5 flat welcome discount for first-time (NEW segment) customers.
 * Stackable — can combine with volume discounts.
 * Capped at the cart subtotal to avoid negative totals.
 */
@Component
public class NewCustomerDiscountRule implements DiscountRule {

    private static final BigDecimal FLAT_DISCOUNT = new BigDecimal("5.00");

    @Override
    public String getName() { return "New Customer Welcome Discount ($5 off)"; }

    @Override
    public int getPriority() { return 30; }

    @Override
    public boolean isStackable() { return true; }

    @Override
    public boolean isApplicable(PricingContext context) {
        return context.customer().getSegment() == CustomerSegment.NEW;
    }

    @Override
    public PriceComponent apply(PricingContext context, Money currentSubtotal) {
        Money discount = Money.of(FLAT_DISCOUNT, currentSubtotal.currency()).min(currentSubtotal);
        return new PriceComponent(getName(), AdjustmentType.DISCOUNT, discount,
            "Welcome discount for first-time customers");
    }
}
