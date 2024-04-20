package com.fastcampuspay.payment.application.port.out;

import com.fastcampuspay.payment.domain.Payment;

public interface CreatePaymentPort {
    Payment createPayment(String requestMembershipId, int requestPrice, String franchiseId, String franchiseFeeRate);
}
