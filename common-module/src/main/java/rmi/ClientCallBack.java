package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallBack extends Remote {
    void receiveAnnouncement(String title,String htmlContent) throws RemoteException;
}
