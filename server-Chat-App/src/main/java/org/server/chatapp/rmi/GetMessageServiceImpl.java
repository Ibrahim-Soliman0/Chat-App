package org.server.chatapp.rmi;

import dto.ChatRoomDTO;
import model.Message;
import model.Users;
import org.server.chatapp.dao.ClientManager;
import org.server.chatapp.dao.implement.MessageDaoImpl;
import rmi.ClientCallBack;
import rmi.GetMessageService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class GetMessageServiceImpl extends UnicastRemoteObject implements GetMessageService {

    private final MessageDaoImpl messageDao;

    public GetMessageServiceImpl() throws RemoteException {
        super();
        this.messageDao = new MessageDaoImpl();
    }

    @Override
    public List<Message> getRoomMessages(Long roomId) throws RemoteException {
        try {
            return messageDao.getMessagesByRoomId(roomId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Failed to retrieve messages for room: " + roomId, e);
        }
    }

    @Override
    public long sendMessage(Message message) throws RemoteException {
        try {
            return messageDao.insert(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void updateOthersGUI(ChatRoomDTO chatRoomDTO) throws RemoteException {
        ClientCallBack clientCallBack = ClientManager.getClient(chatRoomDTO.getOther().getPhoneNumber());
        clientCallBack.receiveMessage(chatRoomDTO);
    }
}