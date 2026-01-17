package org.server.chatapp.dao.implement;

import org.server.chatapp.dao.Database;
import org.server.chatapp.dao.dao.MessageStatusDao;
import org.server.chatapp.model.MessageStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageStatusDaoImpl implements MessageStatusDao {

    @Override
    public MessageStatus get(long id) {
        String sql = "SELECT * FROM messagestatus WHERE id = ?";
        try (Connection connection = Database.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractMessageStatusFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<MessageStatus> getAll() {
        String sql = "SELECT * FROM messagestatus";
        List<MessageStatus> messageStatuses = new ArrayList<>();
        try (Connection connection = Database.getDataSource().getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                messageStatuses.add(extractMessageStatusFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messageStatuses;
    }

    @Override
    public int update(MessageStatus messageStatus) {
        String sql = "UPDATE messagestatus SET messageId = ?, userId = ?, seenAt = ? WHERE id = ?";
        try (Connection connection = Database.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, messageStatus.getMessageId());
            stmt.setLong(2, messageStatus.getUserId());
            stmt.setTimestamp(3, messageStatus.getSeenAt());
            stmt.setLong(4, messageStatus.getId());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int insert(MessageStatus messageStatus) {
        String sql = "INSERT INTO messagestatus (messageId, userId, seenAt) VALUES (?, ?, ?)";
        try (Connection connection = Database.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, messageStatus.getMessageId());
            stmt.setLong(2, messageStatus.getUserId());
            stmt.setTimestamp(3, messageStatus.getSeenAt());

            int affectedRows = stmt.executeUpdate();

            return affectedRows;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(MessageStatus messageStatus) {
        String sql = "DELETE FROM messagestatus WHERE id = ?";
        try (Connection connection = Database.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, messageStatus.getId());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<MessageStatus> getByMessageId(long messageId) {
        String sql = "SELECT * FROM messagestatus WHERE messageId = ?";
        List<MessageStatus> messageStatuses = new ArrayList<>();
        try (Connection connection = Database.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, messageId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                messageStatuses.add(extractMessageStatusFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messageStatuses;
    }

    private MessageStatus extractMessageStatusFromResultSet(ResultSet rs) throws SQLException {
        MessageStatus messageStatus = new MessageStatus();
        messageStatus.setId(rs.getLong("id"));
        messageStatus.setMessageId(rs.getLong("messageId"));
        messageStatus.setUserId(rs.getLong("userId"));
        messageStatus.setSeenAt(rs.getTimestamp("seenAt"));
        return messageStatus;
    }
}