package com.GP.ELsayes.websocket.notification;

import com.GP.ELsayes.model.dto.CarResponse;
import com.GP.ELsayes.model.entity.Car;
import com.GP.ELsayes.model.entity.SystemUsers.User;
import com.GP.ELsayes.model.enums.NotificationStatus;
import com.GP.ELsayes.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepo notificationRepo;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationMapper notificationMapper;
    private final UserService userService;


    private void throwExceptionIfCustomerAlreadyHasCar(Long notificationId){
        Notification notification = getById(notificationId);
        if(notification.getStatus() == NotificationStatus.NOT_OPENED)
            throw new RuntimeException("You can't delete this notification , not open yet");
    }


    public Notification add(Notification notification){

        User user = new User();
        user.setId(notification.getUser().getId());

        notification.setSentAt(new Date());
        notification.setStatus(NotificationStatus.NOT_OPENED);
        notification.setUser(user);


        return notificationRepo.save(notification);
    }


    public void sendGlobalNotification() {
//        ResponseMessage message = new ResponseMessage("Global Notification");
//
//        messagingTemplate.convertAndSend("/topic/global-notifications", message);
    }

    public void sendPrivateNotification(final String userId,final Notification notification) {
        messagingTemplate.convertAndSendToUser(userId,"/topic/private-notifications", notification.getNotificationContent());
        add(notification);
    }

    public Optional<Notification> getEntityById(Long notificationId) {
        return notificationRepo.findById(notificationId);
    }

    public Notification getById(Long notificationId) {
        return getEntityById(notificationId).orElseThrow(
                () -> new NoSuchElementException("There is no notification with id = " + notificationId)
        );
    }

    public NotificationResponse getResponseById(Long notificationId) {
        Notification notification = getById(notificationId);
        notification.setStatus(NotificationStatus.OPENED);
        notification = notificationRepo.save(notification);

        return notificationMapper.toResponse(notification);
    }


    public List<NotificationResponse> getAllByUserId(Long userId){
            return notificationRepo.findAllByUserId(userId)
                    .stream()
                    .map(notification ->  notificationMapper.toResponse(notification))
                    .toList();
    }

    public void delete(Long notificationId) {
        getById(notificationId);
        throwExceptionIfCustomerAlreadyHasCar(notificationId);
        notificationRepo.deleteById(notificationId);
    }

    public void deleteAllByUserId(Long userId) {
        userService.getById(userId);
        notificationRepo.deleteAllByUserId(userId);
    }

    public Integer getCountByUserId(Long userId){
        userService.getById(userId);
        return notificationRepo.countByUserId(userId);
    }
}
