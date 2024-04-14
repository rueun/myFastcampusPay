package com.fastcampuspay.money.application.service;

import com.fastcampuspay.common.CountDownLatchManager;
import com.fastcampuspay.common.RechargingMoneyTask;
import com.fastcampuspay.common.SubTask;
import com.fastcampuspay.common.UseCase;
import com.fastcampuspay.money.adapter.axon.command.IncreaseMemberMoneyCommand;
import com.fastcampuspay.money.adapter.axon.command.MemberMoneyCreatedCommand;
import com.fastcampuspay.money.adapter.out.persistence.MemberMoneyJpaEntity;
import com.fastcampuspay.money.adapter.out.persistence.MoneyChangingRequestMapper;
import com.fastcampuspay.money.application.port.in.*;
import com.fastcampuspay.money.application.port.out.GetMembershipPort;
import com.fastcampuspay.money.application.port.out.IncreaseMoneyPort;
import com.fastcampuspay.money.application.port.out.MembershipStatus;
import com.fastcampuspay.money.application.port.out.SendRechargingMoneyTaskPort;
import com.fastcampuspay.money.domain.MemberMoney;
import com.fastcampuspay.money.domain.MoneyChangingRequest;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
@Transactional
public class IncreaseMoneyRequestService implements IncreaseMoneyRequestUseCase, CreateMemberMoneyUseCase {

    private final CountDownLatchManager countDownLatchManager;

    private final SendRechargingMoneyTaskPort sendRechargingMoneyTaskPort;
    private final GetMembershipPort getMembershipPort;
    private final IncreaseMoneyPort increaseMoneyPort;
    private final MoneyChangingRequestMapper moneyChangingRequestMapper;

    private final CommandGateway commandGateway;
    private final CreateMemberMoneyPort createMemberMoneyPort;
    private final GetMemberMoneyPort getMemberMoneyPort;

    @Override
    public MoneyChangingRequest increaseMoneyRequest(IncreaseMoneyRequestCommand command) {

        // 머니의 충전(증액) 과정
        // 1. 고객 정보 확인 (멤버)
        MembershipStatus membership = getMembershipPort.getMembership(command.getTargetMembershipId());
        if (!membership.isValid()) {
            return null;
        }
        // 2. 고객의 연동된 계좌 정보 확인 -> 고객의 연동된 계좌가 있는지, 고객의 연동된 계좌의 잔액이 충분한지도 확인 (뱅킹)
        // 3. 법인 계좌 정보 확인 (뱅킹)
        // 4. 증액을 위한 기록. 요청 상태로 MoneyChangingRequest 생성
        // 5. 펌뱅킹 수행 (고객의 계좌 -> 법인 계좌로 이체) (뱅킹)
        // 6. 결과에 따라 MoneyChangingRequest 상태 변경
        MemberMoneyJpaEntity memberMoneyJpaEntity = increaseMoneyPort.increaseMoney(
                new MemberMoney.MembershipId(command.getTargetMembershipId()),
                command.getAmount());

        // 6-1. 결과가 정상이라면 MoneyChangingRequest 상태를 완료로 변경 후 리턴
        if (memberMoneyJpaEntity != null) {
            return moneyChangingRequestMapper.mapToDomainEntity(
                    increaseMoneyPort.createMoneyChangingRequest(
                            new MoneyChangingRequest.TargetMembershipId(command.getTargetMembershipId()),
                            new MoneyChangingRequest.MoneyChangingType(0),
                            new MoneyChangingRequest.ChangingMoneyAmount(command.getAmount()),
                            new MoneyChangingRequest.MoneyChangingStatus(1),
                            new MoneyChangingRequest.Uuid(UUID.randomUUID().toString())
                    )
            );
        }

        // 6-2. 결과가 비정상이라면 MoneyChangingRequest 상태를 실패로 변경 후 리턴
        return null;
    }

