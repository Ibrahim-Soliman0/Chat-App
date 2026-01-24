package org.client.chatapp.model;

import java.time.LocalDateTime;

public class ChatItem {

    private String name, lastMessage, profilePic;
    private boolean incoming;
    private LocalDateTime messageTime;
    private int unreadMessageCount;

    public ChatItem(int unreadMessageCount, LocalDateTime messageTime, String lastMessage, String name) {
        this(name, lastMessage, true, messageTime, unreadMessageCount, "defaultProfilePic.png");
    }

    public ChatItem(String name, String lastMessage, boolean incoming,
                    LocalDateTime messageTime, int unreadMessageCount) {
        this(name, lastMessage, incoming, messageTime,
                unreadMessageCount, "defaultProfilePic.png");
    }

    public ChatItem(String name, String lastMessage, boolean incoming,
                    LocalDateTime messageTime, int unreadMessageCount, String profilePic) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.incoming = incoming;
        this.messageTime = messageTime;
        this.unreadMessageCount = unreadMessageCount;
        this.profilePic = profilePic;
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    @Override
    public String toString() {
        return "ChatItem{" +
                "profilePic=" + profilePic +
                ", name='" + name + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", incoming=" + incoming +
                ", messageTime=" + messageTime +
                ", unreadMessageCount=" + unreadMessageCount +
                '}';
    }
}
