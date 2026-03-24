package com.iFolor_demo.pricing_rule_engine.domain.service;

import com.iFolor_demo.pricing_rule_engine.domain.model.Money;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PriceComponent;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PricingContext;

public interface VoucherCalculator {

    PriceComponent calculate(PricingContext context, Money cartAmountAfterRules);
}
