package com.iFolor_demo.pricing_rule_engine.infrastructure.pricing;

import com.iFolor_demo.pricing_rule_engine.domain.model.Money;
import com.iFolor_demo.pricing_rule_engine.domain.service.BreakdownBuilder;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.AdjustmentType;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PriceComponent;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PriceQuote;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultBreakdownBuilder implements BreakdownBuilder {

    @Override
    public PriceQuote build(
        String currency,
        List<PriceComponent> adjustments,
        List<String> appliedRules,
        List<String> decisionTrail
    ) {
        Money subtotal = sum(adjustments, AdjustmentType.BASE);
        Money totalDiscount = sum(adjustments, AdjustmentType.DISCOUNT);
        Money shippingCost = sum(adjustments, AdjustmentType.SHIPPING);
        Money tax = sum(adjustments, AdjustmentType.TAX);

        Money finalTotal = subtotal
            .subtract(totalDiscount)
            .add(shippingCost)
            .add(tax);
        if (finalTotal.isNegative()) {
            finalTotal = Money.zero(currency);
        }

        return new PriceQuote(
            subtotal,
            totalDiscount,
            shippingCost,
            tax,
            finalTotal,
            List.copyOf(adjustments),
            List.copyOf(appliedRules),
            List.copyOf(decisionTrail)
        );
    }

    private Money sum(List<PriceComponent> adjustments, AdjustmentType type) {
        if (adjustments.isEmpty()) {
            throw new IllegalArgumentException("Adjustments cannot be empty");
        }
        String currency = adjustments.getFirst().amount().currency();
        return adjustments.stream()
            .filter(c -> c.type() == type)
            .map(PriceComponent::amount)
            .reduce(Money.zero(currency), Money::add);
    }
}
