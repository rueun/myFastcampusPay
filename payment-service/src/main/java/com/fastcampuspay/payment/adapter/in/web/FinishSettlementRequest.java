package com.fastcampuspay.payment.adapter.in.web;

import com.fastcampuspay.payment.application.port.in.FinishSettlementCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinishSettlementRequest {
    private String paymentId;

    public FinishSettlementCommand toCommand() {
        return new FinishSettlementCommand(paymentId);
    }
}