package com.fastcampuspay.remittance;

import com.fastcampuspay.remittance.adapter.out.persistence.RemittanceRequestJpaEntity;
import com.fastcampuspay.remittance.adapter.out.persistence.RemittanceRequestMapper;
import com.fastcampuspay.remittance.application.port.in.RequestRemittanceCommand;
import com.fastcampuspay.remittance.application.port.out.RequestRemittancePort;
import com.fastcampuspay.remittance.application.port.out.banking.BankingPort;
import com.fastcampuspay.remittance.application.port.out.membership.MembershipPort;
import com.fastcampuspay.remittance.application.port.out.membership.MembershipStatus;
import com.fastcampuspay.remittance.application.port.out.money.MoneyInfo;
import com.fastcampuspay.remittance.application.port.out.money.MoneyPort;
import com.fastcampuspay.remittance.application.service.RequestRemittanceService;
import com.fastcampuspay.remittance.domain.RemittanceRequest;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@SpringBootTest
public class TestRequestRemittanceService {

    // Inject Mock
    // @InjectMocks : 테스트 대상이 되는 객체에 붙이는 어노테이션
    private RequestRemittanceService requestRemittanceService;

    // Inject
    // @Mock : 테스트 대상이 되는 객체가 의존하는 객체에 붙이는 어노테이션
    @Mock
    private RequestRemittancePort requestRemittancePort;
    @Mock
    private RemittanceRequestMapper mapper;
    @Mock
    private MembershipPort membershipPort;
    @Mock
    private MoneyPort moneyPort;
    @Mock
    private BankingPort bankingPort;

    @BeforeEach
    public void setUp() {
        // 이 라인이 InjectMocks 가 포함된 클래스에
        // @Mock 가 붙은 객체를 주입해준다.
        MockitoAnnotations.openMocks(this);

        /**
         * @NOTE
         * private final 필드의 경우, Setter 를 통해 주입할 수 없기 때문에
         * Reflection 또는 생성자를 통해 주입해야 한다.
         */
        requestRemittanceService = new RequestRemittanceService(requestRemittancePort, mapper, membershipPort, moneyPort, bankingPort);
    }

    private static Stream<RequestRemittanceCommand> provideRequestRemittanceCommand() {
        return Stream.of(
                RequestRemittanceCommand.builder()
                        .fromMembershipId("5")
                        .toMembershipId("4")
                        .toBankAccountNumber("1234-1234-1234")
                        .remittanceType(0)
                        .toBankName("국민은행")
                        .amount(152000)
                        .build()
        );
    }

    private static Stream<RequestRemittanceCommand> provideRequestRemittanceCommandRemittanceTypeExternal() {
        return Stream.of(
                RequestRemittanceCommand.builder()
                        .fromMembershipId("5")
                        .toMembershipId("4")
                        .toBankAccountNumber("1234-1234-1234")
                        .remittanceType(1)
                        .toBankName("국민은행")
                        .amount(152000)
                        .build()
        );
    }

    // 송금 요청을 한 고객이 유효하지 않은 경우
    @ParameterizedTest
    @MethodSource("provideRequestRemittanceCommand")
    void testRequestRemittanceServiceWhenFromMembershipInvalid(RequestRemittanceCommand testCommand) {

        // 1. 어떤 결과가 나와야 하는지 정의
        RemittanceRequest want = null;

        // 2. Mocking 을 위한 dummy data 정의

        // 3. 결과를 위해 필요한 객체를 Mocking
        when(requestRemittancePort.createRemittanceRequestHistory(testCommand))
                .thenReturn(null);
        when(membershipPort.getMembershipStatus(testCommand.getFromMembershipId()))
                .thenReturn(new MembershipStatus(testCommand.getToMembershipId(), false));


        // 4. 테스트 대상 메소드 호출
        final RemittanceRequest got = requestRemittanceService.requestRemittance(testCommand);

        // 5. Verify 를 통해 테스트가 잘 수행되었는지 확인
        verify(requestRemittancePort, times(1)).createRemittanceRequestHistory(testCommand);
        verify(membershipPort, times(1)).getMembershipStatus(testCommand.getFromMembershipId());

        // 6. Assertion 을 통해 테스트 결과 확인
        Assertions.assertEquals(want, got);
    }

