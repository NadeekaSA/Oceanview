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
        String guestIdStr = request.getParameter("id");

        try {
            if (guestIdStr != null) {
                int id = Integer.parseInt(guestIdStr);
                Guest guest = guestDAO.getGuestById(id);
                if (guest != null) {
                    String json = "{" +
                            "\"id\":" + guest.getId() + "," +
                            "\"name\":\"" + guest.getName() + "\"," +
                            "\"email\":\"" + guest.getEmail() + "\"," +
                            "\"contact\":\"" + guest.getContact() + "\"," +
                            "\"idCardNumber\":\"" + guest.getIdCardNumber() + "\"," +
                            "\"address\":\"" + guest.getAddress() + "\"" +
                            "}";
                    response.getWriter().write(json);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"success\": false, \"message\": \"Guest not found\"}");
                }
            } else if (request.getParameter("query") != null) {
                String query = request.getParameter("query");
                List<Guest> guests = guestDAO.searchGuests(query);
                response.getWriter().write(toJSONList(guests));
            } else {
                List<Guest> guests = guestDAO.getAllGuests();
                response.getWriter().write(toJSONList(guests));
            }
        } catch (SQLException | NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Error retrieving guest data\"}");
        }
    }

    private String toJSONList(List<Guest> guests) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < guests.size(); i++) {
            Guest g = guests.get(i);
            json.append("{")
                    .append("\"id\":").append(g.getId()).append(",")
                    .append("\"name\":\"").append(g.getName()).append("\",")
                    .append("\"email\":\"").append(g.getEmail()).append("\",")
                    .append("\"contact\":\"").append(g.getContact()).append("\",")
                    .append("\"idCardNumber\":\"").append(g.getIdCardNumber()).append("\",")
                    .append("\"address\":\"").append(g.getAddress()).append("\"")
                    .append("}");
            if (i < guests.size() - 1)
                json.append(",");
        }
        json.append("]");
        return json.toString();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
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
                    response.getWriter().write("{\"success\": true, \"message\": \"Guest registered successfully\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"success\": false, \"message\": \"Failed to register guest\"}");
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"success\": false, \"message\": \"Database error\"}");
            }
        }
    }
}
