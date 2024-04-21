package com.fastcampuspay.money.adapter.out.persistence;

import com.fastcampuspay.common.PersistenceAdapter;
import com.fastcampuspay.money.application.port.out.GetMemberMoneyListPort;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class MemberMoneyPersistenceAdapter implements GetMemberMoneyListPort {

    private final SpringDataMemberMoneyRepository memberMoneyRepository;

    @Override
    public List<MemberMoneyJpaEntity> getMemberMoneyPort(List<String> membershipIds) {
        // membershipIds 를 기준으로, 여러개의 MemberMoneyJpaEntity 를 가져온다.
        return memberMoneyRepository.fineMemberMoneyListByMembershipIds(convertMembershipIds(membershipIds));
    }

    private List<Long> convertMembershipIds(List<String> membershipIds) {
        List<Long> longList = new ArrayList<>();
        // membershipIds 를 Long 타입의 List 로 변환한다.
        for(String membershipId : membershipIds) {
            longList.add(Long.parseLong(membershipId));
        }
        return longList;
    }
}
