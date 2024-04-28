package com.GP.ELsayes.websocket.notification;

import com.GP.ELsayes.model.entity.SystemUsers.User;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.enums.NotificationStatus;
import com.GP.ELsayes.model.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String notificationContent;
    private Date sentAt;

    @Enumerated(EnumType.STRING)
    NotificationStatus status;
    @Enumerated(EnumType.STRING)
    NotificationType type;



    @JsonBackReference
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

}
