package com.oceanview.dao;

import com.oceanview.model.Guest;
import com.oceanview.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {
    public boolean addGuest(Guest guest) throws SQLException {
        String sql = "INSERT INTO guests (name, address, contact, email, id_card_number) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, guest.getName());
            stmt.setString(2, guest.getAddress());
            stmt.setString(3, guest.getContact());
            stmt.setString(4, guest.getEmail());
            stmt.setString(5, guest.getIdCardNumber());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        guest.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public List<Guest> getAllGuests() throws SQLException {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM guests ORDER BY id DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                guests.add(new Guest(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("contact"),
                        rs.getString("email"),
                        rs.getString("id_card_number")));
            }
        }
        return guests;
    }

    public List<Guest> searchGuests(String query) throws SQLException {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM guests WHERE name LIKE ? OR id_card_number LIKE ? ORDER BY id DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchTerm = "%" + query + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    guests.add(new Guest(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("contact"),
                            rs.getString("email"),
                            rs.getString("id_card_number")));
                }
            }
        }
        return guests;
    }

    public Guest getGuestById(int id) throws SQLException {
        String sql = "SELECT * FROM guests WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Guest(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("contact"),
                            rs.getString("email"),
                            rs.getString("id_card_number"));
                }
            }
        }
        return null;
    }

    public boolean updateGuest(Guest guest) throws SQLException {
        String sql = "UPDATE guests SET name = ?, address = ?, contact = ?, email = ?, id_card_number = ? WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, guest.getName());
            stmt.setString(2, guest.getAddress());
            stmt.setString(3, guest.getContact());
            stmt.setString(4, guest.getEmail());
            stmt.setString(5, guest.getIdCardNumber());
            stmt.setInt(6, guest.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteGuest(int id) throws SQLException {
        String sql = "DELETE FROM guests WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
