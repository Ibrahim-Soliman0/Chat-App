package dto;

import model.Message;
import model.Users;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MessageStatusDTO implements Serializable {
    Users user;
    Message message;
    LocalDateTime seenAt;
    Long roomId;

    public MessageStatusDTO() {
    }

    public MessageStatusDTO(Users user, Message message, LocalDateTime seenAt, Long roomId) {
        this.user = user;
        this.message = message;
        this.seenAt = seenAt;
        this.roomId = roomId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public LocalDateTime getSeenAt() {
        return seenAt;
    }

    public void setSeenAt(LocalDateTime seenAt) {
        this.seenAt = seenAt;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}
