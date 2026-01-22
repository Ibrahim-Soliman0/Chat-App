package model;

import model.enums.FriendStatus;

import java.io.Serializable;
import java.sql.Timestamp;

public class Friend implements Serializable {

    long id, senderUserId, receiverUserId;
    Timestamp requestDate, responseDate;
    FriendStatus status;

    public Friend(long senderUserId, long receiverUserId, Timestamp requestDate,
                  Timestamp responseDate, FriendStatus status) {
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
        this.requestDate = requestDate;
        this.responseDate = responseDate;
        this.status = status;
    }

    public Friend(long id, long senderUserId, long receiverUserId, Timestamp requestDate,
                  Timestamp responseDate, FriendStatus status) {
        this.id = id;
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
        this.requestDate = requestDate;
        this.responseDate = responseDate;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(long senderUserId) {
        this.senderUserId = senderUserId;
    }

    public long getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(long receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public Timestamp getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Timestamp requestDate) {
        this.requestDate = requestDate;
    }

    public Timestamp getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Timestamp responseDate) {
        this.responseDate = responseDate;
    }

    public FriendStatus getStatus() {
        return status;
    }

    public void setStatus(FriendStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", senderUserId=" + senderUserId +
                ", receiverUserId=" + receiverUserId +
                ", requestDate=" + requestDate +
                ", responseDate=" + responseDate +
                ", status=" + status +
                '}';
    }
}
