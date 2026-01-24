package org.server.chatapp.dao.implement;

import model.Friend;
import model.enums.FriendStatus;
import org.server.chatapp.dao.Database;
import org.server.chatapp.dao.dao.FriendsDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendsImpl implements FriendsDao {

    @Override
    public Friend get(long id) {

        try (Connection connection = Database.getDataSource().getConnection()) {

            String sql = "SELECT * FROM friends WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Friend(resultSet.getLong("id"),
                        resultSet.getLong("senderUserId"),
                        resultSet.getLong("receiverUserId"),
                        resultSet.getTimestamp("requestDate"),
                        resultSet.getTimestamp("responseDate"),
                        FriendStatus.valueOf(resultSet.getString("status").toUpperCase()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int update(Friend friend) {
        int result = 0;
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = """
                    UPDATE friends
                    SET senderUserId = ?,
                        receiverUserId = ?,
                        requestDate = ?,
                        responseDate = ?,
                        status = ?
                    WHERE id = ?""";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, friend.getSenderUserId());
            preparedStatement.setLong(2, friend.getReceiverUserId());
            preparedStatement.setTimestamp(3, friend.getRequestDate());
            preparedStatement.setTimestamp(4, friend.getResponseDate());
            preparedStatement.setString(5, friend.getStatus().name());
            preparedStatement.setLong(6, friend.getId());

            result = preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public int insert(Friend friend) {

        int result = 0;
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = """
                    INSERT INTO friends 
                    (senderUserId, receiverUserId, requestDate, responseDate, status)
                    VALUES (?, ?, ?, ?, ?)""";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, friend.getSenderUserId());
            preparedStatement.setLong(2, friend.getReceiverUserId());
            preparedStatement.setTimestamp(3, friend.getRequestDate());
            preparedStatement.setTimestamp(4, friend.getResponseDate());
            preparedStatement.setString(5, friend.getStatus().name());

            result = preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public int delete(Friend friend) {
        int result = 0;
        try (Connection connection = Database.getDataSource().getConnection()) {

            String sql = """
                        DELETE FROM friends WHERE id = ?""";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, friend.getId());

            result = preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Friend> getAll() {

        List<Friend> friends = new ArrayList<>();

        try (Connection connection = Database.getDataSource().getConnection()) {

            String sql = """
                            SELECT * FROM friends""";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                friends.add(new Friend(resultSet.getLong("id"),
                        resultSet.getLong("senderUserId"),
                        resultSet.getLong("receiverUserId"),
                        resultSet.getTimestamp("requestDate"),
                        resultSet.getTimestamp("responseDate"),
                        FriendStatus.valueOf(resultSet.getString("status").toUpperCase())));
            }

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return friends;
    }

    @Override
    public List<Friend> getUserFriendsList(long userId) {

        List<Friend> myFriends = new ArrayList<>();

        try (Connection connection = Database.getDataSource().getConnection()) {

            String sql = """
                         SELECT
                            *
                         FROM
                            friends
                         WHERE
                            senderUserId = ? AND STATUS = 'ACCEPTED'""";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                myFriends.add(new Friend(resultSet.getLong("id"),
                        resultSet.getLong("senderUserId"),
                        resultSet.getLong("receiverUserId"),
                        resultSet.getTimestamp("requestDate"),
                        resultSet.getTimestamp("responseDate"),
                        FriendStatus.valueOf(resultSet.getString("status").toUpperCase())));
            }

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return myFriends;
    }

    @Override
    public Friend getUserFriendStatus(long myId, long otherId) {

        try (Connection connection = Database.getDataSource().getConnection()) {

            String sql = """
                      SELECT
                        *
                      FROM
                        FRIENDS as f
                      WHERE
                        senderUserId = ? AND receiverUserId = ?
                      ORDER BY requestDate DESC
                      LIMIT 1;""";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, myId);
            preparedStatement.setLong(2, otherId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Friend(resultSet.getLong("id"),
                        resultSet.getLong("senderUserId"),
                        resultSet.getLong("receiverUserId"),
                        resultSet.getTimestamp("requestDate"),
                        resultSet.getTimestamp("responseDate"),
                        FriendStatus.valueOf(resultSet.getString("status").toUpperCase()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
