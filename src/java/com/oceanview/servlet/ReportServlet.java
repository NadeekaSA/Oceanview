package com.oceanview.servlet;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Room;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/reports")
public class ReportServlet extends HttpServlet {
    private ReservationDAO reservationDAO = new ReservationDAO();
    private RoomDAO roomDAO = new RoomDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try {
            double revenue = reservationDAO.getTotalRevenue();
            List<Room> rooms = roomDAO.getAllRooms();

            int available = 0;
            int occupied = 0;
            for (Room r : rooms) {
                if ("AVAILABLE".equals(r.getStatus()))
                    available++;
                else if ("OCCUPIED".equals(r.getStatus()))
                    occupied++;
            }

            String json = "{" +
                    "\"totalRevenue\":" + revenue + "," +
                    "\"occupancy\": {" +
                    "\"available\":" + available + "," +
                    "\"occupied\":" + occupied + "," +
                    "\"total\":" + rooms.size() +
                    "}" +
                    "}";
            response.getWriter().write(json);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Database error\"}");
        }
    }
}
