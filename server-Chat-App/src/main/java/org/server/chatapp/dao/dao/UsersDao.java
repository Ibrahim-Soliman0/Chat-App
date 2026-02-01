package org.server.chatapp.dao.dao;

import model.Users;
import model.enums.Role;
import model.enums.Status;

import java.util.List;

public interface UsersDao extends Dao<Users> {

    Users getUserByPhoneNumber(String phoneNumber);

    boolean isPhoneNumberExists(String phoneNumber);

    boolean isEmailExists(String email);

    List<Users> searchUsersByPhoneNumber(String phoneNumber, Long searchingUserId);
    boolean updateStatus(String phoneNumber , Status status);

    boolean updateUserRole(Long userId, Role role);
    boolean updateFirstLoginFlag(Long userId, boolean isFirstLogin);
    boolean isAdmin(Long userId);
    List<Users> getAllAdmins();
    Role getUserRole(Long userId);

}
