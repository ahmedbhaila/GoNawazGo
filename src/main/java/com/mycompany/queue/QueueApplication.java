package com.mycompany.queue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class QueueApplication {
	public static void main(String args[]) throws Exception {
		SpringApplication.run(MessageQueueConfig.class, args);
		//SpringApplication.run(TwitterApplication.class, args);
	}
}
