package org.server.chatapp.dao.dao;

import model.Friend;

import java.util.List;

public interface FriendsDao extends Dao<Friend> {

    List<Friend> getUserFriendsList(long userId);

    Friend getUserFriendStatus(long myId, long otherId);
}
