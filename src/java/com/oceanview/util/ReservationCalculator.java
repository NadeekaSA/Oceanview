package com.oceanview.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for reservation-related calculations.
 */
public class ReservationCalculator {

    /**
     * Calculates the total cost of a reservation based on stay duration and daily
     * rate.
     * 
     * @param checkIn    The check-in date.
     * @param checkOut   The check-out date.
     * @param ratePerDay The daily rate.
     * @return The calculated total cost.
     * @throws IllegalArgumentException if check-out is before check-in or dates are
     *                                  null.
     */
    public static double calculateTotalCost(Date checkIn, Date checkOut, double ratePerDay) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }

        if (checkOut.before(checkIn)) {
            throw new IllegalArgumentException("Check-out date cannot be before check-in date");
        }

        long diffInMillies = Math.abs(checkOut.getTime() - checkIn.getTime());
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        // Ensure at least 1 day if dates are different (standard hotel practice)
        if (diffInDays == 0 && !checkIn.equals(checkOut)) {
            diffInDays = 1;
        }

        return diffInDays * ratePerDay;
    }
}
