package com.hisabKitab.springProject.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic friendRequestTopic() {
        return new NewTopic("friend-request-topic", 1, (short) 1);
    }
}
