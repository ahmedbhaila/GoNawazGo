package com.mycompany.queue;

import java.io.FileWriter;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import twitter4j.Status;
import au.com.bytecode.opencsv.CSVWriter;

import com.jayway.jsonpath.JsonPath;


@Component
public class Receiver {
	// @Autowired
	// AnnotationConfigApplicationContext context;
	
	@Autowired
	RestTemplate restTemplate;
	
//	@Autowired
//	RabbitTemplate rabbitTemplate;
	
//	@Autowired
//	StompController stompController;
	
	private SimpMessagingTemplate template;

    @Autowired
    public Receiver(SimpMessagingTemplate template) {
        this.template = template;
    }
	
	private static String mapQuestAPI = "http://open.mapquestapi.com/geocoding/v1/address?" +
									"key=Fmjtd%7Cluur2h07ll%2C2g%3Do5-9wb2lf&callback=renderGeocode&location=";
	
	CSVWriter writer = null;
	public Receiver(){
		try{
			writer = new CSVWriter(new FileWriter("nawaz.csv"), '\t');
		}
		catch(Exception e){
			System.err.println(e.getMessage());
		}
	}
	
	//@MessageMapping("/post")
	//@SendTo("/topic/trend")
	public void receiveMessage(Status status) {
		System.out.println("Received <" + status.toString() + ">");
		
		Double lat = null;
		Double lng = null;
		
		String geo = "";
		
		// get geo location
		if(status.getGeoLocation() != null){
			lat = status.getGeoLocation().getLatitude();
			lng = status.getGeoLocation().getLongitude();
		}
		
		if(lat == null){
			// look up lat lng based on location
			String location = status.getUser().getLocation();
			String response = restTemplate.getForObject(mapQuestAPI + location, String.class);
			if(response != null){
				JsonPath.read(response, "$..displayLatLng.lng");
				JsonPath.read(response, "$.displayLatLng.lat");
			}
		}
		
		if(lat != null && lng != null){
			geo = String.valueOf(lat) + "," + String.valueOf(lng);
			try{
				//stompController.greeting(geo);
			}
			catch(Exception e){
				System.err.println(e.getMessage());
			}
	
		}
		//template.convertAndSend("/topic/trend", new Double[]{lat, lng});
		template.convertAndSend("/topic/trend", geo);
		//return geo;
	}

	
}
