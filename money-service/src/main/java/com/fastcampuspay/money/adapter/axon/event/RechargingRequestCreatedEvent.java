package com.fastcampuspay.money.adapter.axon.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RechargingRequestCreatedEvent {
    // "충전" 동작을 요청이 생성되었다는 "이벤트"

    private String rechargingRequestId; // 하나의 충전 요청 saga 에 대한 식별자

    private String membershipId;

    private int amount;

    private String registeredBankAccountAggregateIdentifier;

    private String bankName;
    private String bankAccountNumber;
}