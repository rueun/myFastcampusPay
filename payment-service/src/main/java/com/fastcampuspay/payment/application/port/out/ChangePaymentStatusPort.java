package com.fastcampuspay.payment.application.port.out;

public interface ChangePaymentStatusPort {

    void changePaymentRequestStatus(String paymentId, int status);
}