    // 잔액이 충분하지 않은 경우
    @ParameterizedTest
    @MethodSource("provideRequestRemittanceCommand")
    void testRequestRemittanceServiceWhenBalanceNotEnough(RequestRemittanceCommand testCommand) {

        // 1. 어떤 결과가 나와야 하는지 정의
        RemittanceRequest want = null;

        // 2. Mocking 을 위한 dummy data 정의
        MoneyInfo dummyMoneyInfo = new MoneyInfo(testCommand.getFromMembershipId(), 5000);
        int dummyRechargeAmount = (int) Math.ceil((testCommand.getAmount() - dummyMoneyInfo.getBalance()) / 10000.0) * 10000;

        // 3. 결과를 위해 필요한 객체를 Mocking
        when(requestRemittancePort.createRemittanceRequestHistory(testCommand))
                .thenReturn(null);
        when(membershipPort.getMembershipStatus(testCommand.getFromMembershipId()))
                .thenReturn(new MembershipStatus(testCommand.getToMembershipId(), true));
        when(moneyPort.getMoneyInfo(testCommand.getFromMembershipId()))
                .thenReturn(dummyMoneyInfo);
        when(moneyPort.requestMoneyRecharging(testCommand.getFromMembershipId(), dummyRechargeAmount))
                .thenReturn(false);

        // 4. 테스트 대상 메소드 호출
        final RemittanceRequest got = requestRemittanceService.requestRemittance(testCommand);

        // 5. Verify 를 통해 테스트가 잘 수행되었는지 확인
        verify(requestRemittancePort, times(1)).createRemittanceRequestHistory(testCommand);
        verify(membershipPort, times(1)).getMembershipStatus(testCommand.getFromMembershipId());
        verify(moneyPort, times(1)).getMoneyInfo(testCommand.getFromMembershipId());
        verify(moneyPort, times(1)).requestMoneyRecharging(testCommand.getFromMembershipId(), dummyRechargeAmount);


        // 6. Assertion 을 통해 테스트 결과 확인
        Assertions.assertEquals(want, got);
    }

    // 정상 송금 요청 (내부 고객)
    @ParameterizedTest
    @MethodSource("provideRequestRemittanceCommand")
    void testRequestRemittanceServiceWhenRemittanceTypeCustomer(RequestRemittanceCommand testCommand) {

        // 1. 어떤 결과가 나와야 하는지 정의
        RemittanceRequest want = RemittanceRequest.generateRemittanceRequest(
                new RemittanceRequest.RemittanceRequestId("1"),
                new RemittanceRequest.RemittanceFromMembershipId(testCommand.getFromMembershipId()),
                new RemittanceRequest.ToBankName(testCommand.getToBankName()),
                new RemittanceRequest.ToBankAccountNumber(testCommand.getToBankAccountNumber()),
                new RemittanceRequest.RemittanceType(0),
                new RemittanceRequest.Amount(testCommand.getAmount()),
                new RemittanceRequest.RemittanceStatus("success")
        );

        // 2. Mocking 을 위한 dummy data 정의
        RemittanceRequestJpaEntity dummyEntity = RemittanceRequestJpaEntity.builder()
                .remittanceRequestId(1L)
                .fromMembershipId(testCommand.getFromMembershipId())
                .toMembershipId(testCommand.getToMembershipId())
                .toBankName(testCommand.getToBankName())
                .toBankAccountNumber(testCommand.getToBankAccountNumber())
                .remittanceType(0)
                .amount(testCommand.getAmount())
                .build();

        MoneyInfo dummyMoneyInfo = new MoneyInfo(testCommand.getFromMembershipId(), 5000);
        int dummyRechargeAmount = (int) Math.ceil((testCommand.getAmount() - dummyMoneyInfo.getBalance()) / 10000.0) * 10000;

        // 3. 결과를 위해 필요한 객체를 Mocking
        when(requestRemittancePort.createRemittanceRequestHistory(testCommand))
                .thenReturn(dummyEntity);
        when(membershipPort.getMembershipStatus(testCommand.getFromMembershipId()))
                .thenReturn(new MembershipStatus(testCommand.getToMembershipId(), true));
        when(moneyPort.getMoneyInfo(testCommand.getFromMembershipId()))
                .thenReturn(dummyMoneyInfo);
        when(moneyPort.requestMoneyRecharging(testCommand.getFromMembershipId(), dummyRechargeAmount))
                .thenReturn(true);
        when(moneyPort.requestMoneyDecrease(testCommand.getFromMembershipId(), testCommand.getAmount()))
                .thenReturn(true);
        when(moneyPort.requestMoneyIncrease(testCommand.getToMembershipId(), testCommand.getAmount()))
                .thenReturn(true);
        when(requestRemittancePort.saveRemittanceRequestHistory(dummyEntity)).thenReturn(true);


        // 4. 테스트 대상 메소드 호출
        final RemittanceRequest got = requestRemittanceService.requestRemittance(testCommand);

        // 5. Verify 를 통해 테스트가 잘 수행되었는지 확인
        verify(requestRemittancePort, times(1)).createRemittanceRequestHistory(testCommand);
        verify(membershipPort, times(1)).getMembershipStatus(testCommand.getFromMembershipId());
        verify(moneyPort, times(1)).getMoneyInfo(testCommand.getFromMembershipId());
        verify(moneyPort, times(1)).requestMoneyRecharging(testCommand.getFromMembershipId(), dummyRechargeAmount);
        verify(moneyPort, times(1)).requestMoneyDecrease(testCommand.getFromMembershipId(), testCommand.getAmount());
        verify(moneyPort, times(1)).requestMoneyIncrease(testCommand.getToMembershipId(), testCommand.getAmount());
        verify(requestRemittancePort, times(1)).saveRemittanceRequestHistory(dummyEntity);

        // 6. Assertion 을 통해 테스트 결과
        Assertions.assertEquals(want, got);
    }

