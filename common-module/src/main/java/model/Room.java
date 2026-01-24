package model;

import model.enums.RoomType;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Room implements Serializable {
    private Long id;
    private RoomType type;
    private String name;
   private String description;
   private String picturePath;
   private LocalDateTime createdAt;
   private LocalDateTime lastMessageAt;
   private Long createdBy;

   // private List<Message> messages = new ArrayList<>();


    public Room() {
    }

    public Room(Long id, RoomType type, String name, String description, String picturePath, LocalDateTime createdAt, LocalDateTime lastMessageAt, Long createdBy) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.picturePath = picturePath;
        this.createdAt = createdAt;
        this.lastMessageAt = lastMessageAt;
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastMessageAt() {
        return lastMessageAt;
    }

    public void setLastMessageAt(LocalDateTime lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", picturePath='" + picturePath + '\'' +
                ", createdAt=" + createdAt +
                ", lastMessageAt=" + lastMessageAt +
                ", createdBy=" + createdBy +
                '}';
    }
}
