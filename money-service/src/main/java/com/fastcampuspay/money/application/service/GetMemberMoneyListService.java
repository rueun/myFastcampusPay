package com.fastcampuspay.money.application.service;

import com.fastcampuspay.common.UseCase;
import com.fastcampuspay.money.adapter.out.persistence.MemberMoneyJpaEntity;
import com.fastcampuspay.money.adapter.out.persistence.MemberMoneyMapper;
import com.fastcampuspay.money.application.port.in.FindMemberMoneyListByMembershipIdsCommand;
import com.fastcampuspay.money.application.port.in.GetMoneyMoneyListUseCase;
import com.fastcampuspay.money.application.port.out.GetMemberMoneyListPort;
import com.fastcampuspay.money.domain.MemberMoney;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@UseCase
@RequiredArgsConstructor
@Transactional
public class GetMemberMoneyListService implements GetMoneyMoneyListUseCase {

    private final GetMemberMoneyListPort getMemberMoneyListPort;
    private final MemberMoneyMapper memberMoneyMapper;

    @Override
    public List<MemberMoney> findMemberMoneyListByMembershipIds(FindMemberMoneyListByMembershipIdsCommand command) {
        // 여러 개의 membership Ids 를 기준으로, memberMoney 정보를 가져온다.
        List<MemberMoneyJpaEntity> memberMoneyJpaEntityList = getMemberMoneyListPort.getMemberMoneyPort(command.getMembershipIds());
        List<MemberMoney> memberMoneyList = new ArrayList<>();

        for (MemberMoneyJpaEntity memberMoneyJpaEntity : memberMoneyJpaEntityList) {
            memberMoneyList.add(memberMoneyMapper.mapToDomainEntity(memberMoneyJpaEntity));
        }

        return memberMoneyList;
    }

}