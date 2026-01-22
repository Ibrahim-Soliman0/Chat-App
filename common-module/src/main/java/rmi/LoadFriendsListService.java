package rmi;

import dto.GetMyFriendsListDTO;
import model.Friend;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface LoadFriendsListService extends Remote {

    List<Friend> getUserFriendsList(GetMyFriendsListDTO getMyFriendsListDTO) throws RemoteException;
}
