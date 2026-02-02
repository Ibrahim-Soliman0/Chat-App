package org.server.chatapp.rmi;

import dto.BidirectionalFriendStatusDTO;
import dto.ChatRoomDTO;
import model.Friend;
import model.Message;
import model.Room;
import model.Users;
import model.enums.RoomType;
import org.server.chatapp.dao.implement.FriendsImpl;
import org.server.chatapp.dao.implement.MessageDaoImpl;
import org.server.chatapp.dao.implement.UserRoomsImpl;
import org.server.chatapp.dao.implement.UsersImpl;
import rmi.GetUserService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class GetUserServiceImpl extends UnicastRemoteObject implements GetUserService {

    public GetUserServiceImpl() throws RemoteException {}

    @Override
    public Users getUser(Long userId) throws RemoteException {

        UsersImpl users = new UsersImpl();

        return users.get(userId);
    }

    @Override
    public List<Users> searchUsersByPhoneNumber(String phoneNumber, long searchingUserId) throws RemoteException {
        UsersImpl user = new UsersImpl();

        return user.searchUsersByPhoneNumber(phoneNumber, searchingUserId);
    }

    @Override
    public BidirectionalFriendStatusDTO getUserFriendStatus(Users me, Users other) throws RemoteException {

        FriendsImpl friend = new FriendsImpl();

        Friend meToOther = friend.getUserFriendStatus(me.getId(), other.getId());
        Friend otherToMe = friend.getUserFriendStatus(other.getId(), me.getId());

        return new BidirectionalFriendStatusDTO(meToOther, otherToMe);
    }

    @Override
    public List<ChatRoomDTO> getUserRooms(Users me) throws RemoteException {

        UserRoomsImpl userRoomsImpl = new UserRoomsImpl();
        MessageDaoImpl messageDao = new MessageDaoImpl();
        List<ChatRoomDTO> userRooms = userRoomsImpl.getUserRooms(me);
        List<ChatRoomDTO> userRoomsWithLastMessage = userRooms.stream()
                .map(chatRoom ->
                {
                    if (chatRoom.getRoom().getType() == RoomType.ONE_TO_ONE)
                        return new ChatRoomDTO(me, userRoomsImpl.getSingleUserInRoom(chatRoom),
                                chatRoom.getUserRoom(), chatRoom.getRoom(),
                                messageDao.getLastMessageInRoom(chatRoom.getRoom().getId()));

                    return new ChatRoomDTO(me, userRoomsImpl.getUsersInRoom(chatRoom),
                            chatRoom.getUserRoom(), chatRoom.getRoom(),
                            messageDao.getLastMessageInRoom(chatRoom.getRoom().getId()));
                })
                .toList();

        return userRoomsWithLastMessage;
    }

    @Override
    public Users getSingleUserInRoom(ChatRoomDTO chatRoomDTO) throws RemoteException {
        UserRoomsImpl userRoomsImpl = new UserRoomsImpl();
        return userRoomsImpl.getSingleUserInRoom(chatRoomDTO);
    }

    @Override
    public List<Users> getUsersInRoom(ChatRoomDTO chatRoomDTO) throws RemoteException {
        UserRoomsImpl userRoomsImpl = new UserRoomsImpl();
        return userRoomsImpl.getUsersInRoom(chatRoomDTO);
    }

    @Override
    public int getUnreadMessagesCount(Users user, Room room) throws RemoteException {
        MessageDaoImpl messageDao = new MessageDaoImpl();
        return messageDao.getUnreadMessagesCount(user, room);
    }

    @Override
    public List<Message> getUnreadMessages(Users user, Room room) throws RemoteException {
        MessageDaoImpl messageDao = new MessageDaoImpl();
        return messageDao.getUnreadMessages(user, room);
    }

    @Override
    public void updateUser(Users user) throws RemoteException {
        UsersImpl usersImpl = new UsersImpl();
        usersImpl.update(user);
    }
}
