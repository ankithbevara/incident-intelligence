package com.incident.kafka;

public class KafkaTopics {
    public static final String EVENTS_TOPIC = "incident.events.v1";
    public static final String EVENTS_DLT_TOPIC = "incident.events.v1.DLT";
    private KafkaTopics() {} // Used an private constructor to make sure nobody is allowed to create an object of this class
}