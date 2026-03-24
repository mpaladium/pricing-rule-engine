package com.iFolor_demo.pricing_rule_engine.infrastructure.rules;

import com.iFolor_demo.pricing_rule_engine.domain.model.Money;
import com.iFolor_demo.pricing_rule_engine.domain.service.DiscountRule;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.AdjustmentType;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PriceComponent;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PricingContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 5% volume discount applied when the cart subtotal reaches $100 or more.
 * Stackable — designed to combine with segment and voucher discounts.
 *
 * <p>To adjust the threshold or rate, externalise THRESHOLD_AMOUNT and
 * DISCOUNT_PERCENT into application configuration (e.g. application.yml).
 */
@Component
public class BulkPurchaseDiscountRule implements DiscountRule {

    private static final BigDecimal THRESHOLD_AMOUNT = new BigDecimal("100.00");
    private static final BigDecimal DISCOUNT_PERCENT = new BigDecimal("5");

    @Override
    public String getName() { return "Bulk Purchase Discount (5% on orders over $100)"; }

    @Override
    public int getPriority() { return 20; }

    @Override
    public boolean isStackable() { return true; }

    @Override
    public boolean isApplicable(PricingContext context) {
        return context.cart().subtotal().amount().compareTo(THRESHOLD_AMOUNT) >= 0;
    }

    @Override
    public PriceComponent apply(PricingContext context, Money currentSubtotal) {
        Money discount = currentSubtotal.percentage(DISCOUNT_PERCENT);
        return new PriceComponent(getName(), AdjustmentType.DISCOUNT, discount,
            "5% volume discount for orders of $100 or more");
    }
}
