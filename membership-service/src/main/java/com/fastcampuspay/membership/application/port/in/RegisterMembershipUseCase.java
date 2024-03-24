package com.fastcampuspay.membership.application.port.in;

import com.fastcampuspay.membership.domain.Membership;
import com.fastcampuspay.common.UseCase;

public interface RegisterMembershipUseCase {
    Membership registerMembership(RegisterMembershipCommand command);
}
