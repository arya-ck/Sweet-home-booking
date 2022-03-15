package com.sweethome.Booking.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "booking")
public class BookingInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="bookingId", nullable = false)
    int id;

    @Column(name="fromDate", nullable = true)
    LocalDate fromDate;

    @Column(name="toDate", nullable = true)
    LocalDate toDate;

    @Column(name="aadharNumber", nullable = true)
    String aadharNumber;

    @Column(name="numOfRooms")
    int numOfRooms;

    @Column(name="roomNumbers")
    String roomNumbers;

    @Column(name="roomPrice", nullable = false)
    int roomPrice;

    @Column(name="transactionId", nullable = false)
    int transactionId = 0;

    @Column(name="bookedOn", nullable = true)
    LocalDateTime bookedOn;

    public int getBookingId() {
        return id;
    }

    public void setBookingId(int bookingId) {
        this.id = bookingId;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public int getNumOfRooms() {
        return numOfRooms;
    }

    public void setNumOfRooms(int numOfRooms) {
        this.numOfRooms = numOfRooms;
    }

    public String getRoomNumbers() {
        return roomNumbers;
    }

    public void setRoomNumbers(String roomNumbers) {
        this.roomNumbers = roomNumbers;
    }

    public int getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(int roomPrice) {
        this.roomPrice = roomPrice;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getBookedOn() {
        return bookedOn;
    }

    public void setBookedOn(LocalDateTime bookedOn) {
        this.bookedOn = bookedOn;
    }

    @Override
    public String toString() {
        return "BookingInfoEntity{" +
                "bookingId=" + id +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", aadharNumber='" + aadharNumber + '\'' +
                ", numOfRooms=" + numOfRooms +
                ", roomNumbers='" + roomNumbers + '\'' +
                ", roomPrice=" + roomPrice +
                ", transactionId=" + transactionId +
                ", bookedOn=" + bookedOn +
                '}';
    }
}
