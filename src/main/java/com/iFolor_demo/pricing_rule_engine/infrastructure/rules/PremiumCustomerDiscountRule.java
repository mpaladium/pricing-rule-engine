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
 * 10% stackable loyalty discount for PREMIUM customers.
 * Stackable — combines freely with volume discounts and other stackable rules.
 */
@Component
public class PremiumCustomerDiscountRule implements DiscountRule {

    private static final BigDecimal DISCOUNT_PERCENT = new BigDecimal("10");

    @Override
    public String getName() { return "Premium Customer Discount (10%)"; }

    @Override
    public int getPriority() { return 50; }

    @Override
    public boolean isStackable() { return true; }

    @Override
    public boolean isApplicable(PricingContext context) {
        return context.customer().getSegment() == CustomerSegment.PREMIUM;
    }

    @Override
    public PriceComponent apply(PricingContext context, Money currentSubtotal) {
        Money discount = currentSubtotal.percentage(DISCOUNT_PERCENT);
        return new PriceComponent(getName(), AdjustmentType.DISCOUNT, discount,
            "10% loyalty discount for Premium tier customers");
    }
}
