package org.server.chatapp.rmi;

import model.*;
import model.enums.FriendStatus;
import model.enums.NotificationStatus;
import model.enums.NotificationType;
import model.enums.RoomType;
import org.server.chatapp.dao.implement.FriendsImpl;
import org.server.chatapp.dao.implement.NotificationDaoImpl;
import org.server.chatapp.dao.implement.RoomImpl;
import org.server.chatapp.dao.implement.UserRoomsImpl;
import rmi.FriendRequestService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

public class FriendRequestServiceImpl extends UnicastRemoteObject implements FriendRequestService {

    public FriendRequestServiceImpl() throws RemoteException {}

    @Override
    public void sendFriendRequest(Users sender, Users receiver) throws RemoteException {
        FriendsImpl friendsImpl = new FriendsImpl();
        Friend friendRequest = new Friend(sender.getId(), receiver.getId(), Timestamp.from(Instant.now()),
                null, FriendStatus.PENDING);

        long friendRecordId = friendsImpl.insert(friendRequest);
        Notification friendRequestNotification = new Notification(receiver.getId(),
                NotificationType.FRIEND_REQUEST,
                sender.getName() + " Sent You a Friend Request.",
                friendRecordId, Timestamp.from(Instant.now()), NotificationStatus.UNREAD, null);

        NotificationServiceImpl notificationService = new NotificationServiceImpl();
        notificationService.sendNotification(friendRequestNotification);
    }

    @Override
    public void acceptFriendRequest(Users sender, Users receiver) throws RemoteException {
        FriendsImpl friendsImpl = new FriendsImpl();
        Friend friendRequest = friendsImpl.getUserFriendStatus(receiver.getId(), sender.getId());

        friendRequest.setResponseDate(Timestamp.from(Instant.now()));
        friendRequest.setStatus(FriendStatus.ACCEPTED);

        Room room = new Room(
                RoomType.ONE_TO_ONE,
                null,
                null,
                null,
                LocalDateTime.now(),
                null,
                sender.getId()
        );

        RoomImpl roomImpl = new RoomImpl();

        long roomId = roomImpl.insert(room);

        if (roomId == -1) {
            System.out.println("Failed to Create Room");
            return;
        }

        UserRoomsImpl userRoomsImpl = new UserRoomsImpl();

        UserRooms addMeToRoom = new UserRooms(sender.getId(), roomId,
                false, null, null, true);
        UserRooms addOtherToRoom = new UserRooms(receiver.getId(), roomId,
                false, null, null, true);

        userRoomsImpl.insert(addMeToRoom);
        userRoomsImpl.insert(addOtherToRoom);

        friendsImpl.update(friendRequest);
    }

    @Override
    public int cancelFriendRequest(Users sender, Users receiver) throws RemoteException {
        FriendsImpl friendsImpl = new FriendsImpl();
        Friend friendRequest = friendsImpl.getUserFriendStatus(sender.getId(), receiver.getId());

        NotificationDaoImpl notificationDao = new NotificationDaoImpl();
        Notification friendRequestNotification =
                notificationDao.getFriendRequestNotification(receiver, friendRequest);

        friendRequestNotification.setStatus(NotificationStatus.DELETED);

        notificationDao.update(friendRequestNotification);

        return friendsImpl.delete(friendRequest);
    }

    @Override
    public int rejectFriendRequest(Users sender, Users receiver) throws RemoteException {
        FriendsImpl friendsImpl = new FriendsImpl();
        Friend friendRequest = friendsImpl.getUserFriendStatus(sender.getId(), receiver.getId());

        friendRequest.setResponseDate(Timestamp.from(Instant.now()));
        friendRequest.setStatus(FriendStatus.REJECTED);

        return friendsImpl.update(friendRequest);
    }
}
