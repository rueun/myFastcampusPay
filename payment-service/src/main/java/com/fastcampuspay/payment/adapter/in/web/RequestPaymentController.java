package com.fastcampuspay.payment.adapter.in.web;

import com.fastcampuspay.common.WebAdapter;
import com.fastcampuspay.payment.application.port.in.RequestPaymentUseCase;
import com.fastcampuspay.payment.domain.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@WebAdapter
@RestController("/payment")
@RequiredArgsConstructor
public class RequestPaymentController {

    private final RequestPaymentUseCase requestPaymentUseCase;

    @PostMapping("/request")
    void requestPayment(@RequestBody PaymentRequest paymentRequest) {
        requestPaymentUseCase.requestPayment(paymentRequest.toCommand());
    }

    @GetMapping(path = "/payment/normal-status")
    List<Payment> getNormalStatusPayments() {
        return requestPaymentUseCase.getNormalStatusPayments();
    }

    @PostMapping(path = "/payment/finish-settlement")
    void finishSettlement(@RequestBody FinishSettlementRequest request) {
        System.out.println("request.getPaymentId() = " + request.getPaymentId());
        requestPaymentUseCase.finishPayment(request.toCommand());
    }
}
