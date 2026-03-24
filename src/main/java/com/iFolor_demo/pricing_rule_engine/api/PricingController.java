package com.iFolor_demo.pricing_rule_engine.api;

import com.iFolor_demo.pricing_rule_engine.application.PricingApplicationService;
import com.iFolor_demo.pricing_rule_engine.application.dto.PricingQuoteRequest;
import com.iFolor_demo.pricing_rule_engine.application.dto.PricingQuoteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pricing")
@Tag(name = "Pricing", description = "Pricing orchestration engine — compute deterministic quotes with adjustments")
public class PricingController {

    private final PricingApplicationService pricingApplicationService;

    public PricingController(PricingApplicationService pricingApplicationService) {
        this.pricingApplicationService = pricingApplicationService;
    }

    @PostMapping(value = "/quote", consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Calculate a price quote",
        description = """
            Computes the final price for a cart through a deterministic pipeline:
            base price -> discount rules -> voucher -> shipping.
            Returns a full breakdown of every component that contributed to the final total.
            """,
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful quote calculation",
                content = @Content(schema = @Schema(implementation = PricingQuoteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
        }
    )
    public PricingQuoteResponse quote(@RequestBody @Valid PricingQuoteRequest request) {
        return pricingApplicationService.calculateQuote(request);
    }
}
