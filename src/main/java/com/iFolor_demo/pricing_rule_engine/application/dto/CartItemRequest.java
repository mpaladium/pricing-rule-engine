package com.iFolor_demo.pricing_rule_engine.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CartItemRequest(
    @NotBlank String productId,
    @NotBlank String productName,
    @NotBlank String category,
    @NotNull @DecimalMin("0.01") BigDecimal unitPrice,
    @Min(1) int quantity
) {}
