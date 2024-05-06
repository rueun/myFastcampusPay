package com.fastcampuspay.membership.adapter.in.web;

import com.fastcampuspay.common.WebAdapter;
import com.fastcampuspay.membership.application.port.in.AuthMembershipUseCase;
import com.fastcampuspay.membership.domain.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@WebAdapter
@RestController
@RequiredArgsConstructor
public class AuthMembershipController {
    private final AuthMembershipUseCase authMembershipUseCase;
    @PostMapping(path = "/membership/login")
    JwtToken loginMembership(@RequestBody LoginMembershipRequest request) {

        return authMembershipUseCase.loginMembership(request.toCommand());
    }

    @PostMapping(path = "/membership/refresh-token")
    JwtToken refreshToken(@RequestBody RefreshTokenRequest request) {
        return authMembershipUseCase.refreshJwtTokenByRefreshToken(request.toCommand());
    }

    @PostMapping(path = "/membership/token-validate")
    boolean validateToken(@RequestBody ValidateTokenRequest request) {

        return authMembershipUseCase.validateJwtToken(request.toCommand());
    }

}