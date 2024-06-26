package com.fastcampuspay.money.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberMoney {

    @Getter private final String memberMoneyId;

    @Getter private final String membershipId;

    @Getter private final int balance;

    // @Getter private final String linkedBankAccount;

    public static MemberMoney generateMemberMoney(
            MemberMoneyId moneyChangingRequestId,
            MembershipId membershipId,
            MoneyBalance balance
    ) {
        return new MemberMoney(
                moneyChangingRequestId.getMemberMoneyId(),
                membershipId.getMembershipId(),
                balance.getBalance()
        );
    }

    @Value
    public static class MemberMoneyId {

        public MemberMoneyId(String memberMoneyId) {
            this.memberMoneyId = memberMoneyId;
        }

        String memberMoneyId;
    }

    @Value
    public static class MembershipId {

        public MembershipId(String membershipId) {
            this.membershipId = membershipId;
        }

        String membershipId;
    }

    @Value
    public static class MoneyBalance {

        public MoneyBalance(int balance) {
            this.balance = balance;
        }

        int balance;
    }

    @Value
    public static class MoneyAggregateIdentifier {

        public MoneyAggregateIdentifier(String aggregateIdentifier) {
            this.aggregateIdentifier = aggregateIdentifier;
        }

        String aggregateIdentifier;
    }
}
