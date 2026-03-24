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
 * 20% exclusive discount for VIP customers.
 * Non-stackable — prevents combining with other exclusive promotions.
 * Highest segment-based priority, fires before PREMIUM/NEW rules.
 */
@Component
public class VipCustomerDiscountRule implements DiscountRule {

    private static final BigDecimal DISCOUNT_PERCENT = new BigDecimal("20");

    @Override
    public String getName() { return "VIP Customer Discount (20%)"; }

    @Override
    public int getPriority() { return 80; }

    @Override
    public boolean isStackable() { return false; }

    @Override
    public boolean isApplicable(PricingContext context) {
        return context.customer().getSegment() == CustomerSegment.VIP;
    }

    @Override
    public PriceComponent apply(PricingContext context, Money currentSubtotal) {
        Money discount = currentSubtotal.percentage(DISCOUNT_PERCENT);
        return new PriceComponent(getName(), AdjustmentType.DISCOUNT, discount,
            "20% exclusive discount for VIP tier customers");
    }
}
