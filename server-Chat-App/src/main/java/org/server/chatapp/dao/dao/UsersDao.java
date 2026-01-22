package org.server.chatapp.dao.dao;

import model.Users;

public interface UsersDao extends Dao<Users> {

    Users getUserByPhoneNumber(String phoneNumber);

    boolean isPhoneNumberExists(String phoneNumber);

    boolean isEmailExists(String email);
}
