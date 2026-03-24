package com.iFolor_demo.pricing_rule_engine.domain.service;

import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PriceQuote;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PricingContext;

public interface PricingAuditLogger {

    void log(PricingContext context, PriceQuote quote);
}
