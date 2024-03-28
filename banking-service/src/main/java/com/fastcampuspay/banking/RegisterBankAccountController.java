package com.fastcampuspay.banking;

import com.fastcampuspay.banking.application.port.in.RegisterBankAccountCommand;
import com.fastcampuspay.banking.application.port.in.RegisterBankAccountUseCase;
import com.fastcampuspay.banking.domain.RegisteredBankAccount;
import com.fastcampuspay.common.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class RegisterBankAccountController {

    private final RegisterBankAccountUseCase registerBankAccountUseCase;

    @PostMapping(path = "banking/account/register")
    ResponseEntity<RegisteredBankAccount> registerMembership(@RequestBody RegisterBankAccountRequest request) {
        RegisterBankAccountCommand command = new RegisterBankAccountCommand(
                request.getMembershipId(),
                request.getBankName(),
                request.getBankAccountNumber(),
                request.isValidLinkedStatus()
        );

        RegisteredBankAccount registeredBankAccount = registerBankAccountUseCase.registerBankAccount(command);
        if (registeredBankAccount == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(registeredBankAccount);
    }
}
