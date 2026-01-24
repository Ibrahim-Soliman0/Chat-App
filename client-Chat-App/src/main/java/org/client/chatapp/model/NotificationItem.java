package org.client.chatapp.model;

import model.enums.NotificationType;

import java.time.LocalDateTime;

public class NotificationItem {

    private NotificationType type;
    private String name;
    private String message;
    private LocalDateTime time;
    private boolean read;
    private String profilePic;

    public NotificationItem(NotificationType type, String name, String message, LocalDateTime time, boolean read) {
        this.type = type;
        this.name = name;
        this.message = message;
        this.time = time;
        this.read = read;
        this.profilePic = "defaultProfilePic.png";
    }

    public NotificationItem(NotificationType type, String name, String message, LocalDateTime time, boolean read, String profilePic) {
        this.type = type;
        this.name = name;
        this.message = message;
        this.time = time;
        this.read = false;
        this.profilePic = profilePic;

    }

    public NotificationType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public boolean isUnread() {
        return !read;
    }

    public void setRead(boolean read) {
        this.read = !read;
    }

    public String getProfilePic() {
        return profilePic;
    }
}

