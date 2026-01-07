// This code defines what should happen when our kafka consumer fails.

package com.incident.config;
import com.incident.kafka.KafkaTopics;
import org.apache.kafka.clients.consumer.ConsumerRecord; //consumer represents kafka message which includes Key, Value, topic, offset, partition etc.
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfig {

    /**
     * This bean controls what happens when your @KafkaListener throws an exception.
     * Strategy:
     *  - Retry 3 times with 2s delay
     *  - If still failing, publish the message to the DLT topic
     */
    @Bean
    public DefaultErrorHandler kafkaErrorHandler(KafkaTemplate<String, String> kafkaTemplate) {

        // When retries are exhausted, send to DLT
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (ConsumerRecord<?, ?> record, Exception ex) ->
                        new org.apache.kafka.common.TopicPartition(KafkaTopics.EVENTS_DLT_TOPIC, record.partition())
        );

        // Retry policy: 3 retries, 2 seconds apart
        FixedBackOff backOff = new FixedBackOff(2000L, 3L);

        return new DefaultErrorHandler(recoverer, backOff);
    }
}