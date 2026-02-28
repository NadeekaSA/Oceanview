package com.oceanview.model;

public class Room {
    private String roomNumber;
    private String type;
    private double rate;
    private String status;

    public Room() {
    }

    public Room(String roomNumber, String type, double rate, String status) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.rate = rate;
        this.status = status;
    }

    // Getters and Setters
    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
