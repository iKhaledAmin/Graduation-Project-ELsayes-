package com.GP.ELsayes.websocket.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification,Long> {

    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId ORDER BY n.sentAt DESC")
    List<Notification> findAllByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.user.id = :userId")
    void deleteAllByUserId(Long userId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.status = 'NOT_OPENED'")
    Integer countByUserId(Long userId);
}
