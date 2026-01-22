package org.server.chatapp.rmi;

import model.Users;
import org.server.chatapp.dao.implement.UsersImpl;
import rmi.GetUserService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GetUserServiceImpl extends UnicastRemoteObject implements GetUserService {

    public GetUserServiceImpl() throws RemoteException {}

    @Override
    public Users getUser(Long userId) throws RemoteException {

        UsersImpl users = new UsersImpl();

        return users.get(userId);
    }
}
