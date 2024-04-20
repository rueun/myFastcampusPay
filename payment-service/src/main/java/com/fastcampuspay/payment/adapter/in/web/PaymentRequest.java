package com.fastcampuspay.payment.adapter.in.web;

import com.fastcampuspay.payment.application.port.in.RequestPaymentCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private String requestMemberId;
    private int requestPrice;
    private String franchiseId;
    private String franchiseFeeRate;
    private int paymentStatus;
    private Date approvedAt;

    public RequestPaymentCommand toCommand() {
        return new RequestPaymentCommand(requestMemberId, requestPrice, franchiseId, franchiseFeeRate);
    }
}
