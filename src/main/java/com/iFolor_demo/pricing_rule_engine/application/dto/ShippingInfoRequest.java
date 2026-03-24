package com.iFolor_demo.pricing_rule_engine.application.dto;

import jakarta.validation.constraints.NotBlank;

public record ShippingInfoRequest(
    @NotBlank String method,
    @NotBlank String country,
    String postalCode
) {}
