package com.fastcampuspay.settlement.job;

import com.fastcampuspay.settlement.tasklet.SettlementTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SettlementJob {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SettlementTasklet settlementTasklet;

    @Bean
    public Job settlement() {
        // settlementStep 을 실행하는 job 을 생성한다.
        return jobBuilderFactory.get("settlement")
                .start(settlementStep())
                .build();
    }

    public Step settlementStep() {
        // settlementTasklet 을 실행하는 step 을 생성한다.
        // settlementTasklet 은 정산 처리를 수행한다.
        return stepBuilderFactory.get("settlementStep")
                .tasklet(settlementTasklet)
                .build();
    }
}