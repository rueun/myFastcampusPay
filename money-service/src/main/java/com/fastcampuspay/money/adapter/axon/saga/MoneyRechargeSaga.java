package com.fastcampuspay.money.adapter.axon.saga;

import com.fastcampuspay.common.command.CheckRegisteredBankAccountCommand;
import com.fastcampuspay.common.command.RequestFirmbankingCommand;
import com.fastcampuspay.common.event.CheckedRegisteredBankAccountEvent;
import com.fastcampuspay.money.adapter.axon.event.RechargingRequestCreatedEvent;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
@NoArgsConstructor
public class MoneyRechargeSaga {

    @NonNull
    private transient CommandGateway commandGateway;

    @Autowired
    public void setCommandGateway(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @StartSaga
    @SagaEventHandler(associationProperty = "rechargingRequestId") // rechargingRequestId 는 saga 를 구분하는 속성 값이다.
    public void handle(RechargingRequestCreatedEvent event) { // 충전이라는 동작을 시작하게 되는 이벤트(충전 동작을 시작하게 하는 주체는 머니 서비스이다.)
        System.out.println("RechargingRequestCreatedEvent Start saga");
        // "충전 요청" 이 시작되었다.
        String checkRegisteredBankAccountId = UUID.randomUUID().toString();
        SagaLifecycle.associateWith("checkRegisteredBankAccountId ", checkRegisteredBankAccountId);

        // 뱅킹의 계좌 등록 여부를 확인한다.(RegisteredBankAccountAggregate)
        // CheckRegisteredBankAccountCommand -> Check Bank Account
        // -> axon server -> Banking Service -> Common
        commandGateway.send(new CheckRegisteredBankAccountCommand(
                event.getRegisteredBankAccountAggregateIdentifier(),
                event.getRechargingRequestId(),
                event.getMembershipId(),
                checkRegisteredBankAccountId,
                event.getBankName(),
                event.getBankAccountNumber(),
                event.getAmount())
        ).whenComplete((result, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                System.out.println("CheckRegisteredBankAccountCommand failed");
            } else {
                System.out.println("CheckRegisteredBankAccountCommand sent");
            }
        });

        // 기본적으로 axon framework 는 command 를 보낸 후에, command 가 처리되었을 때, event 가 발생하면, 그 event 를 통해 saga 를 진행시킨다.
        // 기본적으로 axon framework 에서 모든 aggregate 의 변경은 aggregate 단위로 이루어진다.

    }

    @SagaEventHandler(associationProperty = "checkRegisteredBankAccountId")
    public void handle(CheckedRegisteredBankAccountEvent event) {
        System.out.println("CheckedRegisteredBankAccountEvent saga: " + event.toString());
        boolean status = event.isChecked();
        if (status) {
            System.out.println("CheckedRegisteredBankAccountEvent event success");
        } else {
            System.out.println("CheckedRegisteredBankAccountEvent event Failed");
        }

        String requestFirmbankingId = UUID.randomUUID().toString();
        SagaLifecycle.associateWith("requestFirmbankingId", requestFirmbankingId);

        // 송금 요청
        // 고객 계좌 -> 법인 계좌
        commandGateway.send(new RequestFirmbankingCommand(
                requestFirmbankingId,
                event.getFirmbankingRequestAggregateIdentifier(),
                event.getRechargingRequestId(),
                event.getMembershipId(),
                event.getFromBankName(),
                event.getFromBankAccountNumber(),
                "fastcampus",
                "123456789",
                event.getAmount()
        )).whenComplete(
                (result, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                        System.out.println("RequestFirmbankingCommand Command failed");
                    } else {
                        System.out.println("RequestFirmbankingCommand Command success");
                    }
                }
        );
    }


}