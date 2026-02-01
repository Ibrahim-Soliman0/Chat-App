package org.server.chatapp.dao.dao;

import model.Friend;
import model.Notification;
import model.Users;

import java.util.List;

public interface NotificationDao extends Dao<Notification> {
    List<Notification> getByReceiverId(long receiverId);

    int markAsRead(long id);

    int markAsDeleted(long id);

    int getCountByReceiverId(long receiverId);

    Notification getFriendRequestNotification(Users receiver, Friend friendRequest);
}