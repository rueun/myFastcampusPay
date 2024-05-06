package com.fastcampuspay.membership.adapter.in.web;

import com.fastcampuspay.membership.application.port.in.LoginMembershipCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginMembershipRequest {
    private String membershipId;

    public LoginMembershipCommand toCommand() {
        return LoginMembershipCommand.builder()
                .membershipId(membershipId)
                .build();
    }
}