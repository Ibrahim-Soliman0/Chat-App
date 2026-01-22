package rmi;

import model.Users;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GetUserService extends Remote {

    Users getUser(Long userId) throws RemoteException;
}
