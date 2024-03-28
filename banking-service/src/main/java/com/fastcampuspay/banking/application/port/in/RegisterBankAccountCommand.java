package com.fastcampuspay.banking.application.port.in;

import com.fastcampuspay.common.SelfValidating;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class RegisterBankAccountCommand extends SelfValidating<RegisterBankAccountCommand> {
    @NotNull
    private String membershipId;
    @NotNull
    private String bankName;
    @NotNull
    private String bankAccountNumber;
    @NotNull
    private boolean isValidLinkedStatus;

    public RegisterBankAccountCommand(String membershipId, String bankName, String bankAccountNumber, boolean isValidLinkedStatus) {
        this.membershipId = membershipId;
        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
        this.isValidLinkedStatus = isValidLinkedStatus;

        this.validateSelf();
    }
}
