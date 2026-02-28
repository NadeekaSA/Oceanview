package com.oceanview.servlet;

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

@WebServlet("/room")
public class RoomServlet extends HttpServlet {
    private RoomDAO roomDAO = new RoomDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try {
            List<Room> rooms = roomDAO.getAllRooms();
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < rooms.size(); i++) {
                Room r = rooms.get(i);
                json.append("{")
                        .append("\"roomNumber\":\"").append(r.getRoomNumber()).append("\",")
                        .append("\"type\":\"").append(r.getType()).append("\",")
                        .append("\"rate\":").append(r.getRate()).append(",")
                        .append("\"status\":\"").append(r.getStatus()).append("\"")
                        .append("}");
                if (i < rooms.size() - 1)
                    json.append(",");
            }
            json.append("]");
            response.getWriter().write(json.toString());
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Database error\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String action = request.getParameter("action");
        if ("updateRate".equals(action)) {
            String roomNumber = request.getParameter("roomNumber");
            double rate = Double.parseDouble(request.getParameter("rate"));
            try {
                if (roomDAO.updateRoomRate(roomNumber, rate)) {
                    response.getWriter().write("{\"success\": true, \"message\": \"Rate updated\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"success\": false, \"message\": \"Update failed\"}");
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"success\": false, \"message\": \"Database error\"}");
            }
        } else if ("add".equals(action)) {
            String roomNumber = request.getParameter("roomNumber");
            String type = request.getParameter("type");
            double rate = Double.parseDouble(request.getParameter("rate"));
            try {
                if (roomDAO.addRoom(roomNumber, type, rate)) {
                    response.getWriter().write("{\"success\": true, \"message\": \"Room added successfully\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"success\": false, \"message\": \"Failed to add room\"}");
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Database error: " + e.getMessage() + "\"}");
            }
        }
    }
}
