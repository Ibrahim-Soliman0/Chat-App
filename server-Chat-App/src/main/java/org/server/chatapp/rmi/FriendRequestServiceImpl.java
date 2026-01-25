package org.server.chatapp.rmi;

import model.Friend;
import model.Users;
import model.enums.FriendStatus;
import org.server.chatapp.dao.implement.FriendsImpl;
import rmi.FriendRequestService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.time.Instant;

public class FriendRequestServiceImpl extends UnicastRemoteObject implements FriendRequestService {

    public FriendRequestServiceImpl() throws RemoteException {}

    @Override
    public int sendFriendRequest(Users sender, Users receiver) throws RemoteException {
        FriendsImpl friendsImpl = new FriendsImpl();
        Friend friendRequest = new Friend(sender.getId(), receiver.getId(), Timestamp.from(Instant.now()),
                null, FriendStatus.PENDING);

        return friendsImpl.insert(friendRequest);
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
}
