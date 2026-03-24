package com.iFolor_demo.pricing_rule_engine.infrastructure.pricing;

import com.iFolor_demo.pricing_rule_engine.domain.service.PricingAuditLogger;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PricingContext;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PriceQuote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StructuredPricingAuditLogger implements PricingAuditLogger {

    private static final Logger log = LoggerFactory.getLogger(StructuredPricingAuditLogger.class);

    @Override
    public void log(PricingContext context, PriceQuote quote) {
        log.info(
            "pricing_audit customerId={} cartId={} subtotal={} discount={} shipping={} tax={} finalTotal={} rules={}",
            context.customer().getId(),
            context.cart().getId(),
            quote.subtotal().amount(),
            quote.totalDiscount().amount(),
            quote.shippingCost().amount(),
            quote.tax().amount(),
            quote.finalTotal().amount(),
            quote.appliedRules()
        );

        if (log.isDebugEnabled()) {
            for (String step : quote.decisionTrail()) {
                log.debug("pricing_decision_step cartId={} step={}", context.cart().getId(), step);
            }
        }
    }
}
