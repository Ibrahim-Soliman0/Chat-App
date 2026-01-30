package org.server.chatapp.rmi;

import dto.ChatRoomDTO;
import dto.NotificationDTO;
import model.*;
import model.enums.NotificationType;
import model.enums.RoomType;
import org.server.chatapp.dao.dao.MessageDao;
import org.server.chatapp.dao.implement.*;
import rmi.NotificationService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class NotificationServiceImpl extends UnicastRemoteObject implements NotificationService {
    public NotificationServiceImpl() throws RemoteException {
    }

    @Override
    public List<NotificationDTO> getNotifications(Users user) throws RemoteException {
        FriendsImpl friendsImpl = new FriendsImpl();
        UsersImpl usersImpl = new UsersImpl();
        RoomImpl roomImpl = new RoomImpl();
        NotificationDaoImpl notificationDao = new NotificationDaoImpl();
        List<Notification> userNotifications = notificationDao.getByReceiverId(user.getId());
        List<NotificationDTO> friendSequestAndMessages = userNotifications.stream()
                .map((notification) -> {
                    if (notification.getType() == NotificationType.MESSAGE) {

                        Room room = roomImpl.get(notification.getRoomId());
                        if (room.getType() == RoomType.GROUP) {
                            return new NotificationDTO(notification, null, room, room.getName());
                        }

                        UserRoomsImpl userRooms = new UserRoomsImpl();
                        Users otherUserInChat = userRooms.getSingleUserInRoom(
                                new ChatRoomDTO(user, null, room));
                        return new NotificationDTO(notification, otherUserInChat, room, otherUserInChat.getName());
                    }

                    Friend friendRecord = friendsImpl.get(notification.getFriendId());
                    long userId = friendRecord.getSenderUserId();
                    Users sender = usersImpl.get(userId);
                    return new NotificationDTO(notification, sender, null, sender.getName());
                })
                .toList();

        return friendSequestAndMessages;
    }

    @Override
    public void deleteNotification(Long notificationId) throws RemoteException {
        NotificationDaoImpl notificationDao=new NotificationDaoImpl();
        notificationDao.markAsDeleted(notificationId);
    }

    @Override
    public void markNotificationAsRead(Long notificationId) throws RemoteException {
        NotificationDaoImpl notificationDao=new NotificationDaoImpl();
        notificationDao.markAsRead(notificationId);
    }

    @Override
    public int getNotificationsCount(Users user) throws RemoteException {
        NotificationDaoImpl notificationDao = new NotificationDaoImpl();
        return notificationDao.getCountByReceiverId(user.getId());
    }

    @Override
    public void sendNotification(Notification notification) throws RemoteException {
        NotificationDaoImpl notificationDao = new NotificationDaoImpl();
        // TODO: later send the message to the user in realtime
        notificationDao.insert(notification);
    }
}
