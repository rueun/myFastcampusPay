package com.fastcampuspay.payment.adapter.in.web;

import com.fastcampuspay.common.WebAdapter;
import com.fastcampuspay.payment.application.port.in.RequestPaymentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController("/payment")
@RequiredArgsConstructor
public class RequestPaymentController {

    private final RequestPaymentUseCase requestPaymentUseCase;

    @PostMapping("/request")
    void requestPayment(@RequestBody PaymentRequest paymentRequest) {
        requestPaymentUseCase.requestPayment(paymentRequest.toCommand());
    }
}
