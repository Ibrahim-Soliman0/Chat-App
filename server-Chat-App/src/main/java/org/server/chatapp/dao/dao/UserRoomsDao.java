package org.server.chatapp.dao.dao;

import dto.ChatRoomDTO;
import model.UserRooms;
import model.Users;

import java.util.List;

public interface UserRoomsDao extends Dao<UserRooms>{

    List<ChatRoomDTO> getUserRooms(Users me);

    Users getSingleUserInRoom(ChatRoomDTO chatRoomDTO);

    List<Users> getUsersInRoom(ChatRoomDTO chatRoomDTO);
}
