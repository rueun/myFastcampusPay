package com.fastcampuspay.banking.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RegisteredBankAccount {

    @Getter
    private final String registeredBankAccountId;
    @Getter
    private final String membershipId;
    @Getter
    private final String bankName; // enum
    @Getter
    private final String bankAccountNumber;
    @Getter
    private final boolean isValidLinkedStatus;


    public static RegisteredBankAccount generateRegisteredBankAccount(
            RegisteredBankAccount.RegisteredBankAccountId registeredBankAccountId,
            RegisteredBankAccount.MembershipId membershipId,
            RegisteredBankAccount.BankName bankName,
            RegisteredBankAccount.BankAccountNumber bankAccountNumber,
            IsValidLinkedStatus isValidLinkedStatus
    ) {

        return new RegisteredBankAccount(
                registeredBankAccountId.value,
                membershipId.value,
                bankName.value,
                bankAccountNumber.value,
                isValidLinkedStatus.value
        );

    }

    @Value
    public static class RegisteredBankAccountId {

        public RegisteredBankAccountId(String value) {
            this.value = value;
        }

        String value;
    }

    @Value
    public static class MembershipId {

        public MembershipId(String value) {
            this.value = value;
        }

        String value;
    }

    @Value
    public static class BankName {

        public BankName(String value) {
            this.value = value;
        }

        String value;
    }

    @Value
    public static class BankAccountNumber {

        public BankAccountNumber(String value) {
            this.value = value;
        }

        String value;
    }

    @Value
    public static class IsValidLinkedStatus {

        public IsValidLinkedStatus(boolean value) {
            this.value = value;
        }

        boolean value;
    }
}
