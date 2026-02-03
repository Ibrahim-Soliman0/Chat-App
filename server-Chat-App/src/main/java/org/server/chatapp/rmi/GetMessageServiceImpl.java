package org.server.chatapp.rmi;

import dto.ChatRoomDTO;
import dto.MessageStatusDTO;
import model.Message;
import model.MessageStatus;
import model.Users;
import org.server.chatapp.dao.ClientManager;
import org.server.chatapp.dao.implement.*;
import rmi.ClientCallBack;
import rmi.GetMessageService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    public void sendMessage(Message message) throws RemoteException {
        messageDao.insert(message);
        MessageStatusDaoImpl messageStatusDao = new MessageStatusDaoImpl();

        UsersImpl usersImpl = new UsersImpl();
        RoomImpl roomImpl = new RoomImpl();
        UserRoomsImpl userRoomsImpl = new UserRoomsImpl();
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
        chatRoomDTO.setMe(usersImpl.get(message.getSenderId()));
        chatRoomDTO.setRoom(roomImpl.get(message.getRoomId()));

        Users users = userRoomsImpl.getSingleUserInRoom(chatRoomDTO);

        if (users != null) {
            MessageStatus messageStatus = new MessageStatus(message.getId(), users.getId());
            messageStatusDao.insert(messageStatus);
        }
        else {
            List<Users> usersList = userRoomsImpl.getUsersInRoom(chatRoomDTO);
            for (Users user : usersList) {
                MessageStatus messageStatus = new MessageStatus(message.getId(), user.getId());
                messageStatusDao.insert(messageStatus);
            }
        }
    }

    @Override
    public void updateOthersGUI(ChatRoomDTO chatRoomDTO) throws RemoteException {
        ClientCallBack clientCallBack = ClientManager.getClient(chatRoomDTO.getOther().getPhoneNumber());
        if (clientCallBack == null)
            return;
        try {
            clientCallBack.receiveMessage(chatRoomDTO);
            clientCallBack.updateHomeScreenChat();
        } catch (RemoteException e) {
            ClientManager.removeClient(chatRoomDTO.getOther().getPhoneNumber());
        }
    }

    @Override
    public void setMessageStatusAsSeen(MessageStatusDTO messageStatusDTO) throws RemoteException {
        MessageStatusDaoImpl messageStatusDao = new MessageStatusDaoImpl();
        MessageStatus messageStatus = messageStatusDao.getMessageStatusByUserAndRoom(
                messageStatusDTO.getUser(), messageStatusDTO.getMessage());
        messageStatus.setSeenAt(Timestamp.valueOf(messageStatusDTO.getSeenAt()));

        messageStatusDao.update(messageStatus);
    }
}