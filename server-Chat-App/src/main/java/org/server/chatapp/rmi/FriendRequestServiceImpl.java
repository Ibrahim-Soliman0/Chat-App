package org.server.chatapp.rmi;

import model.Friend;
import model.Notification;
import model.Users;
import model.enums.FriendStatus;
import model.enums.NotificationStatus;
import model.enums.NotificationType;
import org.server.chatapp.dao.implement.FriendsImpl;
import rmi.FriendRequestService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.time.Instant;

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

        // TODO: later send the notification realtime
    }

    @Override
    public int acceptFriendRequest(Users sender, Users receiver) throws RemoteException {
        FriendsImpl friendsImpl = new FriendsImpl();
        Friend friendRequest = friendsImpl.getUserFriendStatus(receiver.getId(), sender.getId());

        friendRequest.setResponseDate(Timestamp.from(Instant.now()));
        friendRequest.setStatus(FriendStatus.ACCEPTED);

        return friendsImpl.update(friendRequest);
    }

    @Override
    public int cancelFriendRequest(Users sender, Users receiver) throws RemoteException {
        FriendsImpl friendsImpl = new FriendsImpl();
        Friend friendRequest = friendsImpl.getUserFriendStatus(sender.getId(), receiver.getId());

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
