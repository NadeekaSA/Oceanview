package com.oceanview.dao;

import com.oceanview.model.Room;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;
import java.util.List;

public class RoomDAOTest {
    private RoomDAO roomDAO;
    private final String TEST_ROOM_NO = "T999";

    @Before
    public void setUp() {
        roomDAO = new RoomDAO();
    }

    @After
    public void tearDown() throws SQLException {
        // Cleanup: remove the test room if it exists (using a direct SQL or similar)
        // Since RoomDAO doesn't have a delete, we'll just try to keep it unique or
        // assume a clean state
    }

    @Test
    public void testAddAndGetRooms() throws SQLException {
        boolean result = roomDAO.addRoom(TEST_ROOM_NO, "SUITE", 200.0);
        assertTrue("Room should be added", result);

        List<Room> rooms = roomDAO.getAllRooms();
        boolean found = false;
        for (Room r : rooms) {
            if (TEST_ROOM_NO.equals(r.getRoomNumber())) {
                found = true;
                assertEquals("Rate should match", 200.0, r.getRate(), 0.001);
                break;
            }
        }
        assertTrue("Added room should be in the list", found);
    }

    @Test
    public void testUpdateRoomStatus() throws SQLException {
        roomDAO.updateRoomStatus(TEST_ROOM_NO, "MAINTENANCE");

        List<Room> rooms = roomDAO.getAllRooms();
        for (Room r : rooms) {
            if (TEST_ROOM_NO.equals(r.getRoomNumber())) {
                assertEquals("Status should be MAINTENANCE", "MAINTENANCE", r.getStatus());
                break;
            }
        }
    }

    @Test
    public void testUpdateRoomRate() throws SQLException {
        roomDAO.updateRoomRate(TEST_ROOM_NO, 250.0);

        List<Room> rooms = roomDAO.getAllRooms();
        for (Room r : rooms) {
            if (TEST_ROOM_NO.equals(r.getRoomNumber())) {
                assertEquals("Rate should be updated", 250.0, r.getRate(), 0.001);
                break;
            }
        }
    }
}
