package org.server.chatapp.dao.implement;

import model.Users;
import model.enums.Gender;
import model.enums.Role;
import model.enums.Status;
import org.server.chatapp.dao.Database;
import org.server.chatapp.dao.dao.UsersDao;
import org.server.chatapp.util.ImageUtil;

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
                Timestamp lastSeenTimestamp = resultSet.getTimestamp("lastSeen");
                Users user = new Users(
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
                        lastSeenTimestamp != null ? lastSeenTimestamp.toLocalDateTime() : null
                );
                user.setRole(Role.valueOf(resultSet.getString("role")));
                user.setFirstLogin(resultSet.getBoolean("isFirstLogin"));

                ImageUtil.setImageBytes(user);

                return user;
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
                Timestamp lastSeenTimestamp = resultSet.getTimestamp("lastSeen");
                Users user = new Users(
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
                        lastSeenTimestamp != null ? lastSeenTimestamp.toLocalDateTime() : null
                );
                user.setRole(Role.valueOf(resultSet.getString("role")));
                user.setFirstLogin(resultSet.getBoolean("isFirstLogin"));

                ImageUtil.setImageBytes(user);

                return user;
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
                Timestamp lastSeenTimestamp = resultSet.getTimestamp("lastSeen");
                Users user = new Users(
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
                        lastSeenTimestamp != null ? lastSeenTimestamp.toLocalDateTime() : null
                );
                user.setRole(Role.valueOf(resultSet.getString("role")));
                user.setFirstLogin(resultSet.getBoolean("isFirstLogin"));
                ImageUtil.setImageBytes(user);

                allUsers.add(user);

            }

            preparedStatement.close();
            resultSet.close();
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
            if (users.getLastSeen() != null) {
                preparedStatement.setTimestamp(11, Timestamp.valueOf(users.getLastSeen()));
            } else {
                preparedStatement.setNull(11, Types.TIMESTAMP);
            }
            preparedStatement.setLong(12, users.getId());

            result = preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return result;
    }

    @Override
    public long insert(Users users) {
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

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
            if (users.getLastSeen() != null) {
                preparedStatement.setTimestamp(11, Timestamp.valueOf(users.getLastSeen()));
            } else {
                preparedStatement.setNull(11, Types.TIMESTAMP);
            }
            result = preparedStatement.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    users.setId(generatedKeys.getLong(1));
                }
            }
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

    @Override
    public boolean isPhoneNumberExists(String phoneNumber) {
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = "SELECT count(*) FROM users WHERE phoneNumber = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, phoneNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isEmailExists(String email) {
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return false;
    }

    public List<Users> searchUsersByPhoneNumber(String phoneNumber, Long searchingUserId) {
        phoneNumber += "%";
        List<Users> matchedUsers = new ArrayList<>();
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = """
                    SELECT
                      *
                    FROM
                      USERS AS u
                    WHERE
                      u.phoneNumber LIKE ?
                      AND
                      u.id NOT IN (
                            SELECT
                                CASE
                                    WHEN f.senderUserId = ? THEN f.receiverUserId
                                    ELSE f.senderUserId
                                END
                            FROM
                                FRIENDS AS f
                            WHERE
                                (senderUserId = u.id AND f.status = 'ACCEPTED')
                                    OR
                                (receiverUserId = u.id AND f.status = 'ACCEPTED')
                          )
                      AND
                        u.id <> ?
                    LIMIT 20;""";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, phoneNumber);
            preparedStatement.setLong(2, searchingUserId);
            preparedStatement.setLong(3, searchingUserId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Timestamp lastSeenTimestamp = resultSet.getTimestamp("lastSeen");
                Users user = new Users(
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
                        lastSeenTimestamp != null ? lastSeenTimestamp.toLocalDateTime() : null
                );
                user.setRole(Role.valueOf(resultSet.getString("role")));
                user.setFirstLogin(resultSet.getBoolean("isFirstLogin"));

                ImageUtil.setImageBytes(user);

                matchedUsers.add(user);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return matchedUsers;
    }

    @Override
    public boolean updateStatus(String phoneNumber, Status status) {
        Users user = getUserByPhoneNumber(phoneNumber);
        if (user != null) {
            user.setStatus(status);
            update(user);
            return true;
        }
        return false;

    }

    @Override
    public boolean updateUserRole(Long userId, Role role) {
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = "UPDATE users SET role = ? WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, role.name());
            preparedStatement.setLong(2, userId);

            int result = preparedStatement.executeUpdate();
            preparedStatement.close();

            return result > 0;
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateFirstLoginFlag(Long userId, boolean isFirstLogin) {
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = "UPDATE users SET isFirstLogin = ? WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, isFirstLogin);
            preparedStatement.setLong(2, userId);

            int result = preparedStatement.executeUpdate();
            preparedStatement.close();

            return result > 0;
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isAdmin(Long userId) {
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = "SELECT role FROM users WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String roleStr = resultSet.getString("role");
                Role role = Role.valueOf(roleStr);
                return role == Role.ADMIN || role == Role.MASTER_ADMIN;
            }

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Users> getAllAdmins() {
        List<Users> adminUsers = new ArrayList<>();
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = "SELECT * FROM users WHERE role IN ('ADMIN', 'MASTER_ADMIN')";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Timestamp lastSeenTimestamp = resultSet.getTimestamp("lastSeen");
                Users user = new Users(
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
                        lastSeenTimestamp != null ? lastSeenTimestamp.toLocalDateTime() : null
                );

                user.setRole(Role.valueOf(resultSet.getString("role")));
                user.setFirstLogin(resultSet.getBoolean("isFirstLogin"));

                ImageUtil.setImageBytes(user);
                adminUsers.add(user);
            }

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return adminUsers;
    }

    @Override
    public Role getUserRole(Long userId) {
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = "SELECT role FROM users WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String roleStr = resultSet.getString("role");
                return Role.valueOf(roleStr);
            }

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return Role.USER;
    }
}
