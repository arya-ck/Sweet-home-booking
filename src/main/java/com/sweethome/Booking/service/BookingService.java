package com.sweethome.Booking.service;

import com.sweethome.Booking.dto.BookingPaymentInfoDTO;
import com.sweethome.Booking.entities.BookingInfoEntity;

public interface BookingService {
    public BookingInfoEntity bookHotel(BookingInfoEntity bookingInfo);
    public BookingInfoEntity doPayment(int bookingId, BookingPaymentInfoDTO paymentInfoDTO);
    public boolean checkBookingValidity(int bookingId);
}
