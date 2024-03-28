package com.fastcampuspay.banking.adapter.out.persistence;

import com.fastcampuspay.banking.application.port.out.RegisterBankAccountPort;
import com.fastcampuspay.banking.domain.RegisteredBankAccount;
import com.fastcampuspay.common.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class RegisteredBankAccountPersistenceAdapter implements RegisterBankAccountPort {

    private final RegisteredBankAccountRepository registeredBankAccountRepository;


    @Override

    public RegisteredBankAccountJpaEntity createRegisteredBankAccount(RegisteredBankAccount.MembershipId membershipId, RegisteredBankAccount.BankName bankName, RegisteredBankAccount.BankAccountNumber bankAccountNumber, RegisteredBankAccount.IsValidLinkedStatus isValidLinkedStatus) {

        return registeredBankAccountRepository.save(
                new RegisteredBankAccountJpaEntity(
                        Long.parseLong(membershipId.getValue()),
                        bankName.getValue(),
                        bankAccountNumber.getValue(),
                        isValidLinkedStatus.isValue()
                )
        );

    }
}
