package com.oceanview.dao;

import com.oceanview.model.Guest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;
import java.util.List;

public class GuestDAOTest {
    private GuestDAO guestDAO;
    private int testGuestId;

    @Before
    public void setUp() {
        guestDAO = new GuestDAO();
    }

    @After
    public void tearDown() throws SQLException {
        if (testGuestId > 0) {
            guestDAO.deleteGuest(testGuestId);
        }
    }

    @Test
    public void testAddAndGetGuest() throws SQLException {
        Guest guest = new Guest(0, "Test Guest", "123 Test St", "1234567890", "test@example.com", "ID12345");
        boolean result = guestDAO.addGuest(guest);

        assertTrue("Guest should be added successfully", result);
        assertTrue("Guest ID should be generated", guest.getId() > 0);
        testGuestId = guest.getId();

        Guest fetched = guestDAO.getGuestById(testGuestId);
        assertNotNull("Fetched guest should not be null", fetched);
        assertEquals("Name should match", "Test Guest", fetched.getName());
    }

    @Test
    public void testUpdateGuest() throws SQLException {
        Guest guest = new Guest(0, "Update Test", "Address", "Phone", "email@test.com", "ID999");
        guestDAO.addGuest(guest);
        testGuestId = guest.getId();

        guest.setName("Updated Name");
        boolean result = guestDAO.updateGuest(guest);
        assertTrue("Update should be successful", result);

        Guest fetched = guestDAO.getGuestById(testGuestId);
        assertEquals("Name should be updated", "Updated Name", fetched.getName());
    }

    @Test
    public void testSearchGuests() throws SQLException {
        Guest guest = new Guest(0, "Searchable Guest", "Address", "Phone", "search@test.com", "UNIQUE_ID_888");
        guestDAO.addGuest(guest);
        testGuestId = guest.getId();

        List<Guest> results = guestDAO.searchGuests("Searchable");
        assertFalse("Search results should not be empty", results.isEmpty());

        boolean found = false;
        for (Guest g : results) {
            if (g.getId() == testGuestId) {
                found = true;
                break;
            }
        }
        assertTrue("Added guest should be found in search", found);
    }
}
