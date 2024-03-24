package com.fastcampuspay.membership.adapter.in.web;

import com.fastcampuspay.membership.application.port.in.ModifyMembershipCommand;
import com.fastcampuspay.membership.application.port.in.ModifyMembershipUseCase;
import com.fastcampuspay.membership.domain.Membership;
import com.fastcampuspay.common.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class ModifyMembershipController {

    private final ModifyMembershipUseCase modifyMembershipUseCase;

    @PutMapping(path = "/membership/{membershipId}")
    ResponseEntity<Membership> modifyMembershipByMemberId(
            @PathVariable String membershipId,
            @RequestBody ModifyMembershipRequest request) {

        ModifyMembershipCommand command = ModifyMembershipCommand.builder()
                .membershipId(membershipId)
                .name(request.getName())
                .email(request.getEmail())
                .address(request.getAddress())
                .isCorp(request.isCorp())
                .isValid(request.isValid())
                .build();

        return ResponseEntity.ok(modifyMembershipUseCase.modifyMembership(command));
    }
}
