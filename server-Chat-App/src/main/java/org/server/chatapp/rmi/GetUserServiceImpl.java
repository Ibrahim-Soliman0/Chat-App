package org.server.chatapp.rmi;

import dto.BidirectionalFriendStatusDTO;
import dto.ChatRoomDTO;
import dto.StatusDTO;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class GetUserServiceImpl extends UnicastRemoteObject implements GetUserService {
    private final String UPLOAD_DIR = System.getProperty("user.dir")
            + File.separator + "server-Chat-App"
            + File.separator + "uploads"
            + File.separator + "profiles";

    public GetUserServiceImpl() throws RemoteException {
    }

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
    public List<Long> getUnreadMessagesIds(Users user, Room room) throws RemoteException {
        MessageDaoImpl messageDao = new MessageDaoImpl();
        return messageDao.getUnreadMessagesIds(user, room);
    }

    @Override
    public List<Message> getUnreadMessages(Users user, Room room) throws RemoteException {
        MessageDaoImpl messageDao = new MessageDaoImpl();
        return messageDao.getUnreadMessages(user, room);
    }

    @Override
    public void updateUser(Users user) throws RemoteException {
        try {
            if (user.getPictureBytes() != null && user.getPictureBytes().length > 0) {
                String fileName = user.getPhoneNumber() + "_" + System.currentTimeMillis() + ".jpg";
                File destinationFile = new File(UPLOAD_DIR, fileName);

                String relativePath = "server-Chat-App" + File.separator + "uploads" + File.separator + "profiles" + File.separator + fileName;

                try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
                    fos.write(user.getPictureBytes());
                    user.setPicturePath(relativePath);
                }
            }
            UsersImpl usersImpl = new UsersImpl();
            int rowsAffected = usersImpl.update(user);

            if (rowsAffected <= 0) {
                System.out.println("Warning: No rows updated in database for user: " + user.getPhoneNumber());
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RemoteException("Failed to save profile picture: " + e.getMessage());
        }
    }

    @Override
    public void updateStatus(StatusDTO statusDTO) throws RemoteException {
        UsersImpl usersDao = new UsersImpl();
        Users user = usersDao.get(statusDTO.getUserId());
        user.setStatus(statusDTO.getStatus());
        usersDao.update(user);
    }
}
