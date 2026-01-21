package org.server.chatapp.dao.dao;

import model.MessageStatus;

import java.util.List;

public interface MessageStatusDao extends Dao<MessageStatus> {
    List<MessageStatus> getByMessageId(long messageId);
}
