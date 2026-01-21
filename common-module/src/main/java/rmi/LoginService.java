package rmi;

import dto.UserLoginDTO;
import model.Users;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LoginService extends Remote {

    Users getUserByPhoneNumber(UserLoginDTO userLoginDTO) throws RemoteException;
}
