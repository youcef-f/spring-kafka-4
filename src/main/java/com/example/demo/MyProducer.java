package com.example.demo;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import com.example.demo.entities.Greeting;

@Service
public class MyProducer {

	private static final Logger logger = LoggerFactory.getLogger(MyProducer.class);
	//private static final String TOPIC_NAME = "users";
	
	
	  @Value("${kafka.topic}")
	  private  String TOPIC_NAME;
	 
	  @Value(value = "${greeting.topic.name}")
	    private String greetingTopicName;
	  
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	  @Autowired
      private KafkaTemplate<String, Greeting> greetingKafkaTemplate;
	  
	  
	public ListenableFuture<SendResult<String, String>> sendMessageString(String message) {
	 return kafkaTemplate.send(TOPIC_NAME,String.valueOf(Math.random()*1000),message);
	}

	
	public  ListenableFuture<SendResult<String, String>> sendMessageValue(String message) {
		logger.info(String.format("$$ -> Producing message --> %s", message));
		return kafkaTemplate.send(new ProducerRecord<String, String>(TOPIC_NAME, message,message));
	}
		
	
	public ListenableFuture<SendResult<String, String>> sendMessageKeyValue(int counter) {
		logger.info(String.format("$$ -> Producing message --> %s", counter));
		//kafkaTemplate.send(TOPIC_NAME,message);
		return kafkaTemplate.send(new ProducerRecord<String, String>(TOPIC_NAME, Integer.toString(counter), Integer.toString(counter)));
		
	}
		
	
	public ListenableFuture<SendResult<String, Greeting>>  sendGreeting(Greeting greeting) {
		logger.info(String.format("$$ -> Producing message --> %s", greeting.getMsg()));
		//kafkaTemplate.send(TOPIC_NAME,message);
		return greetingKafkaTemplate.send(greetingTopicName, greeting.getName(), greeting);
		
	}
}
