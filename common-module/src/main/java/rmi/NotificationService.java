package rmi;

import dto.NotificationDTO;
import model.Notification;
import model.Users;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface NotificationService extends Remote {
    List<NotificationDTO> getNotifications(Users user) throws RemoteException;

    void deleteNotification(Long notificationId) throws RemoteException;

    int getNotificationsCount(Users user) throws RemoteException;

    void markNotificationAsRead(Long notificationId) throws RemoteException;

    void sendNotification(Notification notification) throws RemoteException;
}
