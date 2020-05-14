package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.MyProducer;
import com.example.demo.entities.Greeting;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {

	@Autowired
	private MyProducer myProducer;

	@GetMapping("/simple/{message}")
	public String sendMessage(@PathVariable String message) {

		ListenableFuture<SendResult<String, String>> future = myProducer.sendMessageString(message);

		return "Messages sent";
	}

	@GetMapping(value = "/publish")
	public String sendMessageToKafkaTopic(@RequestParam("message") String message) {
		ListenableFuture<SendResult<String, String>> future = myProducer.sendMessageValue(message);

		future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

			@Override
			public void onSuccess(SendResult<String, String> result) {
				System.out.println("----------------------------------------------");
				System.out.println("offset: " + result.getRecordMetadata().offset() + " topic: "
						+ result.getRecordMetadata().topic() + " partition: " + result.getRecordMetadata().partition()
						+ " key: " + result.getProducerRecord().key() + " value : "
						+ result.getProducerRecord().value());
			}

			@Override
			public void onFailure(Throwable ex) {
				ex.printStackTrace();
			}

		});

		return "Messages sent";
	}

	@GetMapping("/sendMessages/{counter}")
	public String sendMessages(@PathVariable int counter) {

		for (int i = 0; i < counter; i++) {
			ListenableFuture<SendResult<String, String>> future = myProducer.sendMessageKeyValue(i);

			future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

				@Override
				public void onSuccess(SendResult<String, String> result) {
					System.out.println("offset: " + result.getRecordMetadata().offset() + " topic: "
							+ result.getRecordMetadata().topic() + " partition: "
							+ result.getRecordMetadata().partition() + " key: " + result.getProducerRecord().key()
							+ " value : " + result.getProducerRecord().value());
				}

				@Override
				public void onFailure(Throwable ex) {
					ex.printStackTrace();
				}
			});
		}

		return "Messages sent";
	}

	@PostMapping("/greeting")
	public String sendGreeting(@RequestBody Greeting greeting) {

		 ListenableFuture<SendResult<String, Greeting>> future = myProducer.sendGreeting(greeting);

			future.addCallback(new ListenableFutureCallback<SendResult<String, Greeting>>() {

				@Override
				public void onSuccess(SendResult<String, Greeting> result) {
					System.out.println("----------------------------------------------");
					System.out.println("offset: " + result.getRecordMetadata().offset() + " topic: "
							+ result.getRecordMetadata().topic() + " partition: " + result.getRecordMetadata().partition()
							+ " key: " + result.getProducerRecord().key() + " value : "
							+ result.getProducerRecord().value().toString());
				}

				@Override
				public void onFailure(Throwable ex) {
					ex.printStackTrace();
				}

			});

			return "Messages sent";
	}

}
