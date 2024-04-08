package com.fastcampuspay.money.adapter.out.persistence;

import com.fastcampuspay.common.PersistenceAdapter;
import com.fastcampuspay.money.application.port.out.IncreaseMoneyPort;
import com.fastcampuspay.money.domain.MemberMoney;
import com.fastcampuspay.money.domain.MoneyChangingRequest;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class MoneyChangingPersistenceAdapter implements IncreaseMoneyPort {

    private final SpringDataMoneyChangingRequestRepository moneyChangingRequestRepository;
    private final SpringDataMemberMoneyRepository memberMoneyRepository;

    @Override
    public MoneyChangingRequestJpaEntity createMoneyChangingRequest(MoneyChangingRequest.TargetMembershipId targetMembershipId, MoneyChangingRequest.MoneyChangingType moneyChangingType, MoneyChangingRequest.ChangingMoneyAmount changingMoneyAmount, MoneyChangingRequest.MoneyChangingStatus moneyChangingStatus, MoneyChangingRequest.Uuid uuid) {
        return moneyChangingRequestRepository.save(
                new MoneyChangingRequestJpaEntity(
                        targetMembershipId.getTargetMembershipId(),
                        moneyChangingType.getChangingType(),
                        changingMoneyAmount.getChangingMoneyAmount(),
                        moneyChangingStatus.getChangingMoneyStatus(),
                        new Timestamp(System.currentTimeMillis()),
                        UUID.randomUUID().toString()));
    }

    @Override
    public MemberMoneyJpaEntity increaseMoney(MemberMoney.MembershipId membershipId, int increaseMoneyAmount) {
        Optional<MemberMoneyJpaEntity> memberMoneyOp = memberMoneyRepository.findByMembershipId(membershipId.getMembershipId());
        MemberMoneyJpaEntity memberMoney;

        if (memberMoneyOp.isPresent()) {
            memberMoney = memberMoneyOp.get();
            memberMoney.setBalance(memberMoney.getBalance() + increaseMoneyAmount);
        } else {
            memberMoney = new MemberMoneyJpaEntity(membershipId.getMembershipId(), increaseMoneyAmount);
        }
        return memberMoneyRepository.save(memberMoney);
    }
}
