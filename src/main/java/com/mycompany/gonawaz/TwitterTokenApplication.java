package com.mycompany.gonawaz;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import scala.annotation.meta.setter;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class TwitterTokenApplication {

	@Autowired
	static RabbitTemplate rabbitTemplate;
	
	
	public static void main(String[] args) {

		// The factory instance is re-useable and thread safe.
		Twitter twitter = TwitterFactory.getSingleton();
		//twitter.setOAuthConsumer("ZNZOF9K4V81nUKnjhsR4VnUMW", "bGao4lQBdrxy0LcwnxrN4bQOya20briKdMMQwn5pR03D7kU6Po");
		try {
			RequestToken requestToken = twitter.getOAuthRequestToken();
			AccessToken accessToken = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			while (null == accessToken) {
				System.out
						.println("Open the following URL and grant access to your account:");
				System.out.println(requestToken.getAuthorizationURL());
				System.out
						.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
				String pin = br.readLine();
				if (pin.length() > 0) {
					accessToken = twitter
							.getOAuthAccessToken(requestToken, pin);
				} else {
					accessToken = twitter.getOAuthAccessToken();
				}
				//AccessToken accessToken = twitter.getOAuthAccessToken();
				storeAccessToken(twitter.verifyCredentials().getId(), accessToken);
				
//				TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
//			    twitterStream.addListener(new StatusListener() {
//					
//					@Override
//					public void onException(Exception arg0) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void onTrackLimitationNotice(int arg0) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void onStatus(Status arg0) {
//						//System.out.println(arg0.getUser().getName() + " : " + arg0.getText());
//						String location = arg0.getUser().getLocation();
//						String timeZone = arg0.getUser().getTimeZone();
//						System.out.println("Geo Location is " + arg0.getGeoLocation());
//						System.out.println("Time Zone " + timeZone);
//						if(location != null && !location.equals("")){
//							System.out.println("Location " + location);
//						}
//						rabbitTemplate.convertAndSend("twitter-stream", "Hello from RabbitMQ!");
//					}
//					
//					@Override
//					public void onStallWarning(StallWarning arg0) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void onScrubGeo(long arg0, long arg1) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void onDeletionNotice(StatusDeletionNotice arg0) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
			    
			    //twitterStream.sample();
//			    FilterQuery filterQuery = new FilterQuery();
//			    filterQuery.track(new String[]{"#GoImranGo"});
//			    twitterStream.filter(filterQuery);
				
				
//				Status status = twitter.updateStatus(args[0]);
//				System.out.println("Successfully updated the status to ["
//						+ status.getText() + "].");

			}
		} catch (TwitterException te) {
			if (401 == te.getStatusCode()) {
				System.out.println("Unable to get the access token.");
			} else {
				te.printStackTrace();
			}
		} catch (Exception e){
			System.err.println(e.getMessage());
		}
		//System.exit(0);

		// SpringApplication.run(Application.class, args);
	}

	private static void storeAccessToken(long useId, AccessToken accessToken) {
		// store accessToken.getToken()
		// store accessToken.getTokenSecret()
		// 17676628-yfaV8ghpY3p9vPCCb1sXHWQm3oC8yvdHvXDoEvWZj
		// z6tC5sWE1qETMvSt3gTXbskt4LQrNiol4BJFZC1sYfgH6
		// 17676628
	}
}
