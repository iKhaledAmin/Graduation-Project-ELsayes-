package com.GP.ELsayes.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {
    @Autowired
    private WebSocketService webSocketService;

//    @PostMapping("/send-message")
//    public void sendMessage(@RequestBody final Message message) {
//        webSocketService.notifyFrontend(message.getMessageContent());
//    }
//
//    @PostMapping("/send-private-message/{id}")
//    public void sendPrivateMessage(@PathVariable final String id, @RequestBody final Message message)
//    {
//        webSocketService.notifyUser(id, message.getMessageContent());
//    }
}
