package com.fastcampuspay.banking;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// common 패키지의 LoggingProducer, LoggingAspect를 사용하기 위해 ComponentScan을 사용
// common 패키지의 모든 클래스를 빈으로 등록
@Configuration
@ComponentScan("com.fastcampuspay.common")
public class BankingConfig {
}
