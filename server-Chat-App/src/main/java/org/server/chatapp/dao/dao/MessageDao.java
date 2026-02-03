package org.server.chatapp.dao.dao;


import model.Message;
import model.Room;
import model.Users;

import java.util.List;

public interface MessageDao extends Dao<Message> {
    List<Message> getMessagesByRoomId(long roomId);

    Message getLastMessageInRoom(long roomId);

    List<Long> getUnreadMessagesIds(Users user, Room room);

    List<Message> getUnreadMessages(Users user, Room room);
}