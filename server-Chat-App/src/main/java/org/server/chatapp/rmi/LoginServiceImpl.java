package org.server.chatapp.rmi;

import dto.UserLoginDTO;
import model.Users;
import model.enums.Status;
import org.server.chatapp.dao.ClientManager;
import org.server.chatapp.dao.dao.UsersDao;
import org.server.chatapp.dao.implement.UsersImpl;
import org.server.chatapp.util.PasswordUtil;
import rmi.ClientCallBack;
import rmi.LoginService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    public Users login(String phoneNumber, String password, ClientCallBack callBack) throws RemoteException {
        if (usersDao.isPhoneNumberExists(phoneNumber)) {
            Users user = usersDao.getUserByPhoneNumber(phoneNumber);
            if (PasswordUtil.verifyPassword(password, user.getPassword())) {
                ClientManager.addClient(phoneNumber, callBack);
                usersDao.updateStatus(phoneNumber , Status.ONLINE);
                return user;
            } else
                return null;
        }
        return null;
    }

    @Override
    public void logout(String phoneNumber) throws RemoteException {
        ClientManager.removeClient(phoneNumber);
        usersDao.updateStatus(phoneNumber , Status.OFFLINE);

    }

    @Override
    public void broadcastAnnouncement(String title, String htmlContent) throws RemoteException {
        ClientManager.getAllOnlineClients().forEach((phone, clientCallBack) -> {
            try {
                Users user = usersDao.getUserByPhoneNumber(phone);
                if (user != null && user.getStatus() != Status.OFFLINE) {
                    clientCallBack.receiveAnnouncement(title, htmlContent);
                }
            } catch (RemoteException e) {
                System.out.println("Failed to reach " + phone);
                ClientManager.removeClient(phone);
            }
        });

    }

    @Override
    public byte[] getUserProfilePicture(String phoneNumber) throws RemoteException {
        Users user = usersDao.getUserByPhoneNumber(phoneNumber);
        if (user != null && user.getPicturePath() != null) {
            String fullPath = System.getProperty("user.dir") + File.separator + "server-Chat-App" + File.separator + user.getPicturePath();
            File imageFile = new File(fullPath);

            if (imageFile.exists()) {
                try {
                    return Files.readAllBytes(imageFile.toPath());
                } catch (IOException e) {
                    System.err.println("Error reading image file: " + e.getMessage());
                }
            }
        }
        return null;
    }
}