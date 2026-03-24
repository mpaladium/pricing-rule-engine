package com.iFolor_demo.pricing_rule_engine.application.dto;

import com.iFolor_demo.pricing_rule_engine.domain.model.CustomerSegment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PricingQuoteRequest(
    @NotEmpty List<@Valid CartItemRequest> items,
    @NotBlank String customerId,
    @NotNull CustomerSegment customerSegment,
    String voucherCode,
    @NotNull @Valid ShippingInfoRequest shippingInfo,
    @NotBlank String currency
) {}
