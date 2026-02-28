package com.oceanview.servlet;

import com.oceanview.dao.GuestDAO;
import com.oceanview.model.Guest;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/guest")
public class GuestServlet extends HttpServlet {
    private GuestDAO guestDAO = new GuestDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String guestIdStr = request.getParameter("id");
        String query = request.getParameter("query");

        try {
            if (guestIdStr != null && !guestIdStr.isEmpty()) {
                int id = Integer.parseInt(guestIdStr);
                Guest guest = guestDAO.getGuestById(id);
                if (guest != null) {
                    response.getWriter().write(serializeGuest(guest));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"success\": false, \"message\": \"Guest not found\"}");
                }
            } else if (query != null && !query.isEmpty()) {
                List<Guest> guests = guestDAO.searchGuests(query);
                response.getWriter().write(serializeGuestList(guests));
            } else {
                List<Guest> guests = guestDAO.getAllGuests();
                response.getWriter().write(serializeGuestList(guests));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log to server console
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Database error: " + e.getMessage() + "\"}");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid Guest ID format\"}");
        }
    }

    private String serializeGuest(Guest guest) {
        return "{" +
                "\"id\":" + guest.getId() + "," +
                "\"name\":\"" + escapeJson(guest.getName()) + "\"," +
                "\"email\":\"" + escapeJson(guest.getEmail()) + "\"," +
                "\"contact\":\"" + escapeJson(guest.getContact()) + "\"," +
                "\"idCardNumber\":\"" + escapeJson(guest.getIdCardNumber()) + "\"," +
                "\"address\":\"" + escapeJson(guest.getAddress()) + "\"" +
                "}";
    }

    private String serializeGuestList(List<Guest> guests) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < guests.size(); i++) {
            json.append(serializeGuest(guests.get(i)));
            if (i < guests.size() - 1)
                json.append(",");
        }
        json.append("]");
        return json.toString();
    }

    private String escapeJson(String text) {
        if (text == null)
            return "";
        return text.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if ("register".equals(action)) {
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            String contact = request.getParameter("contact");
            String email = request.getParameter("email");
            String idCard = request.getParameter("idCard");

            Guest guest = new Guest(0, name, address, contact, email, idCard);
            try {
                if (guestDAO.addGuest(guest)) {
                    response.getWriter()
                            .write("{\"success\": true, \"message\": \"Guest registered successfully\", \"id\":"
                                    + guest.getId() + "}");
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"success\": false, \"message\": \"Failed to register guest\"}");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Database error: " + e.getMessage() + "\"}");
            }
        }
    }
}
