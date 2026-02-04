package rmi;

import dto.ChatRoomDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallBack extends Remote {
    void receiveAnnouncement(String title, String htmlContent) throws RemoteException;

    void receiveMessage(ChatRoomDTO chatRoomDTO) throws RemoteException;

    void receiveNotification() throws RemoteException;

    void updateHomeScreenChat() throws RemoteException;
}
