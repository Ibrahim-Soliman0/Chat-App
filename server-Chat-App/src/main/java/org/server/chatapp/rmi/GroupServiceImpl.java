package org.server.chatapp.rmi;

import dto.GroupDTO;
import model.Room;
import model.UserRooms;
import model.enums.RoomType;
import org.server.chatapp.dao.dao.RoomDao;
import org.server.chatapp.dao.dao.UserRoomsDao;
import org.server.chatapp.dao.implement.RoomImpl;
import org.server.chatapp.dao.implement.UserRoomsImpl;
import rmi.GroupService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;

public class GroupServiceImpl extends UnicastRemoteObject implements GroupService {
    private RoomDao roomDao;
    private UserRoomsDao userRoomsDao;

    public GroupServiceImpl() throws RemoteException {
        super();
        this.roomDao = new RoomImpl();
        this.userRoomsDao = new UserRoomsImpl();
    }

    @Override
    public Room createGroup(GroupDTO groupDTO) throws RemoteException {
        Room room = new Room();
        room.setName(groupDTO.getGroupName());
        room.setDescription(groupDTO.getDescription());
        room.setType(RoomType.GROUP);
        room.setCreatedBy(groupDTO.getCreatorId());
        room.setCreatedAt(LocalDateTime.now());
        room.setLastMessageAt(null);

        long roomId = roomDao.insert(room);

        groupDTO.getMembersId().forEach(userId -> {
            UserRooms member = new UserRooms();
            member.setRoomId(roomId);
            member.setUserId(userId);
            member.setJoinedAt(LocalDateTime.now());
            member.setIsActive(true);
            member.setLeftAt(null);
            member.setIsAdmin(userId.equals(groupDTO.getCreatorId()));

            userRoomsDao.insert(member);
        });

        return room;
    }
}
