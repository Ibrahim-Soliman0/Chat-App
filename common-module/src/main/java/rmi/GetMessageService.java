package rmi;

import dto.ChatRoomDTO;
import model.Message;
import model.Users;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GetMessageService extends Remote {
    List<Message> getRoomMessages(Long roomId) throws RemoteException;
    long sendMessage(Message message) throws RemoteException;
    void updateOthersGUI(ChatRoomDTO chatRoomDTO) throws RemoteException;
}