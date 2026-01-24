package rmi;

import dto.BidirectionalFriendStatusDTO;
import model.Users;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GetUserService extends Remote {

    Users getUser(Long userId) throws RemoteException;
    List<Users> searchUsersByPhoneNumber(String phoneNumber, long searchingUserId) throws RemoteException;
    BidirectionalFriendStatusDTO getUserFriendStatus(Users me, Users other) throws RemoteException;
}
