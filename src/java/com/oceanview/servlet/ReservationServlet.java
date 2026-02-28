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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@WebServlet("/reservation")
public class ReservationServlet extends HttpServlet {
    private ReservationDAO reservationDAO = new ReservationDAO();
    private RoomDAO roomDAO = new RoomDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        try {
            if ("getAll".equals(action)) {
                List<Reservation> list = reservationDAO.getAllReservations();
                response.getWriter().write(serializeReservationList(list));
            } else if ("get".equals(action)) {
                String resNo = request.getParameter("resNo");
                Reservation res = reservationDAO.getReservationByNumber(resNo);
                if (res != null) {
                    response.getWriter().write(serializeReservation(res));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"success\": false, \"message\": \"Reservation not found\"}");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Database error: " + e.getMessage() + "\"}");
        }
    }

    private String serializeReservation(Reservation r) {
        return "{"
                + "\"reservationNumber\":\"" + r.getReservationNumber() + "\","
                + "\"guestId\":" + r.getGuestId() + ","
                + "\"roomNumber\":\"" + r.getRoomNumber() + "\","
                + "\"checkInDate\":\"" + r.getCheckInDate().toString() + "\","
                + "\"checkOutDate\":\"" + r.getCheckOutDate().toString() + "\","
                + "\"totalCost\":" + r.getTotalCost() + ","
                + "\"status\":\"" + r.getStatus() + "\""
                + "}";
    }

    private String serializeReservationList(List<Reservation> list) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            json.append(serializeReservation(list.get(i)));
            if (i < list.size() - 1)
                json.append(",");
        }
        json.append("]");
        return json.toString();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        try {
            if ("book".equals(action)) {
                int guestId = Integer.parseInt(request.getParameter("guestId"));
                String roomNumber = request.getParameter("roomNumber");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date checkIn = sdf.parse(request.getParameter("checkIn"));
                Date checkOut = sdf.parse(request.getParameter("checkOut"));

                long diffInMillies = Math.abs(checkOut.getTime() - checkIn.getTime());
                long diff = diffInMillies / (1000 * 60 * 60 * 24);
                if (diff == 0)
                    diff = 1;

                double rate = 0;
                List<Room> rooms = roomDAO.getAllRooms();
                for (Room r : rooms) {
                    if (r.getRoomNumber().equals(roomNumber)) {
                        rate = r.getRate();
                        break;
                    }
                }
                double totalCost = diff * rate;

                String resNo = "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                Reservation res = new Reservation(resNo, guestId, roomNumber, checkIn, checkOut, totalCost,
                        "CHECKED_IN");

                if (reservationDAO.createReservation(res)) {
                    response.getWriter().write("{\"success\": true, \"reservationNumber\": \"" + resNo
                            + "\", \"totalCost\": " + totalCost + "}");
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"success\": false, \"message\": \"Failed to create reservation\"}");
                }
            } else if ("checkout".equals(action)) {
                String resNo = request.getParameter("resNo");
                if (reservationDAO.updateReservationStatus(resNo, "CHECKED_OUT")) {
                    response.getWriter().write("{\"success\": true, \"message\": \"Check-out successful\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"success\": false, \"message\": \"Check-out failed\"}");
                }
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
}
