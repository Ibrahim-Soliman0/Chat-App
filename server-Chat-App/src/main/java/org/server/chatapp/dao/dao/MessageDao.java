package org.server.chatapp.dao.dao;

import org.server.chatapp.model.Message;

import java.util.List;

public interface MessageDao extends Dao<Message>{
    List<Message> getMessagesByRoomId(long roomId);
}