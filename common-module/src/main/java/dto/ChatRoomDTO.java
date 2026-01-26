package dto;

import model.Message;
import model.Room;
import model.UserRooms;
import model.Users;

import java.io.Serializable;

public class ChatRoomDTO implements Serializable {

    private Users user;
    private UserRooms userRoom;
    private Room room;
    private Message lastMessage;

    public ChatRoomDTO() {
    }

    public ChatRoomDTO(Users user, UserRooms userRoom, Room room) {
        this.user = user;
        this.userRoom = userRoom;
        this.room = room;
    }

    public ChatRoomDTO(Users user, UserRooms userRoom, Room room, Message lastMessage) {
        this.user = user;
        this.userRoom = userRoom;
        this.room = room;
        this.lastMessage = lastMessage;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public UserRooms getUserRoom() {
        return userRoom;
    }

    public void setUserRoom(UserRooms userRoom) {
        this.userRoom = userRoom;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
}
