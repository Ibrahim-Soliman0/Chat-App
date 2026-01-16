package org.server.chatapp.dao.implement;

import org.server.chatapp.dao.Database;
import org.server.chatapp.dao.dao.UserRoomsDao;
import org.server.chatapp.model.UserRooms;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRoomsImpl implements UserRoomsDao {
    @Override
    public UserRooms get(long id) {
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = """
                    SELECT * FROM USERROOMS WHERE id = ?""";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new UserRooms(
                        resultSet.getLong("id"),
                        resultSet.getLong("userId"),
                        resultSet.getLong("roomId"),
                        resultSet.getBoolean("isAdmin"),
                        resultSet.getTimestamp("joinedAt").toLocalDateTime(),
                        resultSet.getTimestamp("leftAt").toLocalDateTime(),
                        resultSet.getBoolean("isActive")
                );
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    @Override
    public List<UserRooms> getAll() {

        List<UserRooms> allUsers = new ArrayList<>();

        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = """
                    SELECT * FROM USERROOMS""";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                allUsers.add(new UserRooms(
                        resultSet.getLong("id"),
                        resultSet.getLong("userId"),
                        resultSet.getLong("roomId"),
                        resultSet.getBoolean("isAdmin"),
                        resultSet.getTimestamp("joinedAt").toLocalDateTime(),
                        resultSet.getTimestamp("leftAt").toLocalDateTime(),
                        resultSet.getBoolean("isActive")
                ));

                preparedStatement.close();
                resultSet.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return allUsers;
    }

    @Override
    public int update(UserRooms userRooms) {

        int result = 0;

        try (Connection connection = Database.getDataSource().getConnection()) {

            String sql = """
            UPDATE userrooms SET
                userId   = ?,
                roomId   = ?,
                isAdmin  = ?,
                joinedAt = ?,
                leftAt   = ?,
                isActive = ?
            WHERE id = ?;""";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, userRooms.getUserId());
            preparedStatement.setLong(2, userRooms.getRoomId());
            preparedStatement.setBoolean(3, userRooms.getIsAdmin());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(userRooms.getJoinedAt()));

            if (userRooms.getLeftAt() != null) {
                preparedStatement.setTimestamp(5, Timestamp.valueOf(userRooms.getLeftAt()));
            } else {
                preparedStatement.setNull(5, Types.TIMESTAMP);
            }

            preparedStatement.setBoolean(6, userRooms.getIsActive());
            preparedStatement.setLong(7, userRooms.getId());

            result = preparedStatement.executeUpdate();

            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


    @Override
    public int insert(UserRooms userRooms) {

        int result = 0;

        try (Connection connection = Database.getDataSource().getConnection()) {

            String sql = """
            INSERT INTO userrooms (
                userId,
                roomId,
                isAdmin,
                joinedAt,
                leftAt,
                isActive
            ) VALUES (?, ?, ?, ?, ?, ?);
        """;

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, userRooms.getUserId());
            preparedStatement.setLong(2, userRooms.getRoomId());
            preparedStatement.setBoolean(3, userRooms.getIsAdmin());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(userRooms.getJoinedAt()));

            if (userRooms.getLeftAt() != null) {
                preparedStatement.setTimestamp(5, Timestamp.valueOf(userRooms.getLeftAt()));
            } else {
                preparedStatement.setNull(5, Types.TIMESTAMP);
            }

            preparedStatement.setBoolean(6, userRooms.getIsActive());

            result = preparedStatement.executeUpdate();

            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


    @Override
    public int delete(UserRooms UserRooms) {

        int result = 0;
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = """
                    DELETE FROM UserRooms WHERE id = ?""";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, UserRooms.getId());
            result = preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return result;
    }
}
