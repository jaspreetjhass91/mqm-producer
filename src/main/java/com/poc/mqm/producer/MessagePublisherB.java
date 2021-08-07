package com.poc.mqm.producer;

import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import com.poc.mqm.domain.CustomTextMessage;
import com.poc.mqm.utils.AppConstant;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MessagePublisherB implements CommandLineRunner{

	@Autowired
	private JmsTemplate jmsTemplate;
	@Value("${publish.count:700}")
	private int publishCount;

	@Override
	public void run(String... args) throws Exception {
		log.info("MessagePublisherB started");
		IntStream.range(AppConstant.START_INDEX, publishCount).forEach(data -> {
			CustomTextMessage customTextMessage = new CustomTextMessage(null, "queueBmessage" + data);
			MessagePostProcessor messagePostProcessor = (msg) -> {
				msg.setStringProperty("_type", "com.mq.poc.model.CustomTextMessage");
				return msg;
			};
			jmsTemplate.convertAndSend(AppConstant.QUEUEB, customTextMessage,messagePostProcessor);
		});
		log.info("MessagePublisherB completed");
	}
	
	
}
