package org.server.chatapp.rmi;

import model.Users;
import model.enums.Status;
import org.server.chatapp.dao.dao.UsersDao;
import org.server.chatapp.dao.implement.UsersImpl;
import org.server.chatapp.util.PasswordUtil;
import rmi.RegisterService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RegisterServiceImpl extends UnicastRemoteObject implements RegisterService {
    private final UsersDao usersDao;
    public RegisterServiceImpl() throws RemoteException {
        super();
        this.usersDao = new UsersImpl();
    }
    @Override
    public Users register(Users user) throws RemoteException {
        try {
            if (usersDao.isPhoneNumberExists(user.getPhoneNumber())) {
                throw new RemoteException("Phone number already exists");
            }
            if (usersDao.isEmailExists(user.getEmail())) {
                throw new RemoteException("Email already exists");
            }

            String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
            user.setPassword(hashedPassword);
            user.setStatus(Status.OFFLINE);
            user.setLastSeen(null);
            int result = usersDao.insert(user);

            if (result > 0) {
                user.setPassword(null);
                return user;
            } else {
                throw new RemoteException("Failed to register user");
            }
        } catch (Exception e) {
            throw new RemoteException("Registration error: " + e.getMessage());
        }
    }
}