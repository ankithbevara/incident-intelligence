package com.incident.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaReplayConfig {

    /**
     * A dedicated consumer used ONLY for replaying messages from the DLT.
     * We keep it separate from the normal @KafkaListener container.
     */
    @Bean
    public KafkaConsumer<String, String> dltReplayConsumer() {

        Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // unique group-id for replay so it doesn't interfere with main consumer group
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "incident-dlt-replayer-group-v2");

        // I want to control commits manually after successful republish
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // For replay, I typically start from earliest (or we can do latest)
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new KafkaConsumer<>(props);
    }
}