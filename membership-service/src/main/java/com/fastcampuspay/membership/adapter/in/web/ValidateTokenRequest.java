package com.fastcampuspay.membership.adapter.in.web;

import com.fastcampuspay.membership.application.port.in.ValidateTokenCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateTokenRequest {
    private String jwtToken;

    public ValidateTokenCommand toCommand() {
        return ValidateTokenCommand.builder()
                .jwtToken(jwtToken)
                .build();
    }
}