package org.server.chatapp.rmi;

import model.Users;
import model.enums.Status;
import org.server.chatapp.dao.dao.UsersDao;
import org.server.chatapp.dao.implement.UsersImpl;
import org.server.chatapp.util.PasswordUtil;
import rmi.RegisterService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RegisterServiceImpl extends UnicastRemoteObject implements RegisterService {
    private final UsersDao usersDao;
    private final String UPLOAD_DIR = System.getProperty("user.dir")
            + File.separator + "server-Chat-App"
            + File.separator + "uploads"
            + File.separator + "profiles";
    public RegisterServiceImpl() throws RemoteException {
        super();
        this.usersDao = new UsersImpl();
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
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
            if (user.getPictureBytes() != null && user.getPictureBytes().length > 0) {
                String fileName = user.getPhoneNumber() + "_" + System.currentTimeMillis() + ".jpg";
                File destinationFile = new File(UPLOAD_DIR, fileName);
                String relativePath = "uploads" + File.separator + "profiles" + File.separator + fileName;

                try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
                    fos.write(user.getPictureBytes());
                    user.setPicturePath(relativePath);
                } catch (IOException e) {
                    throw new IOException("Failed to save image");
                }
            }

            String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
            user.setPassword(hashedPassword);
            user.setStatus(Status.OFFLINE);
            user.setLastSeen(null);
            int result = usersDao.insert(user);

            if (result > 0) {
                user.setPassword(null);
                user.setPictureBytes(null);
                return user;
            } else {
                throw new RemoteException("Failed to register user");
            }
        } catch (Exception e) {
            throw new RemoteException("Registration error: " + e.getMessage());
        }
    }
}