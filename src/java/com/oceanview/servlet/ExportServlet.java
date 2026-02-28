package com.oceanview.servlet;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/export")
public class ExportServlet extends HttpServlet {
    private ReservationDAO reservationDAO = new ReservationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String format = request.getParameter("format");
        try {
            List<Reservation> reservations = reservationDAO.getAllReservations();

            if ("csv".equalsIgnoreCase(format)) {
                exportCSV(response, reservations);
            } else if ("excel".equalsIgnoreCase(format)) {
                exportExcel(response, reservations);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void exportCSV(HttpServletResponse response, List<Reservation> reservations) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=reservations_report.csv");
        PrintWriter writer = response.getWriter();
        writer.println("Reservation No,Guest ID,Room,Check In,Check Out,Cost,Status");

        for (Reservation res : reservations) {
            writer.printf("%s,%d,%s,%s,%s,%.2f,%s\n",
                    res.getReservationNumber(),
                    res.getGuestId(),
                    res.getRoomNumber(),
                    res.getCheckInDate(),
                    res.getCheckOutDate(),
                    res.getTotalCost(),
                    res.getStatus());
        }
    }

    private void exportExcel(HttpServletResponse response, List<Reservation> reservations) throws IOException {
        // We use HTML format for Excel export as it's natively supported and doesn't
        // require libraries
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=reservations_report.xls");
        PrintWriter writer = response.getWriter();

        writer.println("<table border='1'>");
        writer.println(
                "<tr><th>Reservation No</th><th>Guest ID</th><th>Room</th><th>Check In</th><th>Check Out</th><th>Cost</th><th>Status</th></tr>");
        for (Reservation res : reservations) {
            writer.println("<tr>");
            writer.printf("<td>%s</td><td>%d</td><td>%s</td><td>%s</td><td>%s</td><td>%.2f</td><td>%s</td>",
                    res.getReservationNumber(),
                    res.getGuestId(),
                    res.getRoomNumber(),
                    res.getCheckInDate(),
                    res.getCheckOutDate(),
                    res.getTotalCost(),
                    res.getStatus());
            writer.println("</tr>");
        }
        writer.println("</table>");
    }
}
