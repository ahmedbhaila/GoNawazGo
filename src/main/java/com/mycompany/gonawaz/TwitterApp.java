package com.mycompany.gonawaz;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import twitter4j.FilterQuery;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class TwitterApp implements CommandLineRunner {
	
	
	final static String queueName = "twitter-stream";

	@Bean
	RabbitTemplate template(){
		RabbitTemplate template = new RabbitTemplate();
		template.setConnectionFactory(connectionFactory());
		template.setExchange("twitter-stream-exchange");
		template.setQueue("twitter-stream");
		return template;
	}
	
	@Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
            new CachingConnectionFactory("localhost");
        return connectionFactory;
    }
	
	@Bean
	Twitter twitter() {
		return TwitterFactory.getSingleton();
	}
	
	@Bean
	TwitterStream twitterStream(){
		return new TwitterStreamFactory().getInstance();
	}
	
	@Bean
	TwitterStatusListener statusListener(){
		return new TwitterStatusListener();
	}

	void init() {
		twitterStream().addListener(statusListener());
		FilterQuery filterQuery = new FilterQuery();
	    filterQuery.track(new String[]{"iPhone"});
	    twitterStream().filter(filterQuery);		
	}
	@Override
	public void run(String... arg0) throws Exception {
		init();
	}
	
	
	public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(TwitterApp.class, args);
    }
}
