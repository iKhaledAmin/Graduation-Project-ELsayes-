package com.GP.ELsayes.websocket.notification;

import com.GP.ELsayes.model.enums.NotificationStatus;
import com.GP.ELsayes.model.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    private Long id;
    private String notificationTitle;
    private String notificationContent;
    private Date sentAt;
    NotificationStatus status;
    NotificationType type;
}
