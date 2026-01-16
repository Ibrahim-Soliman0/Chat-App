package org.server.chatapp.model;

import java.time.LocalDateTime;

public class UserRooms {

    private Long id, userId, roomId;
    private Boolean isAdmin;
    private LocalDateTime joinedAt, leftAt;
    private Boolean isActive;

    public UserRooms() {
    }

    public UserRooms(Long userId, Long roomId, Boolean isAdmin,
                     LocalDateTime joinedAt, LocalDateTime leftAt, Boolean isActive) {
        this.userId = userId;
        this.roomId = roomId;
        this.isAdmin = isAdmin;
        this.joinedAt = joinedAt;
        this.leftAt = leftAt;
        this.isActive = isActive;
    }

    public UserRooms(Long id, Long userId, Long roomId, Boolean isAdmin,
                     LocalDateTime joinedAt, LocalDateTime leftAt, Boolean isActive) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.isAdmin = isAdmin;
        this.joinedAt = joinedAt;
        this.leftAt = leftAt;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public LocalDateTime getLeftAt() {
        return leftAt;
    }

    public void setLeftAt(LocalDateTime leftAt) {
        this.leftAt = leftAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}
