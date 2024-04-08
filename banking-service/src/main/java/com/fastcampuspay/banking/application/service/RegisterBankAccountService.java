package com.fastcampuspay.banking.application.service;

import com.fastcampuspay.banking.adapter.out.external.bank.BankAccount;
import com.fastcampuspay.banking.adapter.out.external.bank.GetBankAccountRequest;
import com.fastcampuspay.banking.adapter.out.persistence.RegisteredBankAccountJpaEntity;
import com.fastcampuspay.banking.adapter.out.persistence.RegisteredBankAccountMapper;
import com.fastcampuspay.banking.application.port.in.RegisterBankAccountCommand;
import com.fastcampuspay.banking.application.port.in.RegisterBankAccountUseCase;
import com.fastcampuspay.banking.application.port.out.GetMembershipPort;
import com.fastcampuspay.banking.application.port.out.MembershipStatus;
import com.fastcampuspay.banking.application.port.out.RegisterBankAccountPort;
import com.fastcampuspay.banking.application.port.out.RequestBankAccountInfoPort;
import com.fastcampuspay.banking.domain.RegisteredBankAccount;
import com.fastcampuspay.common.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional
public class RegisterBankAccountService implements RegisterBankAccountUseCase {

    private final GetMembershipPort getMembershipPort;
    private final RegisterBankAccountPort registerMembershipPort;
    private final RegisteredBankAccountMapper registeredBankAccountMapper;

    private final RequestBankAccountInfoPort requestBankAccountInfoPort;

    @Override
    public RegisteredBankAccount registerBankAccount(RegisterBankAccountCommand command) {

        // 은행 계좌를 등록 해야 하는 서비스 (비즈니스 로직)
        // 멤버 서비스 확인
        MembershipStatus membershipStatus = getMembershipPort.getMembership(command.getMembershipId());
        if (!membershipStatus.isValid()) {
            return null;
        }

        // 1. 등록된 계좌인지 확인한다. -> 외부의 은행에 등록된 계좌인지 확인한다.(정상 여부 확인)
        // Biz Logic -> External System (외부 시스템) -> 외부 시스템과 통신
        // Port -> Adapter -> 외부 시스템과 통신
        BankAccount accountInfo = requestBankAccountInfoPort.getBankAccountInfo(new GetBankAccountRequest(command.getBankName(), command.getBankAccountNumber()));
        boolean accountIsValid = accountInfo.isValid();

        // 2. 등록된 계좌가 아니라면 등록하고, 등록 정보를 리턴한다.
        // 2-1. 등록 가능하지 않은 계좌라면 에러를 리턴
        if (accountIsValid) {
            RegisteredBankAccountJpaEntity registeredBankAccount = registerMembershipPort.createRegisteredBankAccount(
                    new RegisteredBankAccount.MembershipId(command.getMembershipId()),
                    new RegisteredBankAccount.BankName(command.getBankName()),
                    new RegisteredBankAccount.BankAccountNumber(command.getBankAccountNumber()),
                    new RegisteredBankAccount.IsValidLinkedStatus(command.isValidLinkedStatus())
            );
            return registeredBankAccountMapper.mapToDomainEntity(registeredBankAccount);
        } else {
            return null;
        }
    }
}