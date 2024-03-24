package com.fastcampuspay.membership.adapter.in.web;

import com.fastcampuspay.membership.application.port.in.RegisterMembershipCommand;
import com.fastcampuspay.membership.application.port.in.RegisterMembershipUseCase;
import com.fastcampuspay.membership.domain.Membership;
import com.fastcampuspay.common.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class RegisterMembershipController {

    private final RegisterMembershipUseCase registerMembershipUseCase;

    @PostMapping(path = "membership/register")
    ResponseEntity<Membership> registerMembership(@RequestBody RegisterMembershipRequest request) {
        RegisterMembershipCommand command = RegisterMembershipCommand.builder()
                .name(request.getName())
                .email(request.getEmail())
                .address(request.getAddress())
                .isValid(true)
                .isCorp(request.isCorp())
                .build();

        return ResponseEntity.ok(registerMembershipUseCase.registerMembership(command));
    }
}
