package org.server.chatapp.dao.dao;


import model.Message;

import java.util.List;

public interface MessageDao extends Dao<Message>{
    List<Message> getMessagesByRoomId(long roomId);
}