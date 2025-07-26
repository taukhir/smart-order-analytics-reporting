//package org.soar.orderservice.config;
//
//import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class KafkaTopicConfig {
//
//    @Value("${app.kafka.topics.order-topic}")
//    private String orderTopic;
//
//    @Value("${app.kafka.topics.partitions}")
//    private int partitions;
//
//    @Value("${app.kafka.topics.replicas}")
//    private short replicas;
//
//    @Bean
//    public NewTopic orderTopic() {
//        return new NewTopic(orderTopic, partitions, replicas);
//    }
//}
