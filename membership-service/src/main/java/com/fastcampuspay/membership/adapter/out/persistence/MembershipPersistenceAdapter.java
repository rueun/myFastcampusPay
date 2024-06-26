package com.fastcampuspay.membership.adapter.out.persistence;

import com.fastcampuspay.membership.application.port.out.FindMembershipPort;
import com.fastcampuspay.membership.application.port.out.ModifyMembershipPort;
import com.fastcampuspay.membership.application.port.out.RegisterMembershipPort;
import com.fastcampuspay.membership.domain.Membership;
import com.fastcampuspay.common.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class MembershipPersistenceAdapter implements RegisterMembershipPort, FindMembershipPort, ModifyMembershipPort {

    private final MembershipRepository membershipRepository;

    @Override
    public MembershipJpaEntity createMembership(Membership.MembershipName name, Membership.MembershipEmail email, Membership.MembershipAddress address, Membership.MembershipIsValid isValid, Membership.MembershipIsCorp isCorp) {
        return membershipRepository.save(new MembershipJpaEntity(
                name.getNameValue(),
                email.getEmailValue(),
                address.getAddressValue(),
                isValid.isValidValue(),
                isCorp.isCorpValue(),
                null)
        );
    }

    @Override
    public MembershipJpaEntity findMembership(Membership.MembershipId membershipId) {
        return membershipRepository.getById(Long.parseLong(membershipId.getMembershipId()));
    }

    @Override
    public List<MembershipJpaEntity> findMembershipListByAddress(Membership.MembershipAddress membershipAddress) {
        // 관악구, 서초구, 강남구 중 하나
        String address = membershipAddress.getAddressValue();
        return membershipRepository.findByAddress(address);
    }

    @Override
    public MembershipJpaEntity modifyMembership(Membership.MembershipId membershipId, Membership.MembershipName name, Membership.MembershipEmail email, Membership.MembershipAddress address, Membership.MembershipIsValid isValid, Membership.MembershipIsCorp isCorp, Membership.MembershipRefreshToken refreshToken) {
        MembershipJpaEntity entity = membershipRepository.getById(Long.parseLong(membershipId.getMembershipId()));
        entity.setName(name.getNameValue());
        entity.setEmail(email.getEmailValue());
        entity.setAddress(address.getAddressValue());
        entity.setValid(isValid.isValidValue());
        entity.setCorp(isCorp.isCorpValue());
        entity.setRefreshToken(refreshToken.getRefreshToken());

        return membershipRepository.save(entity);
    }
}
