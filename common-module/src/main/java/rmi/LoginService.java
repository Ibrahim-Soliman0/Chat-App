package rmi;

import dto.UserLoginDTO;
import model.Users;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LoginService extends Remote {

    Users getUserByPhoneNumber(UserLoginDTO userLoginDTO) throws RemoteException;

    Users login(String phoneNumber, String password, ClientCallBack callBack) throws RemoteException;
    void logout(String phoneNumber) throws RemoteException;

    void broadcastAnnouncement(String title,String htmlContent) throws RemoteException;
    byte[] getUserProfilePicture(String phoneNumber) throws RemoteException;
}