    @Override
    public MoneyChangingRequest increaseMoneyRequestAsync(IncreaseMoneyRequestCommand command) {
        // 비동기로 처리하는 경우
        // 1. SubTask, Task 생성
        SubTask validMemberTask = SubTask.builder()
                .subTaskName("validMemberTask : 멤버쉽 유효성 검사")
                .membershipID(command.getTargetMembershipId())
                .taskType("membership")
                .status("ready")
                .build();

        // banking subTask
        // banking account validation
        SubTask validBankingAccountTask = SubTask.builder()
                .subTaskName("validBankingTask : 은행 계좌 유효성 검사")
                .membershipID(command.getTargetMembershipId())
                .taskType("banking")
                .status("ready")
                .build();
        // amount money firmbanking -> 무조건 ok 받았다고 가정(생략하나, 실제로는 필요함)

        List<SubTask> subTasks = List.of(validMemberTask, validBankingAccountTask);
        RechargingMoneyTask task = RechargingMoneyTask.builder()
                .taskID(UUID.randomUUID().toString())
                .taskName("RechargingMoneyTask / 머니 충전 태스크")
                .subTaskList(subTasks)
                .moneyAmount(command.getAmount())
                .membershipID(command.getTargetMembershipId())
                .toBankName("fastcampus")
                .build();

        // 2. Kafka Cluster Producer로 Task 전달
        // task produce
        sendRechargingMoneyTaskPort.sendRechargingMoneyTask(task);
        countDownLatchManager.addCountDownLatch(task.getTaskID());

        // 3. Wait
        try {
            countDownLatchManager.getCountDownLatchForKey(task.getTaskID()).await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 3-1. task-consumer
        // task consumer가 수행하는 동안 countDownLatch 를 통해서 스레드를 대기시킨다.
        // 등록된 subTask, status 모두 ok -> task 결과를 produce

        // 4. Task 결과를 Consume
        // 받은 countDownLatchManager를 통해서 결과 데이터를 받아야 한다.
        String result = countDownLatchManager.getDataForKey(task.getTaskID());
        if (result.equals("success")) {
            // 4-1. Consume OK, Logic 수행

            MemberMoneyJpaEntity memberMoneyJpaEntity = increaseMoneyPort.increaseMoney(
                    new MemberMoney.MembershipId(command.getTargetMembershipId())
                    , command.getAmount());

            if (memberMoneyJpaEntity != null) {
                return moneyChangingRequestMapper.mapToDomainEntity(increaseMoneyPort.createMoneyChangingRequest(
                                new MoneyChangingRequest.TargetMembershipId(command.getTargetMembershipId()),
                                new MoneyChangingRequest.MoneyChangingType(1),
                                new MoneyChangingRequest.ChangingMoneyAmount(command.getAmount()),
                                new MoneyChangingRequest.MoneyChangingStatus(1),
                                new MoneyChangingRequest.Uuid(UUID.randomUUID().toString())
                        )
                );
            }

        } else {
            // 4-2. Consume Fail, Logic 수행
            return null;
        }
        return null;
    }

    @Override
    public void increaseMoneyRequestByEvent(IncreaseMoneyRequestCommand command) {
        final MemberMoneyJpaEntity memberMoney = getMemberMoneyPort.getMemberMoney(new MemberMoney.MembershipId(command.getTargetMembershipId()));

        // 이벤트 소싱
        commandGateway.send(IncreaseMemberMoneyCommand.builder()
                        .membershipId(command.getTargetMembershipId())
                        .amount(command.getAmount())
                        .aggregateIdentifier(memberMoney.getAggregateIdentifier())
                        .build())
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        System.out.println("Error : " + throwable.getMessage());
                        throw new RuntimeException(throwable);
                    } else {
                        // IncreaseMoneyCommand가 성공적으로 처리되었을 때
                        // money Increase
                        System.out.println("increase money result : " + result);
                        increaseMoneyPort.increaseMoney(
                                new MemberMoney.MembershipId(command.getTargetMembershipId())
                                , command.getAmount());
                    }
                });
    }

    @Override
    public void createMemberMoney(CreateMemberMoneyCommand command) {
        // 이벤트 소싱
        MemberMoneyCreatedCommand axonCommand = new MemberMoneyCreatedCommand(command.getMembershipId());
        commandGateway.send(axonCommand).whenComplete((result, throwable) -> {
            if (throwable != null) {
                System.out.println("Error : " + throwable.getMessage());
                throw new RuntimeException(throwable);
            } else {
                System.out.println("Success : " + result);
                createMemberMoneyPort.createMemberMoney(
                        new MemberMoney.MembershipId(command.getMembershipId()),
                        new MemberMoney.MoneyAggregateIdentifier(result.toString())
                );
            }
        });
    }
}