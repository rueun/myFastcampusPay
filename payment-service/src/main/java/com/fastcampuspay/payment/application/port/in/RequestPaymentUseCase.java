package com.fastcampuspay.payment.application.port.in;

import com.fastcampuspay.payment.domain.Payment;

import java.util.List;

public interface RequestPaymentUseCase {
    Payment requestPayment(RequestPaymentCommand command);

    List<Payment> getNormalStatusPayments();

    void finishPayment(FinishSettlementCommand toCommand);
}
