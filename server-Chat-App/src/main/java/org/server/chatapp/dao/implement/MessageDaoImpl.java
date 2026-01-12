package org.server.chatapp.dao.implement;

import org.server.chatapp.dao.Database;
import org.server.chatapp.dao.dao.MessageDao;
import org.server.chatapp.model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDaoImpl implements MessageDao {

    private Message createMessageObject(ResultSet rs) throws SQLException {
        Message message = new Message();

        message.setId(rs.getLong("id"));
        message.setSenderId(rs.getLong("senderId"));
        message.setRoomId(rs.getLong("roomId"));

        message.setText(rs.getString("text"));

        message.setFontFamily(rs.getString("fontFamily"));
        message.setFontSize(rs.getInt("fontSize"));
        message.setFontColor(rs.getString("fontColor"));
        message.setBackgroundColor(rs.getString("backgroundColor"));

        message.setBold(rs.getBoolean("isBold"));
        message.setItalic(rs.getBoolean("isItalic"));
        message.setUnderline(rs.getBoolean("isUnderline"));

        message.setAttachedFile(rs.getString("attachedFile"));
        message.setFileName(rs.getString("fileName"));
        message.setFileType(rs.getString("fileType"));
        message.setFileSize(rs.getLong("fileSize"));

        Timestamp sentAt = rs.getTimestamp("sentAt");
        if (sentAt != null) {
            message.setSentAt(sentAt.toLocalDateTime());
        }

        message.setDeleted(rs.getBoolean("isDeleted"));

        return message;
    }

    @Override
    public Message get(long id) {
        Message message = null;
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = "SELECT * FROM Message WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    message = createMessageObject(resultSet);
                }
            }
            return message;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Message> getAll() {
        List<Message> messages = new ArrayList<>();
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = "SELECT * FROM Message ORDER BY sentAt";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Message message = createMessageObject(resultSet);
                    messages.add(message);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return messages;
    }

    @Override
    public int update(Message message) {
        String sql = """
        UPDATE Message SET
            text = ?,
            fontFamily = ?,
            fontSize = ?,
            fontColor = ?,
            backgroundColor = ?,
            isBold = ?,
            isItalic = ?,
            isUnderline = ?,
            attachedFile = ?,
            fileName = ?,
            fileType = ?,
            fileSize = ?
        WHERE id = ?
        """;

        try (Connection connection = Database.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, message.getText());
            ps.setString(2, message.getFontFamily());
            ps.setInt(3, message.getFontSize());
            ps.setString(4, message.getFontColor());
            ps.setString(5, message.getBackgroundColor());
            ps.setBoolean(6, message.isBold());
            ps.setBoolean(7, message.isItalic());
            ps.setBoolean(8, message.isUnderline());
            ps.setString(9, message.getAttachedFile());
            ps.setString(10, message.getFileName());
            ps.setString(11, message.getFileType());

            if (message.getFileSize() > 0) {
                ps.setLong(12, message.getFileSize());
            } else {
                ps.setNull(12, Types.BIGINT);
            }

            ps.setLong(13, message.getId());
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public int insert(Message message) {
        String sql = """
        INSERT INTO Message
        (senderId, roomId, text, fontFamily, fontSize, fontColor, backgroundColor,
         isBold, isItalic, isUnderline,
         attachedFile, fileName, fileType, fileSize, sentAt, isDeleted)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection connection = Database.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, message.getSenderId());
            ps.setLong(2, message.getRoomId());
            ps.setString(3, message.getText());
            ps.setString(4, message.getFontFamily());
            ps.setInt(5, message.getFontSize());
            ps.setString(6, message.getFontColor());
            ps.setString(7, message.getBackgroundColor());

            ps.setBoolean(8, message.isBold());
            ps.setBoolean(9, message.isItalic());
            ps.setBoolean(10, message.isUnderline());

            ps.setString(11, message.getAttachedFile());
            ps.setString(12, message.getFileName());
            ps.setString(13, message.getFileType());

            if (message.getFileSize() > 0) {
                ps.setLong(14, message.getFileSize());
            } else {
                ps.setNull(14, Types.BIGINT);
            }

            if (message.getSentAt() != null) {
                ps.setTimestamp(15, Timestamp.valueOf(message.getSentAt()));
            } else {
                ps.setTimestamp(15, new Timestamp(System.currentTimeMillis()));
            }

            ps.setBoolean(16, message.isDeleted());

            int rowsInserted = ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    message.setId(rs.getLong(1));
                }
            }

            return rowsInserted;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delete(Message message) {
        String sql = "UPDATE Message SET isDeleted = true WHERE id = ?";

        try (Connection connection = Database.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, message.getId());
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }    }

    @Override
    public List<Message> getMessagesByRoomId(long roomId) {
        List<Message> messages = new ArrayList<>();
        String sql = """
        SELECT * FROM Message
        WHERE roomId = ? AND isDeleted = false
        ORDER BY sentAt
        """;
        try (Connection connection = Database.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, roomId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Message message = createMessageObject(rs);
                    messages.add(message);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return messages;
    }
}