package rmi;

import dto.BidirectionalFriendStatusDTO;
import dto.ChatRoomDTO;
import model.Message;
import model.Room;
import model.Users;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GetUserService extends Remote {

    Users getUser(Long userId) throws RemoteException;

    List<Users> searchUsersByPhoneNumber(String phoneNumber, long searchingUserId) throws RemoteException;

    BidirectionalFriendStatusDTO getUserFriendStatus(Users me, Users other) throws RemoteException;

    List<ChatRoomDTO> getUserRooms(Users me) throws RemoteException;

    Users getSingleUserInRoom(ChatRoomDTO chatRoomDTO) throws RemoteException;

    List<Users> getUsersInRoom(ChatRoomDTO chatRoomDTO) throws RemoteException;

    int getUnreadMessagesCount(Users user, Room room) throws RemoteException;

    List<Message> getUnreadMessages(Users user, Room room) throws RemoteException;

    void updateUser(Users user) throws RemoteException;
}
