package org.server.chatapp.rmi;

import dto.UserLoginDTO;
import model.Users;
import org.server.chatapp.dao.implement.UsersImpl;
import rmi.LoginService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class LoginServiceImpl extends UnicastRemoteObject implements LoginService {

    public LoginServiceImpl() throws RemoteException {}

    public Users getUserByPhoneNumber(UserLoginDTO userLoginDTO) {
        UsersImpl users = new UsersImpl();
        return users.getUserByPhoneNumber(userLoginDTO.getPhoneNumber());
    }
}
