package com.fastcampuspay.banking.adapter.out.external.bank;

import com.fastcampuspay.banking.application.port.out.RequestBankAccountInfoPort;
import com.fastcampuspay.common.ExternalSystemAdapter;
import lombok.RequiredArgsConstructor;

@ExternalSystemAdapter
@RequiredArgsConstructor
public class BankAccountAdapter implements RequestBankAccountInfoPort {

    @Override
    public BankAccount getBankAccountInfo(GetBankAccountRequest request) {
        // 외부 시스템과 통신하는 로직이라고 가정
        // 실제로 외부 은행에 http 요청을 통해 실제 은행 계좌 정보를 가져오는 로직이 들어갈 수 있음
        return new BankAccount(request.getBankName(), request.getBankAccountNumber(), true);
    }
}
