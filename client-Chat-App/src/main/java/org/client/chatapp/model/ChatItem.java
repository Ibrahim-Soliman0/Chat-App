package org.client.chatapp.model;

import java.time.LocalDateTime;
import java.util.List;

public class ChatItem {

    private String name, lastMessage;
    private boolean incoming;
    private LocalDateTime messageTime;
    private List<Long> unreadMessageIds;

    public ChatItem(String name, String lastMessage, boolean incoming,
                    LocalDateTime messageTime, List<Long> unreadMessageIds) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.incoming = incoming;
        this.messageTime = messageTime;
        this.unreadMessageIds = unreadMessageIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public boolean isIncoming() {
        return incoming;
    }

    public void setIncoming(boolean incoming) {
        this.incoming = incoming;
    }

    public LocalDateTime getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(LocalDateTime messageTime) {
        this.messageTime = messageTime;
    }

    public List<Long> getUnreadMessageIds() {
        return unreadMessageIds;
    }

    public void setUnreadMessageIds(List<Long> unreadMessageIds) {
        this.unreadMessageIds = unreadMessageIds;
    }

    @Override
    public String toString() {
        return "ChatItem{" +
                "name='" + name + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", incoming=" + incoming +
                ", messageTime=" + messageTime +
                ", unreadMessageIds=" + unreadMessageIds +
                '}';
    }
}
