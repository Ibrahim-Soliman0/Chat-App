package org.server.chatapp.dao.dao;

import model.Users;

import java.util.List;

public interface UsersDao extends Dao<Users> {

    Users getUserByPhoneNumber(String phoneNumber);

    boolean isPhoneNumberExists(String phoneNumber);

    boolean isEmailExists(String email);

    List<Users> searchUsersByPhoneNumber(String phoneNumber, Long searchingUserId);
}
