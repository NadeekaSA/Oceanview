package com.oceanview.dao;

import com.oceanview.model.Reservation;
import com.oceanview.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    public boolean createReservation(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservations (reservation_number, guest_id, room_number, check_in_date, check_out_date, total_cost, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, reservation.getReservationNumber());
            stmt.setInt(2, reservation.getGuestId());
            stmt.setString(3, reservation.getRoomNumber());
            stmt.setDate(4, new java.sql.Date(reservation.getCheckInDate().getTime()));
            stmt.setDate(5, new java.sql.Date(reservation.getCheckOutDate().getTime()));
            stmt.setDouble(6, reservation.getTotalCost());
            stmt.setString(7, reservation.getStatus());

            int result = stmt.executeUpdate();
            if (result > 0) {
                // Update room status to OCCUPIED
                new RoomDAO().updateRoomStatus(reservation.getRoomNumber(), "OCCUPIED");
                return true;
            }
        }
        return false;
    }

    public List<Reservation> getAllReservations() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";
        try (Connection conn = DBConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                reservations.add(new Reservation(
                        rs.getString("reservation_number"),
                        rs.getInt("guest_id"),
                        rs.getString("room_number"),
                        rs.getDate("check_in_date"),
                        rs.getDate("check_out_date"),
                        rs.getDouble("total_cost"),
                        rs.getString("status")));
            }
        }
        return reservations;
    }

    public double getTotalRevenue() throws SQLException {
        String sql = "SELECT SUM(total_cost) FROM reservations WHERE status = 'CHECKED_OUT' OR status = 'CHECKED_IN'";
        try (Connection conn = DBConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0;
    }
}
