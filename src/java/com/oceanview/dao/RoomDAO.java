package com.oceanview.dao;

import com.oceanview.model.Room;
import com.oceanview.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";
        try (Connection conn = DBConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(new Room(
                        rs.getString("room_number"),
                        rs.getString("type"),
                        rs.getDouble("rate"),
                        rs.getString("status")));
            }
        }
        return rooms;
    }

    public boolean updateRoomRate(String roomNumber, double newRate) throws SQLException {
        String sql = "UPDATE rooms SET rate = ? WHERE room_number = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newRate);
            stmt.setString(2, roomNumber);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateRoomStatus(String roomNumber, String status) throws SQLException {
        String sql = "UPDATE rooms SET status = ? WHERE room_number = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, roomNumber);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean addRoom(String roomNumber, String type, double rate) throws SQLException {
        String sql = "INSERT INTO rooms (room_number, type, rate, status) VALUES (?, ?, ?, 'AVAILABLE')";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roomNumber);
            stmt.setString(2, type);
            stmt.setDouble(3, rate);
            return stmt.executeUpdate() > 0;
        }
    }
}
