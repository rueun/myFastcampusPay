package com.fastcampuspay.money.adapter.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyChangingResultDetail {
    private String moneyChangingRequestId;
    private int moneyChangingType;
    private int moneyChangingResultStatus;
    private int amount;
}


