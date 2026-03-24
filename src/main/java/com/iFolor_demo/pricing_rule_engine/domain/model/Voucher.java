package com.iFolor_demo.pricing_rule_engine.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Voucher {

    private final String code;
    private final VoucherType type;
    private final BigDecimal value;
    private final LocalDate expiryDate;
    private final boolean active;

    public Voucher(String code, VoucherType type, BigDecimal value, LocalDate expiryDate, boolean active) {
        this.code = Objects.requireNonNull(code, "Voucher code cannot be null");
        this.type = Objects.requireNonNull(type, "Voucher type cannot be null");
        this.value = Objects.requireNonNull(value, "Voucher value cannot be null");
        this.expiryDate = expiryDate;
        this.active = active;
    }

    public boolean isValid() {
        return active && (expiryDate == null || !LocalDate.now().isAfter(expiryDate));
    }

    public String getCode() { return code; }
    public VoucherType getType() { return type; }
    public BigDecimal getValue() { return value; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public boolean isActive() { return active; }
}
