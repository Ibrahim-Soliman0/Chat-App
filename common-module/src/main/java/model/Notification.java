package model;

import model.enums.NotificationStatus;
import model.enums.NotificationType;

import java.io.Serializable;
import java.sql.Timestamp;

public class Notification implements Serializable {
    private long id;
    private long receiverId;
    private NotificationType type;
    private String content;
    private Long friendId;
    private Timestamp createdAt;
    private NotificationStatus status;
    private Long roomId;


    public Notification() {}

    public Notification(long receiverId, NotificationType type, String content,
                        Long friendId, Timestamp createdAt, NotificationStatus status, Long roomId) {
        this.receiverId = receiverId;
        this.type = type;
        this.content = content;
        this.friendId = friendId;
        this.createdAt = createdAt;
        this.status = status;
        this.roomId = roomId;
    }

    public Notification(long id, long receiverId, NotificationType type, String content,
                        Long friendId, Timestamp createdAt, NotificationStatus status, Long roomId) {
        this.id = id;
        this.receiverId = receiverId;
        this.type = type;
        this.content = content;
        this.friendId = friendId;
        this.createdAt = createdAt;
        this.status = status;
        this.roomId = roomId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) { this.id = id; }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) { this.status = status; }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", receiverId=" + receiverId +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", friendId=" + friendId +
                ", createdAt=" + createdAt +
                ", status=" + status +
                ", roomId=" + roomId +
                '}';
    }
}