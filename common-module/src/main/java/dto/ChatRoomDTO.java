package dto;

import model.Message;
import model.Room;
import model.UserRooms;
import model.Users;

import java.io.Serializable;
import java.util.List;

public class ChatRoomDTO implements Serializable {

    //? if the other object is null this means it's a group chat
    //? so the list of users will be filled with the group members instead
    private Users me, other;
    private List<Users> groupMembers;
    private UserRooms userRoom;
    private Room room;
    private Message lastMessage;

    public ChatRoomDTO() {
    }

    public ChatRoomDTO(Users me, UserRooms userRoom, Room room) {
        this.me = me;
        this.userRoom = userRoom;
        this.room = room;
    }

    public ChatRoomDTO(Users me, Users other, UserRooms userRoom, Room room, Message lastMessage) {
        this.me = me;
        this.other = other;
        this.userRoom = userRoom;
        this.room = room;
        this.lastMessage = lastMessage;
    }

    public ChatRoomDTO(Users me, List<Users> groupMembers, UserRooms userRoom, Room room, Message lastMessage) {
        this.me = me;
        this.groupMembers = groupMembers;
        this.userRoom = userRoom;
        this.room = room;
        this.lastMessage = lastMessage;
    }

    public Users getMe() {
        return me;
    }

    public void setMe(Users me) {
        this.me = me;
    }

    public Users getOther() {
        return other;
    }

    public void setOther(Users other) {
        this.other = other;
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

    public List<Users> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<Users> groupMembers) {
        this.groupMembers = groupMembers;
    }
}
