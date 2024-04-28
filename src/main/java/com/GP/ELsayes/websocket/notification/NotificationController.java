package com.GP.ELsayes.websocket.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


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
        this.notificationService.delete(notificationId);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete-all-by-user-id/{userId}")
    public ResponseEntity<?> deleteALLByUserId(@PathVariable Long userId){
        this.notificationService.deleteAllByUserId(userId);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.ACCEPTED);
    }

    @GetMapping("/get-count-unOpened-by-user-id/{userId}")
    public ResponseEntity<?> getCountOfUnRead(@PathVariable Long userId){
        return new ResponseEntity<>(this.notificationService.getCountByUserId(userId),HttpStatus.OK);
    }
}
