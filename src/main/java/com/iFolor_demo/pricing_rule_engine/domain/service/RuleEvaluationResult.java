package com.iFolor_demo.pricing_rule_engine.domain.service;

import com.iFolor_demo.pricing_rule_engine.domain.model.Money;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PriceComponent;

import java.util.List;

public record RuleEvaluationResult(
    Money totalDiscount,
    List<PriceComponent> adjustments,
    List<String> appliedRules,
    List<String> decisionTrail
) {}
