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
 * Grants free shipping when:
 * <ul>
 *   <li>The post-discount total is >= $75, OR</li>
 *   <li>The voucher code is FREESHIP</li>
 * </ul>
 *
 * <p>Evaluated before {@link FlatRateShippingRule} ({@code @Order(1)}).
 * Returns {@code null} when neither condition is met, allowing the next
 * shipping rule to provide a cost.
 */
@Component
@Order(1)
public class FreeShippingThresholdRule implements ShippingRule {

    private static final BigDecimal FREE_THRESHOLD = new BigDecimal("75.00");

    @Override
    public String getName() { return "Free Shipping on Orders Over $75"; }

    @Override
    public boolean isApplicable(PricingContext context) {
        return true;
    }

    @Override
    public PriceComponent apply(PricingContext context, Money afterDiscount) {
        boolean qualifies = afterDiscount.amount().compareTo(FREE_THRESHOLD) >= 0
            || "FREESHIP".equalsIgnoreCase(context.voucherCode());

        if (!qualifies) return null;

        String reason = "FREESHIP".equalsIgnoreCase(context.voucherCode())
            ? "Free shipping via FREESHIP voucher"
            : "Free shipping on orders over $75";

        return new PriceComponent(getName(), AdjustmentType.SHIPPING, Money.zero(afterDiscount.currency()), reason);
    }
}
