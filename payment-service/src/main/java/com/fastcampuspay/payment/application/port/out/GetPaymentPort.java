package com.fastcampuspay.payment.application.port.out;

import com.fastcampuspay.payment.domain.Payment;

import java.util.List;

public interface GetPaymentPort {
    List<Payment> getNormalStatusPayments();
}
