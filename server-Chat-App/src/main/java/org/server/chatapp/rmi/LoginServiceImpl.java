package org.server.chatapp.rmi;

import dto.UserLoginDTO;
import model.Users;
import org.server.chatapp.dao.dao.UsersDao;
import org.server.chatapp.dao.implement.UsersImpl;
import org.server.chatapp.util.PasswordUtil;
import rmi.LoginService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class LoginServiceImpl extends UnicastRemoteObject implements LoginService {
    private final UsersDao usersDao;

    public LoginServiceImpl() throws RemoteException {
        super();
        this.usersDao = new UsersImpl();
    }

    public Users getUserByPhoneNumber(UserLoginDTO userLoginDTO) {
        UsersImpl users = new UsersImpl();
        return users.getUserByPhoneNumber(userLoginDTO.getPhoneNumber());
    }

    @Override
    public boolean login(String phoneNumber, String password) throws RemoteException {
        System.out.println("Not Here");
        if(usersDao.isPhoneNumberExists(phoneNumber))
        {
            System.out.println("Here");
            Users user = usersDao.getUserByPhoneNumber(phoneNumber);
            return PasswordUtil.verifyPassword(password, user.getPassword());
        }
        return false;
    }
}
