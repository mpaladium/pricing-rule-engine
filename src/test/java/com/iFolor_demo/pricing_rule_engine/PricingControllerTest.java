package com.iFolor_demo.pricing_rule_engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PricingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Cart: 2x $50 Widget = $100 subtotal
    // Segment: PREMIUM → 10% off ($10) + Bulk ($100 >= $100 threshold) → 5% off ($5) = $15 total discount
    // After discount: $85. Free shipping kicks in ($85 >= $75).
    // Final: $85.00
    @Test
    void premiumCustomerReceivesSegmentAndBulkDiscount() throws Exception {
        var request = buildRequest("PREMIUM", null, 50.0, 2);

        mockMvc.perform(post("/api/pricing/quote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.subtotal").value(100.00))
            .andExpect(jsonPath("$.totalDiscount").value(15.00))
            .andExpect(jsonPath("$.shippingCost").value(0.00))
            .andExpect(jsonPath("$.totalSurcharge").doesNotExist())
            .andExpect(jsonPath("$.tax").value(0.00))
            .andExpect(jsonPath("$.finalTotal").value(85.00))
            .andExpect(jsonPath("$.currency").value("USD"))
            .andExpect(jsonPath("$.decisionTrail").isArray())
            .andExpect(jsonPath("$.appliedRules").isArray())
            .andExpect(jsonPath("$.breakdown").isArray());
    }

    // VIP discount is non-stackable (20% off). Voucher is also non-stackable but VIP fires first.
    // Cart: 1x $50 = $50 subtotal. VIP 20% = $10 off. After: $40. Flat shipping ($40 < $75).
    // Final: $40 + $5.99 = $45.99
    @Test
    void vipCustomerReceivesExclusiveDiscount() throws Exception {
        var request = buildRequest("VIP", null, 50.0, 1);

        mockMvc.perform(post("/api/pricing/quote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.subtotal").value(50.00))
            .andExpect(jsonPath("$.totalDiscount").value(10.00))
            .andExpect(jsonPath("$.shippingCost").value(5.99))
            .andExpect(jsonPath("$.tax").value(0.00))
            .andExpect(jsonPath("$.finalTotal").value(45.99));
    }

    // SAVE10 voucher = 10% off on $60 = $6. After: $54. Flat shipping.
    // Final: $54 + $5.99 = $59.99
    @Test
    void voucherSave10AppliesPercentageDiscount() throws Exception {
        var request = buildRequest("REGULAR", "SAVE10", 60.0, 1);

        mockMvc.perform(post("/api/pricing/quote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.subtotal").value(60.00))
            .andExpect(jsonPath("$.totalDiscount").value(6.00))
            .andExpect(jsonPath("$.shippingCost").value(5.99))
            .andExpect(jsonPath("$.tax").value(0.00))
            .andExpect(jsonPath("$.finalTotal").value(59.99));
    }

    // FREESHIP voucher = free shipping regardless of order total.
    // Cart: $30. No other discounts. Final: $30.00.
    @Test
    void freeshipVoucherGrantsFreeShipping() throws Exception {
        var request = buildRequest("REGULAR", "FREESHIP", 30.0, 1);

        mockMvc.perform(post("/api/pricing/quote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.subtotal").value(30.00))
            .andExpect(jsonPath("$.totalDiscount").value(0.00))
            .andExpect(jsonPath("$.shippingCost").value(0.00))
            .andExpect(jsonPath("$.tax").value(0.00))
            .andExpect(jsonPath("$.finalTotal").value(30.00));
    }

    // NEW customer welcome $5 flat off. Cart: $20. After: $15. Flat shipping.
    // Final: $15 + $5.99 = $20.99
    @Test
    void newCustomerReceivesWelcomeDiscount() throws Exception {
        var request = buildRequest("NEW", null, 20.0, 1);

        mockMvc.perform(post("/api/pricing/quote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.subtotal").value(20.00))
            .andExpect(jsonPath("$.totalDiscount").value(5.00))
            .andExpect(jsonPath("$.shippingCost").value(5.99))
            .andExpect(jsonPath("$.tax").value(0.00))
            .andExpect(jsonPath("$.finalTotal").value(20.99));
    }

    // Tax module removed: country does not affect total at this stage.
    // Cart: $30, no discounts, shipping: $5.99.
    // Final: $35.99
    @Test
    void germanyOrderDoesNotApplyTax() throws Exception {
        var request = buildRequest("REGULAR", null, 30.0, 1, "DE");

        mockMvc.perform(post("/api/pricing/quote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.subtotal").value(30.00))
            .andExpect(jsonPath("$.totalDiscount").value(0.00))
            .andExpect(jsonPath("$.shippingCost").value(5.99))
            .andExpect(jsonPath("$.tax").value(0.00))
            .andExpect(jsonPath("$.finalTotal").value(35.99));
    }

    @Test
    void missingItemsReturns400() throws Exception {
        var request = Map.of(
            "customerId", "cust-1",
            "customerSegment", "REGULAR",
            "shippingInfo", Map.of("method", "STANDARD", "country", "US"),
            "currency", "USD"
        );

        mockMvc.perform(post("/api/pricing/quote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    private Map<String, Object> buildRequest(String segment, String voucherCode,
                                              double unitPrice, int quantity) {
        return buildRequest(segment, voucherCode, unitPrice, quantity, "US");
    }

    private Map<String, Object> buildRequest(String segment, String voucherCode,
                                             double unitPrice, int quantity, String country) {
        var items = List.of(Map.of(
            "productId", "prod-1",
            "productName", "Test Product",
            "category", "General",
            "unitPrice", unitPrice,
            "quantity", quantity
        ));

        var shipping = Map.of("method", "STANDARD", "country", country, "postalCode", "10001");

        var req = new java.util.HashMap<String, Object>();
        req.put("items", items);
        req.put("customerId", "cust-test");
        req.put("customerSegment", segment);
        req.put("shippingInfo", shipping);
        req.put("currency", "USD");
        if (voucherCode != null) req.put("voucherCode", voucherCode);
        return req;
    }
}
