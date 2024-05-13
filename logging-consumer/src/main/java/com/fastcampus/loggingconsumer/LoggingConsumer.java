package com.fastcampus.loggingconsumer;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Properties;

@Component
public class LoggingConsumer {

    private KafkaConsumer<String, String> consumer;

    @Bean
    public KafkaConsumer<String, String> initConsumer(@Value("${kafka.clusters.bootstrap-servers}") String bootstrapServers,
                                                       @Value("${logging.topic}") String topic) {

        Properties props = new Properties();

        props.put("bootstrap.servers", bootstrapServers);

        // consumer group
        props.put("group.id", "my-group");

        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));

        return consumer;
    }
}