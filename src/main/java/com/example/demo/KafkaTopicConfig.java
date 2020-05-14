package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {


	@Value("${kafka.topic}")
	private String TOPIC_NAME;

	@Value("${kafka.bootstrap-servers}")
	private String bootstrapServers;

	  @Value(value = "${greeting.topic.name}")
	    private String greetingTopicName;
	  
	@Bean
	public NewTopic mySpringKafkaMessageTopic() {
	  return TopicBuilder.name(TOPIC_NAME)
	    .partitions(1)
	    .replicas(1)
	    .compact()
	    .build();
	}
	
	
	@Bean
	public NewTopic mySpringGreetingTopic() {
	  return TopicBuilder.name(greetingTopicName)
	    .partitions(1)
	    .replicas(1)
	    .compact()
	    .build();
	}
	
	
	 @Bean
	    public KafkaAdmin kafkaAdmin() {
	        Map<String, Object> configs = new HashMap<>();
	        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
	        return new KafkaAdmin(configs);
	    }

}
