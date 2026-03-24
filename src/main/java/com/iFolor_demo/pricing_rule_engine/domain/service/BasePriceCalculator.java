package com.iFolor_demo.pricing_rule_engine.domain.service;

import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PriceComponent;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PricingContext;

import java.util.List;

public interface BasePriceCalculator {

    List<PriceComponent> calculate(PricingContext context);
}
