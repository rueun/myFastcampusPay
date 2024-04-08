package com.fastcampuspay.money.application.port.out;

import com.fastcampuspay.common.RechargingMoneyTask;

// kafka cluster 로 태스크를 async 하게 produce 하기 위한 포트
public interface SendRechargingMoneyTaskPort {
    void sendRechargingMoneyTask(RechargingMoneyTask rechargingMoneyTask);
}
