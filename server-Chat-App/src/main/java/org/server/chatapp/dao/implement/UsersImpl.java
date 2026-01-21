package org.server.chatapp.dao.implement;

import model.Users;
import model.enums.Gender;
import model.enums.Status;
import org.server.chatapp.dao.Database;
import org.server.chatapp.dao.dao.UsersDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersImpl implements UsersDao {

    @Override
    public Users getUserByPhoneNumber(String phoneNumber) {
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = """
                    SELECT * FROM USERS WHERE phoneNumber = ?""";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, phoneNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Users(
                        resultSet.getLong("id"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("picturePath"),
                        resultSet.getString("password"),
                        Gender.valueOf(resultSet.getString("gender").toUpperCase()),
                        resultSet.getString("country"),
                        resultSet.getDate("DOB").toLocalDate(),
                        resultSet.getString("bio"),
                        Status.valueOf(resultSet.getString("status").toUpperCase()),
                        resultSet.getTimestamp("lastSeen").toLocalDateTime()
                );
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    @Override
    public Users get(long id) {
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = """
                    SELECT * FROM USERS WHERE id = ?""";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Users(
                        resultSet.getLong("id"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("picturePath"),
                        resultSet.getString("password"),
                        Gender.valueOf(resultSet.getString("gender").toUpperCase()),
                        resultSet.getString("country"),
                        resultSet.getDate("DOB").toLocalDate(),
                        resultSet.getString("bio"),
                        Status.valueOf(resultSet.getString("status").toUpperCase()),
                        resultSet.getTimestamp("lastSeen").toLocalDateTime()
                );
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Users> getAll() {

        List<Users> allUsers = new ArrayList<>();

        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = """
                    SELECT * FROM USERS""";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                allUsers.add(new Users(
                        resultSet.getLong("id"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("picturePath"),
                        resultSet.getString("password"),
                        Gender.valueOf(resultSet.getString("gender").toUpperCase()),
                        resultSet.getString("country"),
                        resultSet.getDate("DOB").toLocalDate(),
                        resultSet.getString("bio"),
                        Status.valueOf(resultSet.getString("status").toUpperCase()),
                        resultSet.getTimestamp("lastSeen").toLocalDateTime()
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
    public int update(Users users) {

        int result = 0;
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = """
                    UPDATE users SET
                            phoneNumber = ?,
                            name = ?,
                            email = ?,
                            picturePath = ?,
                            password = ?,
                            gender = ?,
                            country = ?,
                            DOB = ?,
                            bio = ?,
                            status = ?,
                            lastSeen = ?
                        WHERE id = ?;""";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, users.getPhoneNumber());
            preparedStatement.setString(2, users.getName());
            preparedStatement.setString(3, users.getEmail());
            preparedStatement.setString(4, users.getPicturePath());
            preparedStatement.setString(5, users.getPassword());
            preparedStatement.setString(6, users.getGender().name());
            preparedStatement.setString(7, users.getCountry());
            preparedStatement.setDate(8, Date.valueOf(users.getDob()));
            preparedStatement.setString(9, users.getBio());
            preparedStatement.setString(10, users.getStatus().name());
            preparedStatement.setTimestamp(11, Timestamp.valueOf(users.getLastSeen()));
            preparedStatement.setLong(12, users.getId());

            result = preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return result;
    }

    @Override
    public int insert(Users users) {
        int result = 0;
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = """
                    INSERT INTO users (
                              phoneNumber,
                              name,
                              email,
                              picturePath,
                              password,
                              gender,
                              country,
                              DOB,
                              bio,
                              status,
                              lastSeen
                          ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);""";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, users.getPhoneNumber());
            preparedStatement.setString(2, users.getName());
            preparedStatement.setString(3, users.getEmail());
            preparedStatement.setString(4, users.getPicturePath());
            preparedStatement.setString(5, users.getPassword());
            preparedStatement.setString(6, users.getGender().name());
            preparedStatement.setString(7, users.getCountry());
            preparedStatement.setDate(8, Date.valueOf(users.getDob()));
            preparedStatement.setString(9, users.getBio());
            preparedStatement.setString(10, users.getStatus().name());
            preparedStatement.setTimestamp(11, Timestamp.valueOf(users.getLastSeen()));

            result = preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return result;
    }

    @Override
    public int delete(Users users) {

        int result = 0;
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = """
                    DELETE FROM USERS WHERE id = ?""";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, users.getId());
            result = preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return result;
    }
}
