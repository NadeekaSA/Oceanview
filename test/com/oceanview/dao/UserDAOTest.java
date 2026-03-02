package com.oceanview.dao;

import com.oceanview.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;

public class UserDAOTest {
    private UserDAO userDAO;
    private int testUserId;

    @Before
    public void setUp() {
        userDAO = new UserDAO();
    }

    @After
    public void tearDown() throws SQLException {
        if (testUserId > 0) {
            userDAO.deleteUser(testUserId);
        }
    }

    @Test
    public void testCreateAndAuthenticateUser() throws SQLException {
        User user = new User(0, "testuser", "password123", "RECEPTIONIST", "Test User");
        boolean result = userDAO.createUser(user);
        assertTrue("User should be created", result);

        // We need to find the ID, but UserDAO doesn't return it on create nor has
        // getByUsername
        // Let's find it in getAllUsers
        for (User u : userDAO.getAllUsers()) {
            if ("testuser".equals(u.getUsername())) {
                testUserId = u.getId();
                break;
            }
        }
        assertTrue("User ID should be found", testUserId > 0);

        User authenticated = userDAO.authenticate("testuser", "password123");
        assertNotNull("Authentication should succeed", authenticated);
        assertEquals("Full name should match", "Test User", authenticated.getFullName());
    }

    @Test
    public void testAuthenticateFailure() throws SQLException {
        User authenticated = userDAO.authenticate("nonexistent", "wrongpass");
        assertNull("Authentication should fail", authenticated);
    }
}
