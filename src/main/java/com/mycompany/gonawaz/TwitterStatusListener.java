package com.mycompany.gonawaz;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class TwitterStatusListener implements StatusListener {
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Override
	public void onException(Exception arg0) {
		System.err.println("Exception " + arg0.getMessage());
	}

	@Override
	public void onTrackLimitationNotice(int arg0) {
		System.err.println("Limit " + arg0);
	}

	@Override
	public void onStatus(Status status) {
		// System.out.println(arg0.getUser().getName() + " : " +
		// arg0.getText());
//		String location = status.getUser().getLocation();
//		String timeZone = status.getUser().getTimeZone();
//		String geoLocation = status.getGeoLocation().toString();
//		String timeStamp = status.getCreatedAt().toString();
//		String user = status.getUser().getScreenName();
//		System.out.println("Geo Location is " + arg0.getGeoLocation());
//		System.out.println("Time Zone " + timeZone);
		//System.out.println("User is " + user);
		
//		String csv =  (user != null ? user : "null")
//				+ "^" + (location != null ? location : "null")
//				+ "^" + (timeZone != null ? timeZone : "null")
//				+ "^" + (geoLocation != null ? geoLocation : "null")
//				+ "^" + (timeStamp != null ? timeStamp : "null");
		sendMessage(status);
		
	}

	private void sendMessage(Status status){
		rabbitTemplate.convertAndSend("twitter-stream", status);
	}
	@Override
	public void onStallWarning(StallWarning arg0) {
		System.err.println("Stalling ... " + arg0.getMessage());
	}

	@Override
	public void onScrubGeo(long arg0, long arg1) {
		System.err.println("GeoLoc scrubbed");
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice arg0) {
		// TODO Auto-generated method stub

	}
}
