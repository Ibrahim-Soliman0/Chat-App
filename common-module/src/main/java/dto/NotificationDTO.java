package dto;

import model.Notification;
import model.Room;
import model.Users;

import java.io.Serializable;

public class NotificationDTO implements Serializable {
    private Notification notification;
    private Users sender;
    private Room room;
    private String name;

    public NotificationDTO(Notification type, Users sender, Room room, String name) {
        this.notification = type;
        this.sender = sender;
        this.room = room;
        this.name = name;
    }

    public Users getSender() {
        return sender;
    }

    public void setSender(Users sender) {
        this.sender = sender;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
