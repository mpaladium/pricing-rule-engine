package com.iFolor_demo.pricing_rule_engine.infrastructure.rules;

import com.iFolor_demo.pricing_rule_engine.domain.model.Money;
import com.iFolor_demo.pricing_rule_engine.domain.model.VoucherType;
import com.iFolor_demo.pricing_rule_engine.domain.service.VoucherCalculator;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.AdjustmentType;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PriceComponent;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PricingContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Voucher calculation module.
 *
 * <p>Supported codes:
 * <ul>
 *   <li>SAVE10 — 10% off</li>
 *   <li>SAVE15 — 15% off</li>
 *   <li>FLAT20 — $20 flat off</li>
 *   <li>FLAT50 — $50 flat off</li>
 * </ul>
 */
@Component
public class VoucherDiscountRule implements VoucherCalculator {

    private record VoucherDef(VoucherType type, BigDecimal value) {}

    private static final Map<String, VoucherDef> VOUCHERS = Map.of(
        "SAVE10",  new VoucherDef(VoucherType.PERCENTAGE,  new BigDecimal("10")),
        "SAVE15",  new VoucherDef(VoucherType.PERCENTAGE,  new BigDecimal("15")),
        "FLAT20",  new VoucherDef(VoucherType.FLAT_AMOUNT, new BigDecimal("20.00")),
        "FLAT50",  new VoucherDef(VoucherType.FLAT_AMOUNT, new BigDecimal("50.00"))
    );

    @Override
    public PriceComponent calculate(PricingContext context, Money cartAmountAfterRules) {
        String code = context.voucherCode();
        if (code == null || code.isBlank()) {
            return null;
        }

        VoucherDef voucher = VOUCHERS.get(code.toUpperCase());
        if (voucher == null) {
            return null;
        }

        Money discount;
        String description;

        switch (voucher.type()) {
            case PERCENTAGE -> {
                discount = cartAmountAfterRules.percentage(voucher.value());
                description = "Voucher %s: %s%% off".formatted(
                    context.voucherCode().toUpperCase(), voucher.value().stripTrailingZeros().toPlainString());
            }
            case FLAT_AMOUNT -> {
                discount = Money.of(voucher.value(), cartAmountAfterRules.currency()).min(cartAmountAfterRules);
                description = "Voucher %s: $%s off".formatted(
                    context.voucherCode().toUpperCase(), voucher.value().stripTrailingZeros().toPlainString());
            }
            default -> { return null; }
        }

        return new PriceComponent("Voucher: " + context.voucherCode().toUpperCase(),
            AdjustmentType.DISCOUNT, discount, description);
    }
}