    // 정상 송금 요청 (외부 은행)
    @ParameterizedTest
    @MethodSource("provideRequestRemittanceCommandRemittanceTypeExternal")
    void testRequestRemittanceServiceWhenRemittanceTypeExternal(RequestRemittanceCommand testCommand) {

        // 1. 어떤 결과가 나와야 하는지 정의
        RemittanceRequest want = RemittanceRequest.generateRemittanceRequest(
                new RemittanceRequest.RemittanceRequestId("1"),
                new RemittanceRequest.RemittanceFromMembershipId(testCommand.getFromMembershipId()),
                new RemittanceRequest.ToBankName(testCommand.getToBankName()),
                new RemittanceRequest.ToBankAccountNumber(testCommand.getToBankAccountNumber()),
                new RemittanceRequest.RemittanceType(1),
                new RemittanceRequest.Amount(testCommand.getAmount()),
                new RemittanceRequest.RemittanceStatus("success")
        );

        // 2. Mocking 을 위한 dummy data 정의
        RemittanceRequestJpaEntity dummyEntity = RemittanceRequestJpaEntity.builder()
                .remittanceRequestId(1L)
                .fromMembershipId(testCommand.getFromMembershipId())
                .toMembershipId(testCommand.getToMembershipId())
                .toBankName(testCommand.getToBankName())
                .toBankAccountNumber(testCommand.getToBankAccountNumber())
                .remittanceType(1)
                .amount(testCommand.getAmount())
                .build();

        MoneyInfo dummyMoneyInfo = new MoneyInfo(testCommand.getFromMembershipId(), 5000);
        int dummyRechargeAmount = (int) Math.ceil((testCommand.getAmount() - dummyMoneyInfo.getBalance()) / 10000.0) * 10000;

        // 3. 결과를 위해 필요한 객체를 Mocking
        when(requestRemittancePort.createRemittanceRequestHistory(testCommand))
                .thenReturn(dummyEntity);
        when(membershipPort.getMembershipStatus(testCommand.getFromMembershipId()))
                .thenReturn(new MembershipStatus(testCommand.getToMembershipId(), true));
        when(moneyPort.getMoneyInfo(testCommand.getFromMembershipId()))
                .thenReturn(dummyMoneyInfo);
        when(moneyPort.requestMoneyRecharging(testCommand.getFromMembershipId(), dummyRechargeAmount))
                .thenReturn(true);
        when(bankingPort.requestFirmbanking(testCommand.getToBankName(), testCommand.getToBankAccountNumber(), testCommand.getAmount()))
                .thenReturn(true);

        // 4. 테스트 대상 메소드 호출
        final RemittanceRequest got = requestRemittanceService.requestRemittance(testCommand);

        // 5. Verify 를 통해 테스트가 잘 수행되었는지 확인
        verify(requestRemittancePort, times(1)).createRemittanceRequestHistory(testCommand);
        verify(membershipPort, times(1)).getMembershipStatus(testCommand.getFromMembershipId());
        verify(moneyPort, times(1)).getMoneyInfo(testCommand.getFromMembershipId());
        verify(moneyPort, times(1)).requestMoneyRecharging(testCommand.getFromMembershipId(), dummyRechargeAmount);
        verify(bankingPort, times(1)).requestFirmbanking(testCommand.getToBankName(), testCommand.getToBankAccountNumber(), testCommand.getAmount());

        // 6. Assertion 을 통해 테스트 결과
        Assertions.assertEquals(want, got);
    }

}
