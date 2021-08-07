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
public class MessagePublisherC implements CommandLineRunner {

	@Autowired
	private JmsTemplate jmsTemplate;
	@Value("${publish.count:900}")
	private int publishCount;

	@Override
	public void run(String... args) throws Exception {
		log.info("MessagePublisherC started");
		IntStream.range(AppConstant.START_INDEX, publishCount).forEach(data -> {
			CustomTextMessage customTextMessage = new CustomTextMessage(null, "queueCmessage" + data);
			MessagePostProcessor messagePostProcessor = (msg) -> {
				msg.setStringProperty("_type", "com.mq.poc.model.CustomTextMessage");
				return msg;
			};
			jmsTemplate.convertAndSend(AppConstant.QUEUEC, customTextMessage, messagePostProcessor);
		});
		log.info("MessagePublisherC completed");

	}

}
