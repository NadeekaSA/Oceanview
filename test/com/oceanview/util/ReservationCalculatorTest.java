package com.oceanview.util;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Calendar;
import java.util.Date;

public class ReservationCalculatorTest {

    @Test
    public void testCalculateTotalCost_NormalRange() {
        Calendar cal = Calendar.getInstance();
        Date checkIn = cal.getTime();

        cal.add(Calendar.DAY_OF_YEAR, 3);
        Date checkOut = cal.getTime();

        double ratePerDay = 100.0;
        double expected = 300.0;

        assertEquals("Calculation for 3 days should be 300",
                expected,
                ReservationCalculator.calculateTotalCost(checkIn, checkOut, ratePerDay),
                0.001);
    }

    @Test
    public void testCalculateTotalCost_SingleNight() {
        Calendar cal = Calendar.getInstance();
        Date checkIn = cal.getTime();

        cal.add(Calendar.DAY_OF_YEAR, 1);
        Date checkOut = cal.getTime();

        double ratePerDay = 150.0;
        double expected = 150.0;

        assertEquals("Calculation for 1 night should be 150",
                expected,
                ReservationCalculator.calculateTotalCost(checkIn, checkOut, ratePerDay),
                0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateTotalCost_InvalidDates() {
        Calendar cal = Calendar.getInstance();
        Date checkIn = cal.getTime();

        cal.add(Calendar.DAY_OF_YEAR, -1);
        Date checkOut = cal.getTime();

        ReservationCalculator.calculateTotalCost(checkIn, checkOut, 100.0);
    }
}
