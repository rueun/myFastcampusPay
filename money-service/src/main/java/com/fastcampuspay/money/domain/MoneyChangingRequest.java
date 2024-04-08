package com.fastcampuspay.money.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MoneyChangingRequest {

    @Getter
    private final String moneyChangingRequestId;

    // 어떤 고객의 증액/감액 요청을 했는지의 멤버 정보
    @Getter
    private final String targetMembershipId;

    // 증액/감액 요청의 타입 정보
    @Getter
    private final int changingType; // enum

    // 증액/감액 요청의 금액 정보
    @Getter
    private final int changingMoneyAmount;

    // 증액/감액 요청의 처리 상태 정보
    @Getter
    private final int changingMoneyStatus;

    @Getter
    private final String uuid;

    @Getter
    private final Date createdAt;

    public static MoneyChangingRequest generateMoneyChangingRequest(
            MoneyChangingRequestId moneyChangingRequestId,
            TargetMembershipId targetMembershipId,
            MoneyChangingType moneyChangingType,
            ChangingMoneyAmount changingMoneyAmount,
            MoneyChangingStatus changingMonetStatus,
            Uuid uuid
    ) {
        return new MoneyChangingRequest(
                moneyChangingRequestId.getMoneyChangingRequestId(),
                targetMembershipId.getTargetMembershipId(),
                moneyChangingType.getChangingType(),
                changingMoneyAmount.getChangingMoneyAmount(),
                changingMonetStatus.getChangingMoneyStatus(),
                uuid.getUuid(),
                new Date()
        );
    }

    @Value
    public static class MoneyChangingRequestId {

        public MoneyChangingRequestId(String moneyChangingRequestId) {
            this.moneyChangingRequestId = moneyChangingRequestId;
        }

        String moneyChangingRequestId;
    }

    @Value
    public static class TargetMembershipId {

        public TargetMembershipId(String targetMembershipId) {
            this.targetMembershipId = targetMembershipId;
        }

        String targetMembershipId;
    }

    @Value
    public static class ChangingMoneyAmount {

        public ChangingMoneyAmount(int changingMoneyAmount){
            this.changingMoneyAmount = changingMoneyAmount;
        }

        int changingMoneyAmount;
    }

    @Value
    public static class Uuid {

        public Uuid(String uuid) {
            this.uuid = uuid;
        }

        String uuid;
    }

    @Value
    public static class CreatedAt {

        public CreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        Date createdAt;
    }

    @Value
    public static class MoneyChangingType {

        public MoneyChangingType(int changingType) {
            this.changingType = changingType;
        }

        int changingType;
    }

    @Value
    public static class MoneyChangingStatus {

        public MoneyChangingStatus(int changingMoneyStatus) {
            this.changingMoneyStatus = changingMoneyStatus;
        }

        int changingMoneyStatus;
    }
}
