package org.server.chatapp.dao.implement;

import model.Notification;
import model.enums.NotificationStatus;
import model.enums.NotificationType;
import org.server.chatapp.dao.Database;
import org.server.chatapp.dao.dao.NotificationDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDaoImpl implements NotificationDao {

    @Override
    public Notification get(long id) {
        String sql = "SELECT * FROM notification WHERE id = ?";
        try (Connection connection = Database.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractNotificationFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Notification> getAll() {
        String sql = "SELECT * FROM notification";
        List<Notification> notifications = new ArrayList<>();
        try (Connection connection = Database.getDataSource().getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                notifications.add(extractNotificationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    @Override
    public int update(Notification notification) {
        String sql = "UPDATE notification SET receiverId = ?, type = ?, content = ?, friendId = ?, " +
                "createdAt = ?, status = ?, roomId = ? WHERE id = ?";
        try (Connection connection = Database.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, notification.getReceiverId());
            stmt.setString(2, notification.getType().name());
            stmt.setString(3, notification.getContent());

            if (notification.getFriendId() != null) {
                stmt.setLong(4, notification.getFriendId());
            } else {
                stmt.setNull(4, Types.BIGINT);
            }

            stmt.setTimestamp(5, notification.getCreatedAt());
            stmt.setString(6, notification.getStatus().name());

            if (notification.getRoomId() != null) {
                stmt.setLong(7, notification.getRoomId());
            } else {
                stmt.setNull(7, Types.BIGINT);
            }

            stmt.setLong(8, notification.getId());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int insert(Notification notification) {
        String sql = "INSERT INTO notification (receiverId, type, content, friendId, createdAt, status, roomId) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, notification.getReceiverId());
            stmt.setString(2, notification.getType().name());
            stmt.setString(3, notification.getContent());

            if (notification.getFriendId() != null) {
                stmt.setLong(4, notification.getFriendId());
            } else {
                stmt.setNull(4, Types.BIGINT);
            }

            stmt.setTimestamp(5, notification.getCreatedAt());
            stmt.setString(6, notification.getStatus().name());

            if (notification.getRoomId() != null) {
                stmt.setLong(7, notification.getRoomId());
            } else {
                stmt.setNull(7, Types.BIGINT);
            }

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        notification.setId(generatedKeys.getLong(1));
                    }
                }
            }
            return affectedRows;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(Notification notification) {
        String sql = "DELETE FROM notification WHERE id = ?";
        try (Connection connection = Database.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, notification.getId());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Notification> getByReceiverId(long receiverId) {
        String sql = "SELECT * FROM notification WHERE receiverId = ? ORDER BY createdAt DESC";
        List<Notification> notifications = new ArrayList<>();
        try (Connection connection = Database.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, receiverId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notifications.add(extractNotificationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    @Override
    public int markAsRead(long id) {
        String sql = "UPDATE notification SET status = 'READ' WHERE id = ?";
        try (Connection connection = Database.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int markAsDeleted(long id) {
        String sql = "UPDATE notification SET status = 'DELETED' WHERE id = ?";
        try (Connection connection = Database.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Notification extractNotificationFromResultSet(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setId(rs.getLong("id"));
        notification.setReceiverId(rs.getLong("receiverId"));
        notification.setType(NotificationType.valueOf(rs.getString("type").toUpperCase()));
        notification.setContent(rs.getString("content"));

        long friendId = rs.getLong("friendId");
        notification.setFriendId(rs.wasNull() ? null : friendId);

        notification.setCreatedAt(rs.getTimestamp("createdAt"));
        notification.setStatus(NotificationStatus.valueOf(rs.getString("status").toUpperCase()));

        long roomId = rs.getLong("roomId");
        notification.setRoomId(rs.wasNull() ? null : roomId);

        return notification;
    }
}