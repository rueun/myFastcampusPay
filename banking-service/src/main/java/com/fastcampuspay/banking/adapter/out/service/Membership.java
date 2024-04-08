package com.fastcampuspay.banking.adapter.out.service;

import lombok.*;

// for banking-service
// 뱅킹 서비스만을 위한 Membership 인터페이스
@Getter
@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Membership {
    private String membershipId;
    private String name;
    private String email;
    private String address;
    private boolean isValid;
    private boolean isCorp;
}
