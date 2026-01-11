package org.server.chatapp.dao.dao;

import org.server.chatapp.model.Users;

public interface UsersDao extends Dao<Users> {

    Users getUserByPhoneNumber(String phoneNumber);
}
