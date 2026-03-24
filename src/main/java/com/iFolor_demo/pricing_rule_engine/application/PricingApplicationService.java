package com.iFolor_demo.pricing_rule_engine.application;

import com.iFolor_demo.pricing_rule_engine.application.dto.CartItemRequest;
import com.iFolor_demo.pricing_rule_engine.application.dto.PriceComponentResponse;
import com.iFolor_demo.pricing_rule_engine.application.dto.PricingQuoteRequest;
import com.iFolor_demo.pricing_rule_engine.application.dto.PricingQuoteResponse;
import com.iFolor_demo.pricing_rule_engine.domain.model.Cart;
import com.iFolor_demo.pricing_rule_engine.domain.model.CartItem;
import com.iFolor_demo.pricing_rule_engine.domain.model.Customer;
import com.iFolor_demo.pricing_rule_engine.domain.model.Money;
import com.iFolor_demo.pricing_rule_engine.domain.service.PricingOrchestrator;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PriceQuote;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.PricingContext;
import com.iFolor_demo.pricing_rule_engine.domain.valueobject.ShippingInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Maps API DTOs to domain objects, delegates pricing to the orchestrator,
 * and maps the result back to response DTOs.
 */
@Service
public class PricingApplicationService {

    private final PricingOrchestrator pricingOrchestrator;

    public PricingApplicationService(PricingOrchestrator pricingOrchestrator) {
        this.pricingOrchestrator = pricingOrchestrator;
    }

    public PricingQuoteResponse calculateQuote(PricingQuoteRequest request) {
        String currency = request.currency();

        List<CartItem> cartItems = request.items().stream()
            .map(item -> toCartItem(item, currency))
            .toList();

        Cart cart = new Cart(UUID.randomUUID().toString(), cartItems, currency);
        Customer customer = new Customer(request.customerId(), request.customerId(), request.customerSegment());
        ShippingInfo shippingInfo = new ShippingInfo(
            request.shippingInfo().method(),
            request.shippingInfo().country(),
            request.shippingInfo().postalCode()
        );

        PricingContext context = new PricingContext(cart, customer, request.voucherCode(), shippingInfo);
        PriceQuote quote = pricingOrchestrator.calculate(context);

        return new PricingQuoteResponse(
            quote.subtotal().amount(),
            quote.totalDiscount().amount(),
            quote.shippingCost().amount(),
            quote.tax().amount(),
            quote.finalTotal().amount(),
            currency,
            quote.appliedRules(),
            quote.decisionTrail(),
            quote.breakdown().stream()
                .map(c -> new PriceComponentResponse(c.name(), c.type().name(), c.amount().amount(), c.description()))
                .toList()
        );
    }

    private CartItem toCartItem(CartItemRequest req, String currency) {
        return new CartItem(
            req.productId(),
            req.productName(),
            req.category(),
            Money.of(req.unitPrice(), currency),
            req.quantity()
        );
    }
}
