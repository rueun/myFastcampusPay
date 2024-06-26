package com.fastcampuspay.payment.adapter.out.persistence;

import com.fastcampuspay.payment.application.port.out.ChangePaymentStatusPort;
import com.fastcampuspay.payment.application.port.out.CreatePaymentPort;
import com.fastcampuspay.payment.application.port.out.GetPaymentPort;
import com.fastcampuspay.payment.domain.Payment;
import com.fastcampuspay.common.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class PaymentPersistenceAdapter implements CreatePaymentPort, GetPaymentPort, ChangePaymentStatusPort {
    private final SpringDataPaymentRepository paymentRepository;
    private final PaymentMapper mapper;

    @Override
    public Payment createPayment(String requestMembershipId, int requestPrice, String franchiseId, String franchiseFeeRate) {
        PaymentJpaEntity jpaEntity = paymentRepository.save(
                new PaymentJpaEntity(
                        requestMembershipId,
                        requestPrice,
                        franchiseId,
                        franchiseFeeRate,
                        0, // 0: 승인, 1: 실패, 2: 정산 완료.
                        null
                )
        );
        return mapper.mapToDomainEntity(jpaEntity);
    }

    @Override
    public List<Payment> getNormalStatusPayments() {
        final List<Payment> payments = new ArrayList<>();
        final List<PaymentJpaEntity> paymentJpaEntities = paymentRepository.findByPaymentStatus(0);
        if (paymentJpaEntities != null) {
            for (PaymentJpaEntity paymentJpaEntity : paymentJpaEntities) {
                payments.add(mapper.mapToDomainEntity(paymentJpaEntity));
            }
            return payments;
        }
        return null;
    }

    @Override
    public void changePaymentRequestStatus(final String paymentId, final int status) {
        final Optional<PaymentJpaEntity> paymentJpaEntity = paymentRepository.findById(Long.parseLong(paymentId));
        if (paymentJpaEntity.isPresent()) {
            paymentJpaEntity.get().setPaymentStatus(status);
            paymentRepository.save(paymentJpaEntity.get());
        }
    }
}