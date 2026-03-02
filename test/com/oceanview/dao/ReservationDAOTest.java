package com.oceanview.dao;

import com.oceanview.model.Reservation;
import com.oceanview.model.Guest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.Calendar;

public class ReservationDAOTest {
    private ReservationDAO reservationDAO;
    private GuestDAO guestDAO;
    private int testGuestId;
    private final String TEST_RES_NO = "RES_TEST_123";
    private final String TEST_ROOM_NO = "R101"; // Assuming R101 exists or we add it

    @Before
    public void setUp() throws SQLException {
        reservationDAO = new ReservationDAO();
        guestDAO = new GuestDAO();

        // Need a guest for reservation
        Guest guest = new Guest(0, "Res Test Guest", "Addr", "123", "test@res.com", "IDRES");
        guestDAO.addGuest(guest);
        testGuestId = guest.getId();
    }

    @After
    public void tearDown() throws SQLException {
        // Cleanup reservation (if possible, but DAO lacks delete)
        // Cleanup guest
        if (testGuestId > 0) {
            guestDAO.deleteGuest(testGuestId);
        }
    }

    @Test
    public void testCreateAndGetReservation() throws SQLException {
        Calendar cal = Calendar.getInstance();
        Date checkIn = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 2);
        Date checkOut = cal.getTime();

        Reservation res = new Reservation(TEST_RES_NO, testGuestId, TEST_ROOM_NO, checkIn, checkOut, 200.0, "BOOKED");
        boolean result = reservationDAO.createReservation(res);
        assertTrue("Reservation should be created", result);

        Reservation fetched = reservationDAO.getReservationByNumber(TEST_RES_NO);
        assertNotNull("Reservation should be fetched", fetched);
        assertEquals("Room number should match", TEST_ROOM_NO, fetched.getRoomNumber());
    }

    @Test
    public void testUpdateStatus() throws SQLException {
        reservationDAO.updateReservationStatus(TEST_RES_NO, "CHECKED_IN");
        Reservation fetched = reservationDAO.getReservationByNumber(TEST_RES_NO);
        if (fetched != null) {
            assertEquals("Status should be CHECKED_IN", "CHECKED_IN", fetched.getStatus());
        }
    }
}
