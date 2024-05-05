package com.fastcampuspay.settlement.port.out;

public interface GetRegisteredBankAccountPort {
    RegisteredBankAccountAggregateIdentifier getRegisteredBankAccount(String membershipId);

    // 타겟 계좌 정보, 금액
    void requestFirmbanking(String bankName, String bankAccountNumber, int moneyAmount);
}