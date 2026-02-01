package org.server.chatapp.dao.implement;

import model.ServerAnnouncement;
import org.server.chatapp.dao.Database;
import org.server.chatapp.dao.dao.ServerAnnouncementDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ServerAnnouncementImpl implements ServerAnnouncementDao {

    @Override
    public ServerAnnouncement get(long id) {
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = "SELECT * FROM serverannouncement WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ServerAnnouncement announcement = new ServerAnnouncement();
                announcement.setId(rs.getLong("id"));
                announcement.setTitle(rs.getString("title"));
                announcement.setContent(rs.getString("content"));
                announcement.setCreatedBy(rs.getString("createdBy"));
                announcement.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                announcement.setExpireAt(rs.getTimestamp("expiresAt").toLocalDateTime());

                announcement.setActive(rs.getInt("isActive") == 1);

                rs.close();
                ps.close();
                return announcement;
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    @Override
    public List<ServerAnnouncement> getAll() {
        List<ServerAnnouncement> allAnnouncements = new ArrayList<>();

        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = "SELECT * FROM serverannouncement";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ServerAnnouncement announcement = new ServerAnnouncement();
                announcement.setId(rs.getLong("id"));
                announcement.setTitle(rs.getString("title"));
                announcement.setContent(rs.getString("content"));
                announcement.setCreatedBy(rs.getString("createdBy"));
                announcement.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                announcement.setExpireAt(rs.getTimestamp("expiresAt").toLocalDateTime());
                announcement.setActive(rs.getInt("isActive") == 1);

                allAnnouncements.add(announcement);
            }

            rs.close();
            ps.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return allAnnouncements;

    }
    @Override
    public long insert(ServerAnnouncement announcement) {
        int result = 0;
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = "INSERT INTO serverannouncement (title, content, createdBy, createdAt, expiresAt, isActive) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, announcement.getTitle());
            ps.setString(2, announcement.getContent());
            ps.setString(3, announcement.getCreatedBy());
            ps.setTimestamp(4, Timestamp.valueOf(announcement.getCreatedAt()));
            ps.setTimestamp(5, Timestamp.valueOf(announcement.getExpireAt()));
            ps.setInt(6, announcement.getActive() ? 1 : 0);
            result = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public int update(ServerAnnouncement announcement) {
        int result = 0;
        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = "UPDATE serverannouncement SET title= ?, content = ?, createdBy = ?,isActive=?, createdAt = ?, expiresAt = ? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, announcement.getTitle());
            ps.setString(2, announcement.getContent());
            ps.setString(3, announcement.getCreatedBy());
            ps.setInt(4, announcement.getActive() ? 1 : 0);
            ps.setTimestamp(5, Timestamp.valueOf(announcement.getCreatedAt()));
            ps.setTimestamp(6, Timestamp.valueOf(announcement.getExpireAt()));
            ps.setLong(7, announcement.getId());


            result = ps.executeUpdate();
            ps.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }
    @Override
    public int delete(ServerAnnouncement announcement) {
        int result = 0;

        try (Connection connection = Database.getDataSource().getConnection()) {
            String sql = "DELETE FROM serverannouncement WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, announcement.getId());

            result = ps.executeUpdate();
            ps.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return result;
    }



}
