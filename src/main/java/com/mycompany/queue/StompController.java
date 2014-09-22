package com.mycompany.queue;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class StompController {

	@MessageMapping("/post")
    @SendTo("/topic/trend")
    public String greeting(String message) throws Exception {
        return message;
    }

}
