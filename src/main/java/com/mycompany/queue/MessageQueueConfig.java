package com.mycompany.queue;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class MessageQueueConfig 
//implements CommandLineRunner 
{
	final static String queueName = "twitter-stream";

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("twitter-stream-exchange");
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(queueName);
	}

	@Bean
	SimpleMessageListenerContainer container(
			ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	Receiver receiver() {
		return new Receiver();
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}
	
	
//	@Bean(name = "stompTemplate")
//	RabbitTemplate template(){
//		RabbitTemplate template = new RabbitTemplate();
//		template.setConnectionFactory(connectionFactory());
//		//template.setExchange("topic");
//		//template.setQueue("topic");
//		return template;
//	}
//	
//	@Bean(name = "stompFactory")
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory =
//            new CachingConnectionFactory("localhost", 61613);
//        return connectionFactory;
//    }
	
	
	
	@Bean
	RestTemplate restTemplate(){
		return new RestTemplate();
	}

//	@Override
//	public void run(String... arg0) throws Exception {
//		System.out.println("Waiting five seconds...");
//		Thread.sleep(5000);
//		System.out.println("Sending message...");
//		rabbitTemplate.convertAndSend(queueName, "Hello from RabbitMQ!");
//	}
}
