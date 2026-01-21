package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class MessageStatus implements Serializable {
    private long id;
    private long messageId;
    private long userId;
    private Timestamp seenAt;

    public MessageStatus() {}

    public MessageStatus(long id, long messageId, long userId, Timestamp seenAt) {
        this.id = id;
        this.messageId = messageId;
        this.userId = userId;
        this.seenAt = seenAt;
    }

    public long getId() { return id; }

    public void setId(long id) {
        this.id = id;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Timestamp getSeenAt() {
        return seenAt;
    }

    public void setSeenAt(Timestamp seenAt) {
        this.seenAt = seenAt;
    }

    @Override
    public String toString() {
        return "MessageStatus{" +
                "id=" + id +
                ", messageId=" + messageId +
                ", userId=" + userId +
                ", seenAt=" + seenAt +
                '}';
    }
}
