package rmi;

import model.Users;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RegisterService extends Remote {
    Users register(Users user) throws RemoteException;
}
