package com.fastcampuspay.banking.application.port.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MembershipStatus {
    private String membershipId;
    private boolean isValid;
}
