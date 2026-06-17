package com.oceanview.servlet;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/report")
public class ReportServlet extends HttpServlet {
    private ReservationDAO reservationDAO = new ReservationDAO();
    private RoomDAO roomDAO = new RoomDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            if ("stats".equals(action)) {
                List<Room> rooms = roomDAO.getAllRooms();
                List<Reservation> reservations = reservationDAO.getAllReservations();

                int available = 0;
                int occupied = 0;
                if (rooms != null) {
                    for (Room r : rooms) {
                        if (r != null && "AVAILABLE".equals(r.getStatus()))
                            available++;
                        else if (r != null && "OCCUPIED".equals(r.getStatus()))
                            occupied++;
                    }
                }

                long activeBookings = 0;
                if (reservations != null) {
                    activeBookings = reservations.stream()
                            .filter(r -> r != null &&
                                    ("CHECKED_IN".equals(r.getStatus()) ||
                                            "BOOKED".equals(r.getStatus()) ||
                                            "CONFIRMED".equals(r.getStatus())))
                            .count();
                }

                String json = "{" +
                        "\"availableRooms\":" + available + "," +
                        "\"occupiedRooms\":" + occupied + "," +
                        "\"activeBookings\":" + activeBookings +
                        "}";
                response.getWriter().write(json);

            } else if ("recent".equals(action)) {
                List<Reservation> reservations = reservationDAO.getAllReservations();
                StringBuilder json = new StringBuilder("[");

                if (reservations != null && !reservations.isEmpty()) {
                    // Get last 5
                    int limit = Math.min(5, reservations.size());
                    List<Reservation> recent = reservations.subList(0, limit);

                    for (int i = 0; i < recent.size(); i++) {
                        Reservation r = recent.get(i);
                        if (r == null)
                            continue;

                        json.append("{")
                                .append("\"reservationNumber\":\"")
                                .append(r.getReservationNumber() != null ? r.getReservationNumber() : "N/A")
                                .append("\",")
                                .append("\"guestId\":").append(r.getGuestId()).append(",")
                                .append("\"roomNumber\":\"")
                                .append(r.getRoomNumber() != null ? r.getRoomNumber() : "N/A").append("\",")
                                .append("\"checkInDate\":\"")
                                .append(r.getCheckInDate() != null ? r.getCheckInDate() : "").append("\",")
                                .append("\"checkOutDate\":\"")
                                .append(r.getCheckOutDate() != null ? r.getCheckOutDate() : "").append("\",")
                                .append("\"totalCost\":").append(r.getTotalCost()).append(",")
                                .append("\"status\":\"").append(r.getStatus() != null ? r.getStatus() : "").append("\"")
                                .append("}");
                        if (i < recent.size() - 1)
                            json.append(",");
                    }
                }
                json.append("]");
                response.getWriter().write(json.toString());
            } else {
                // Default: existing admin stats
                double revenue = reservationDAO.getTotalRevenue();
                List<Room> rooms = roomDAO.getAllRooms();
                int available = 0;
                int occupied = 0;
                int total = 0;
                if (rooms != null) {
                    total = rooms.size();
                    for (Room r : rooms) {
                        if (r != null && "AVAILABLE".equals(r.getStatus()))
                            available++;
                        else if (r != null && "OCCUPIED".equals(r.getStatus()))
                            occupied++;
                    }
                }

                String json = "{" +
                        "\"totalRevenue\":" + revenue + "," +
                        "\"occupancy\": {" +
                        "\"available\":" + available + "," +
                        "\"occupied\":" + occupied + "," +
                        "\"total\":" + total +
                        "}" +
                        "}";
                response.getWriter().write(json);
            }
        } catch (Exception e) {
            // General catch to prevent 500 error page
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String message = e.getMessage() != null ? e.getMessage().replace("\"", "\\\"") : "Unknown Error";
            response.getWriter().write("{\"success\": false, \"message\": \"Server error: " + message + "\"}");
        }
    }
}
