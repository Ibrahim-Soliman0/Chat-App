package rmi;

import model.Message;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GetMessageService extends Remote {
    List<Message> getRoomMessages(Long roomId) throws RemoteException;
}