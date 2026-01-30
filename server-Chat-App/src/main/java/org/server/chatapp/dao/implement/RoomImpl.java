package org.server.chatapp.dao.implement;

import model.Room;
import model.enums.RoomType;
import org.server.chatapp.dao.Database;
import org.server.chatapp.dao.dao.RoomDao;
import org.server.chatapp.util.ImageUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.sql.Timestamp;

public class RoomImpl implements RoomDao {

    @Override
    public Room get(long id) {
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = "SELECT * FROM room WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Room room = new Room();
                room.setId(rs.getLong("id"));
                room.setType(RoomType.valueOf(rs.getString("type")));
                room.setName(rs.getString("name"));
                room.setDescription(rs.getString("description"));
                room.setPicturePath(rs.getString("picturePath"));

                ImageUtil.setImageBytes(room);

                Timestamp createdAt = rs.getTimestamp("createdAt");
                if (createdAt != null) room.setCreatedAt(createdAt.toLocalDateTime());

                Timestamp lastMessageAt = rs.getTimestamp("lastMessageAt");
                if (lastMessageAt != null) room.setLastMessageAt(lastMessageAt.toLocalDateTime());

                room.setCreatedBy(rs.getLong("createdBy"));


                return room;
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Room> getAll() {
        List<Room> allRooms = new ArrayList<>();
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = "SELECT * FROM room";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getLong("id"));
                room.setType(RoomType.valueOf(rs.getString("type")));
                room.setName(rs.getString("name"));
                room.setDescription(rs.getString("description"));
                room.setPicturePath(rs.getString("picturePath"));

                ImageUtil.setImageBytes(room);

                Timestamp createdAt = rs.getTimestamp("createdAt");
                if (createdAt != null) room.setCreatedAt(createdAt.toLocalDateTime());

                Timestamp lastMessageAt = rs.getTimestamp("lastMessageAt");
                if (lastMessageAt != null) room.setLastMessageAt(lastMessageAt.toLocalDateTime());

                room.setCreatedBy(rs.getLong("createdBy"));

                allRooms.add(room);
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }

        return allRooms;
    }

    @Override
    public long insert(Room room) {
        int result = 0;
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = "INSERT INTO room (type, name, description, picturePath, createdAt, lastMessageAt, createdBy) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, room.getType().name());
            ps.setString(2, room.getName());
            ps.setString(3, room.getDescription());
            ps.setString(4, room.getPicturePath());
            LocalDateTime createdAt = room.getCreatedAt();
            if (createdAt == null) createdAt = LocalDateTime.now();
            ps.setTimestamp(5, Timestamp.valueOf(createdAt));

            LocalDateTime lastMessageAt = room.getLastMessageAt();
            if (lastMessageAt == null) lastMessageAt = LocalDateTime.now();
            ps.setTimestamp(6, Timestamp.valueOf(lastMessageAt));
            ps.setLong(7, room.getCreatedBy());

            result = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public int update(Room room) {
        int result = 0;
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = "UPDATE room SET type = ?, name = ?, description = ?, picturePath = ?, lastMessageAt = ? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, room.getType().name());
            ps.setString(2, room.getName());
            ps.setString(3, room.getDescription());
            ps.setString(4, room.getPicturePath());
            LocalDateTime lastMessageAt = room.getLastMessageAt();
            if (lastMessageAt == null) lastMessageAt = LocalDateTime.now();
            ps.setTimestamp(5, Timestamp.valueOf(lastMessageAt));
            ps.setLong(6, room.getId());

            result = ps.executeUpdate();
            ps.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return result;
    }

    @Override
    public int delete(Room room) {
        int result = 0;
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = "DELETE FROM room WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, room.getId());

            result = ps.executeUpdate();
            ps.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return result;
    }

}






