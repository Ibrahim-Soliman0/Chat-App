package org.client.chatapp.model;

import model.enums.NotificationType;

import java.time.LocalDateTime;

public class NotificationItem {
    private Long id;

    private NotificationType type;
    private String name;
    private String message;
    private LocalDateTime time;
    private boolean read;
    private String profilePic;

    public NotificationItem(Long id,
                            NotificationType type,
                            String name,
                            String message,
                            LocalDateTime time,
                            boolean read,
                            String profilePic) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.message = message;
        this.time = time;
        this.read = read;
        this.profilePic = profilePic != null ? profilePic : "defaultProfilePic.png";
    }

    public NotificationItem(Long id,
                            NotificationType type,
                            String name,
                            String message,
                            LocalDateTime time,
                            boolean read) {
        this(id, type, name, message, time, read, "defaultProfilePic.png");
    }

    public Long getId() {
        return id;
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

    public boolean isRead() {
        return read;
    }
     public void setRead(boolean read) { this.read = !read; }

    public String getProfilePic() {
        return profilePic;
    }

    public void markAsRead() {
        this.read = true;
    }

    public void markAsUnread() {
        this.read = false;
    }
}
