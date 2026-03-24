package com.iFolor_demo.pricing_rule_engine.domain.service;

import com.iFolor_demo.pricing_rule_engine.domain.model.Money;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.AdjustmentType;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PriceComponent;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PricingContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Evaluates discount rules deterministically in priority order.
 */
@Component
public class PricingRuleEngine {

    private final List<DiscountRule> discountRules;

    public PricingRuleEngine(List<DiscountRule> discountRules) {
        this.discountRules = discountRules.stream()
            .sorted(Comparator.comparingInt(DiscountRule::getPriority).reversed())
            .toList();
    }

    public RuleEvaluationResult evaluate(PricingContext context, Money baseSubtotal) {
        String currency = baseSubtotal.currency();
        List<PriceComponent> adjustments = new ArrayList<>();
        List<String> appliedRules = new ArrayList<>();
        List<String> decisionTrail = new ArrayList<>();

        Money totalDiscount = Money.zero(currency);
        boolean exclusiveApplied = false;
        for (DiscountRule rule : discountRules) {
            if (rule.isApplicable(context) == false) {
                continue;
            }
            if (exclusiveApplied && rule.isStackable() == false) {
                continue;
            }

            PriceComponent discount = rule.apply(context, baseSubtotal);
            if (discount == null || discount.type() != AdjustmentType.DISCOUNT) {
                continue;
            }
            adjustments.add(discount);
            totalDiscount = totalDiscount.add(discount.amount());
            appliedRules.add(rule.getName());
            decisionTrail.add("DISCOUNT_RULE_APPLIED: " + rule.getName());
            if (rule.isStackable() == false) {
                exclusiveApplied = true;
            }
        }

        return new RuleEvaluationResult(
            totalDiscount,
            List.copyOf(adjustments),
            List.copyOf(appliedRules),
            List.copyOf(decisionTrail)
        );
    }
}
