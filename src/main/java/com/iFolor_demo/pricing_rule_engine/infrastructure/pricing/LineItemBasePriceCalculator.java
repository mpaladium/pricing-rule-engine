package com.iFolor_demo.pricing_rule_engine.infrastructure.pricing;

import com.iFolor_demo.pricing_rule_engine.domain.service.BasePriceCalculator;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.AdjustmentType;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PriceComponent;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PricingContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LineItemBasePriceCalculator implements BasePriceCalculator {

    @Override
    public List<PriceComponent> calculate(PricingContext context) {
        List<PriceComponent> components = new ArrayList<>();
        for (var item : context.cart().getItems()) {
            components.add(new PriceComponent(
                "Base: %s x%d".formatted(item.getProductName(), item.getQuantity()),
                AdjustmentType.BASE,
                item.subtotal(),
                "Base price from line item"
            ));
        }
        return components;
    }
}
