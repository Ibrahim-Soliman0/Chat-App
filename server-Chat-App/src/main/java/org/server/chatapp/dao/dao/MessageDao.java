package org.server.chatapp.dao.dao;


import model.Message;
import model.Room;
import model.Users;

import java.rmi.RemoteException;
import java.util.List;

public interface MessageDao extends Dao<Message> {
    List<Message> getMessagesByRoomId(long roomId);

    Message getLastMessageInRoom(long roomId);

    int getUnreadMessagesCount(Users user, Room room);

    List<Message> getUnreadMessages(Users user, Room room);
}