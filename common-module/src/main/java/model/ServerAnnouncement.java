package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ServerAnnouncement implements Serializable {
    private Long id;
    private String title;
    private String content;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime expireAt;
    private Boolean isActive;

    public ServerAnnouncement() {
    }

    public ServerAnnouncement(Long id, String title, String content, String createdBy, LocalDateTime createdAt, LocalDateTime expireAt, Boolean isActive) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.expireAt = expireAt;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(LocalDateTime expireAt) {
        this.expireAt = expireAt;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "ServerAnnouncement{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdAt=" + createdAt +
                ", expireAt=" + expireAt +
                ", isActive=" + isActive +
                '}';
    }
}
