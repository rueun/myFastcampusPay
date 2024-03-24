package com.fastcampuspay.membership.application.port.out;

import com.fastcampuspay.membership.adapter.out.persistence.MembershipJpaEntity;
import com.fastcampuspay.membership.domain.Membership;

public interface ModifyMembershipPort {
    MembershipJpaEntity modifyMembership(
            Membership.MembershipId membershipId,
            Membership.MembershipName name,
            Membership.MembershipEmail email,
            Membership.MembershipAddress address,
            Membership.MembershipIsValid isValid,
            Membership.MembershipIsCorp isCorp
    );
}
