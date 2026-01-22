package org.server.chatapp.rmi;

import dto.GetMyFriendsListDTO;
import model.Friend;
import org.server.chatapp.dao.implement.FriendsImpl;
import rmi.LoadFriendsListService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class LoadFriendsListServiceImpl extends UnicastRemoteObject implements LoadFriendsListService {

    public LoadFriendsListServiceImpl() throws RemoteException {}

    @Override
    public List<Friend> getUserFriendsList(GetMyFriendsListDTO getMyFriendsListDTO) throws RemoteException {

        FriendsImpl userFriends = new FriendsImpl();

        return userFriends.getUserFriendsList(getMyFriendsListDTO.getId());
    }
}
