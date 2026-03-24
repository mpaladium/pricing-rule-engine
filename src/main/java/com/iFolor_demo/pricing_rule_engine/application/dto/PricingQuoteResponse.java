package com.iFolor_demo.pricing_rule_engine.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record PricingQuoteResponse(
    BigDecimal subtotal,
    BigDecimal totalDiscount,
    BigDecimal shippingCost,
    BigDecimal tax,
    BigDecimal finalTotal,
    String currency,
    List<String> appliedRules,
    List<String> decisionTrail,
    List<PriceComponentResponse> breakdown
) {}
