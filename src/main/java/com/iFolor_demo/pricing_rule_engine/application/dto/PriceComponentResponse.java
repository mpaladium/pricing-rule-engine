package com.iFolor_demo.pricing_rule_engine.application.dto;

import java.math.BigDecimal;

public record PriceComponentResponse(
    String name,
    String type,
    BigDecimal amount,
    String description
) {}
