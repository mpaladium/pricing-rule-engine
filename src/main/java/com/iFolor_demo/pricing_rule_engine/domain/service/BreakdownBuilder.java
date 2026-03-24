package com.iFolor_demo.pricing_rule_engine.domain.service;

import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PriceComponent;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PriceQuote;

import java.util.List;

public interface BreakdownBuilder {

    PriceQuote build(
        String currency,
        List<PriceComponent> adjustments,
        List<String> appliedRules,
        List<String> decisionTrail
    );
}
