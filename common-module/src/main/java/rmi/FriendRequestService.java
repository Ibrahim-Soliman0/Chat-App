package rmi;

import model.Users;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FriendRequestService extends Remote {

    int sendFriendRequest(Users sender, Users receiver) throws RemoteException;
    int acceptFriendRequest(Users sender, Users receiver) throws RemoteException;
    int cancelFriendRequest(Users sender, Users receiver) throws RemoteException;
}
