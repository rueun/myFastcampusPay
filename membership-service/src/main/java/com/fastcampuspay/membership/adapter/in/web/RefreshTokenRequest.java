package com.fastcampuspay.membership.adapter.in.web;

import com.fastcampuspay.membership.application.port.in.RefreshTokenCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {
    private String refreshToken;

    public RefreshTokenCommand toCommand() {
        return RefreshTokenCommand.builder()
                .refreshToken(refreshToken)
                .build();
    }
}