package com.fastcampuspay.money.adapter.axon.event;

import com.fastcampuspay.common.SelfValidating;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class IncreaseMemberMoneyEvent extends SelfValidating<IncreaseMemberMoneyEvent> {

    private String aggregateIdentifier;
    private String membershipId;
    private int amount;

    public IncreaseMemberMoneyEvent(@NotNull String aggregateIdentifier, @NotNull String membershipId, @NotNull int amount) {
        this.aggregateIdentifier = aggregateIdentifier;
        this.membershipId = membershipId;
        this.amount = amount;
        this.validateSelf();
    }

    public IncreaseMemberMoneyEvent() {
    }
}