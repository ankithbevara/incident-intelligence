package com.incident.service;

import com.incident.kafka.KafkaTopics;
import com.incident.service.DltReplayService.ReplayResult;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class DltReplayService {

    private final KafkaConsumer<String, String> dltReplayConsumer;
    private final KafkaTemplate<String, String> kafkaTemplate;
    /*
        * Without Lombok - @RequiredArgsConstructor
        public DltReplayService(KafkaConsumer<String, String> dltReplayConsumer, KafkaTemplate<String, String> kafkaTemplate){
            this.dltReplayConsumer = dltReplayConsumer;
            this.kafkaTemplate = kafkaTemplate;
        }
    */

    /**
     * Poll messages from the DLT and republish them to the main topic.
     * We commit offsets only after successful republish.
     */
    public ReplayResult replayFromDlt(int maxMessages) {

        // Subscribe to DLT topic (safe to call multiple times, Kafka will handle)
        dltReplayConsumer.subscribe(Collections.singletonList(KafkaTopics.EVENTS_DLT_TOPIC));

        int replayed = 0;
        int polled = 0;

        for (int i = 0; i < 5 && polled < maxMessages; i++) {

            ConsumerRecords<String, String> records = dltReplayConsumer.poll(Duration.ofSeconds(2));

            for (ConsumerRecord<String, String> record : records) {
                if (polled >= maxMessages) break;

                polled++;

                String payload = record.value();

                try {
                    // Republish to main topic (or you can process directly here)
                    kafkaTemplate.send(KafkaTopics.EVENTS_TOPIC, payload).get();

                    replayed++;

                } catch (Exception e) {
                    // If republish fails, do NOT commit offsets.
                    // This keeps the record available for future replay attempts.
                    break;
                }
            }
            if (!records.isEmpty()) break;
        }
        if (replayed > 0) {
            dltReplayConsumer.commitSync();    
        }
        return new ReplayResult(polled, replayed);        
    }
    public record ReplayResult(int polled, int replayed) {}
}