package com.fastcampuspay.banking.adapter.out.persistence;

import com.fastcampuspay.banking.application.port.in.GetRegisteredBankAccountCommand;
import com.fastcampuspay.banking.application.port.out.GetRegisteredBankAccountPort;
import com.fastcampuspay.banking.application.port.out.RegisterBankAccountPort;
import com.fastcampuspay.banking.domain.RegisteredBankAccount;
import com.fastcampuspay.common.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class RegisteredBankAccountPersistenceAdapter implements RegisterBankAccountPort, GetRegisteredBankAccountPort {

    private final SpringDataRegisteredBankAccountRepository bankAccountRepository;

    @Override
    public RegisteredBankAccountJpaEntity createRegisteredBankAccount(RegisteredBankAccount.MembershipId membershipId, RegisteredBankAccount.BankName bankName, RegisteredBankAccount.BankAccountNumber bankAccountNumber, RegisteredBankAccount.IsValidLinkedStatus isValidLinkedStatus) {

        return bankAccountRepository.save(
                new RegisteredBankAccountJpaEntity(
                        membershipId.getValue(),
                        bankName.getValue(),
                        bankAccountNumber.getValue(),
                        isValidLinkedStatus.isValue(),
                        ""
                )
        );

    }

    @Override
    public RegisteredBankAccountJpaEntity getRegisteredBankAccount(GetRegisteredBankAccountCommand command) {
        List<RegisteredBankAccountJpaEntity> entityList = bankAccountRepository.findByMembershipId(command.getMembershipId());
        if (entityList.size() > 0) {
            return entityList.get(0);
        }
        return null;
    }
}
