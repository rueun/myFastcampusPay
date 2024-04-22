package com.fastcampuspay.money.aggregation.adapter.in.web;

import com.fastcampuspay.money.aggregation.application.port.in.GetMoneySumByAddressCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetMoneySumByAddressRequest {
    String address;

    public GetMoneySumByAddressCommand toCommand() {
        return GetMoneySumByAddressCommand.builder()
                .address(address)
                .build();
    }
}
