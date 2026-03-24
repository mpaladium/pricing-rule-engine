package com.iFolor_demo.pricing_rule_engine.domain.service;

import com.iFolor_demo.pricing_rule_engine.domain.model.Money;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.AdjustmentType;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PriceComponent;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PriceQuote;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PricingContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Deterministic orchestrator for the pricing sequence.
 */
@Service
public class PricingOrchestrator {

    private final PricingRuleEngine ruleEngine;
    private final BasePriceCalculator basePriceCalculator;
    private final VoucherCalculator voucherCalculator;
    private final BreakdownBuilder breakdownBuilder;
    private final PricingAuditLogger pricingAuditLogger;
    private final List<ShippingRule> shippingRules;

    public PricingOrchestrator(
        PricingRuleEngine ruleEngine,
        BasePriceCalculator basePriceCalculator,
        VoucherCalculator voucherCalculator,
        BreakdownBuilder breakdownBuilder,
        PricingAuditLogger pricingAuditLogger,
        List<ShippingRule> shippingRules
    ) {
        this.ruleEngine = ruleEngine;
        this.basePriceCalculator = basePriceCalculator;
        this.voucherCalculator = voucherCalculator;
        this.breakdownBuilder = breakdownBuilder;
        this.pricingAuditLogger = pricingAuditLogger;
        this.shippingRules = List.copyOf(shippingRules);
    }

    public PriceQuote calculate(PricingContext context) {
        String currency = context.cart().getCurrency();
        Money baseSubtotal = context.cart().subtotal();

        List<PriceComponent> adjustments = new ArrayList<>();
        List<String> appliedRules = new ArrayList<>();
        List<String> decisionTrail = new ArrayList<>();

        adjustments.addAll(basePriceCalculator.calculate(context));
        decisionTrail.add("BASE_PRICE_COMPUTED");

        RuleEvaluationResult ruleEvaluation = ruleEngine.evaluate(context, baseSubtotal);
        adjustments.addAll(ruleEvaluation.adjustments());
        appliedRules.addAll(ruleEvaluation.appliedRules());
        decisionTrail.addAll(ruleEvaluation.decisionTrail());

        Money amountAfterRules = baseSubtotal
            .subtract(ruleEvaluation.totalDiscount());
        if (amountAfterRules.isNegative()) {
            amountAfterRules = Money.zero(currency);
        }

        PriceComponent voucherAdjustment = voucherCalculator.calculate(context, amountAfterRules);
        if (voucherAdjustment != null && voucherAdjustment.type() == AdjustmentType.DISCOUNT) {
            adjustments.add(voucherAdjustment);
            appliedRules.add(voucherAdjustment.name());
            decisionTrail.add("VOUCHER_APPLIED: " + voucherAdjustment.name());
            amountAfterRules = amountAfterRules.subtract(voucherAdjustment.amount());
            if (amountAfterRules.isNegative()) {
                amountAfterRules = Money.zero(currency);
            }
        } else {
            decisionTrail.add("VOUCHER_SKIPPED");
        }

        PriceComponent shippingAdjustment = evaluateShipping(context, amountAfterRules);
        adjustments.add(shippingAdjustment);
        appliedRules.add(shippingAdjustment.name());
        decisionTrail.add("SHIPPING_APPLIED: " + shippingAdjustment.name());

        PriceQuote quote = breakdownBuilder.build(currency, adjustments, appliedRules, decisionTrail);
        pricingAuditLogger.log(context, quote);
        return quote;
    }

    private PriceComponent evaluateShipping(PricingContext context, Money afterDiscount) {
        for (ShippingRule rule : shippingRules) {
            if (rule.isApplicable(context) == false) {
                continue;
            }
            PriceComponent shipping = rule.apply(context, afterDiscount);
            if (shipping != null && shipping.type() == AdjustmentType.SHIPPING) {
                return shipping;
            }
        }
        return new PriceComponent(
            "No Shipping Rule Matched",
            AdjustmentType.SHIPPING,
            Money.zero(afterDiscount.currency()),
            "Fallback shipping of zero when no rule matches"
        );
    }
}
