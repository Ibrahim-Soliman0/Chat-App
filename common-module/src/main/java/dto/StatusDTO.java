package dto;

import model.enums.Status;

import java.io.Serializable;

public class StatusDTO implements Serializable {
    private long userId;
    private Status status;

    public StatusDTO() {
    }

    public StatusDTO(long userId, Status status) {
        this.userId = userId;
        this.status = status;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
