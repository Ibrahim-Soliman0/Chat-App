package org.client.chatapp.model;

import java.time.LocalDateTime;

public class ChatItem {

    private String name, lastMessage;
    private boolean incoming;
    private LocalDateTime messageTime;
    private int unreadMessageCount;


    public ChatItem(String name, String lastMessage, boolean incoming,
                    LocalDateTime messageTime, int unreadMessageCount) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.incoming = incoming;
        this.messageTime = messageTime;
        this.unreadMessageCount = unreadMessageCount;
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

    public int getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    @Override
    public String toString() {
        return "ChatItem{" +
                "name='" + name + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", incoming=" + incoming +
                ", messageTime=" + messageTime +
                ", unreadMessageCount=" + unreadMessageCount +
                '}';
    }
}
