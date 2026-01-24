package org.server.chatapp.dao.dao;

import model.Notification;

import java.util.List;

public interface NotificationDao extends Dao<Notification> {
    List<Notification> getByReceiverId(long receiverId);
    int markAsRead(long id);
    int markAsDeleted(long id);
}