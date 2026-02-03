package org.server.chatapp.dao.dao;

import model.Message;
import model.MessageStatus;
import model.Users;

import java.util.List;

public interface MessageStatusDao extends Dao<MessageStatus> {
    List<MessageStatus> getByMessageId(long messageId);

    MessageStatus getMessageStatusByUserAndRoom(Users user, Message message);
}
