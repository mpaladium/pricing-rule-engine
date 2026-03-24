package com.iFolor_demo.pricing_rule_engine.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Campaign {

    private final String id;
    private final String name;
    private final CampaignType type;
    private final int priority;
    private final boolean stackable;
    private final Set<CustomerSegment> eligibleSegments;
    private final Map<String, BigDecimal> parameters;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Campaign(String id, String name, CampaignType type, int priority,
                    boolean stackable, Set<CustomerSegment> eligibleSegments,
                    Map<String, BigDecimal> parameters, LocalDate startDate, LocalDate endDate) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
        this.priority = priority;
        this.stackable = stackable;
        this.eligibleSegments = Set.copyOf(Objects.requireNonNull(eligibleSegments));
        this.parameters = Map.copyOf(Objects.requireNonNull(parameters));
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return (startDate == null || !today.isBefore(startDate))
            && (endDate == null || !today.isAfter(endDate));
    }

    public boolean isEligibleFor(CustomerSegment segment) {
        return eligibleSegments.isEmpty() || eligibleSegments.contains(segment);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public CampaignType getType() { return type; }
    public int getPriority() { return priority; }
    public boolean isStackable() { return stackable; }
    public Set<CustomerSegment> getEligibleSegments() { return eligibleSegments; }
    public Map<String, BigDecimal> getParameters() { return parameters; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
}
