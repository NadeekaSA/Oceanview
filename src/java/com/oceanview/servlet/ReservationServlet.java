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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String action = request.getParameter("action");
        if ("book".equals(action)) {
            try {
                int guestId = Integer.parseInt(request.getParameter("guestId"));
                String roomNumber = request.getParameter("roomNumber");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date checkIn = sdf.parse(request.getParameter("checkIn"));
                Date checkOut = sdf.parse(request.getParameter("checkOut"));

                // Calculate total cost
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
                    response.getWriter().write("{\"success\": true, \"reservationNumber\": \"" + resNo + "\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"success\": false, \"message\": \"Failed to create reservation\"}");
                }
            } catch (SQLException | ParseException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"success\": false, \"message\": \"Error processing reservation\"}");
            }
        }
    }
}
