package com.GP.ELsayes.websocket.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;


@RestController
@CrossOrigin("*")
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/send")
    @SendTo("/topic/notifications")
    public String send(final String message) {
        return message;
    }



    @GetMapping("/get-by-id/{notificationId}")
    public ResponseEntity<?> getById(@PathVariable Long notificationId){
        return new ResponseEntity<>(this.notificationService.getResponseById(notificationId),HttpStatus.OK);
    }

    @GetMapping("/get-all-notification-by-userId/{userId}")
    public ResponseEntity<?> getAllWorkersByBranchId(@PathVariable Long userId){
        return new ResponseEntity<>(this.notificationService.getAllByUserId(userId), HttpStatus.OK);
    }


    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<?> delete(@PathVariable Long notificationId){
        Map<String, String> response = new HashMap<>();
        this.notificationService.delete(notificationId);
        response.put("message", "Deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete-all-by-user-id/{userId}")
    public ResponseEntity<?> deleteALLByUserId(@PathVariable Long userId){
        Map<String, String> response = new HashMap<>();
        this.notificationService.deleteAllByUserId(userId);
        response.put("message", "Deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/get-count-unOpened-by-user-id/{userId}")
    public ResponseEntity<?> getCountOfUnRead(@PathVariable Long userId){
        return new ResponseEntity<>(this.notificationService.getCountByUserId(userId),HttpStatus.OK);
    }
}
