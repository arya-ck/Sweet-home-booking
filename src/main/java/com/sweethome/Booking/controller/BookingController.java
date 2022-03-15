package com.sweethome.Booking.controller;

import com.sweethome.Booking.dto.BookingInfoDto;
import com.sweethome.Booking.dto.BookingPaymentInfoDTO;
import com.sweethome.Booking.dto.InvalidPaymentMessageDTO;
import com.sweethome.Booking.entities.BookingInfoEntity;
import com.sweethome.Booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotel")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @PostMapping("/booking")
    public ResponseEntity bookRooms(@RequestBody BookingInfoDto bookingInfo){
        BookingInfoEntity bookingInfoEntity = new BookingInfoEntity();
        bookingInfoEntity.setFromDate(bookingInfo.getFromDate());
        bookingInfoEntity.setToDate(bookingInfo.getToDate());
        bookingInfoEntity.setAadharNumber(bookingInfo.getAadharNumber());
        bookingInfoEntity.setNumOfRooms(bookingInfo.getNumOfRooms());

        BookingInfoEntity savedBooking = bookingService.bookHotel(bookingInfoEntity);

        return new ResponseEntity(savedBooking, HttpStatus.CREATED);
    }

    @PostMapping("/booking/{bookingId}/transaction")
    public ResponseEntity bookRooms(@PathVariable int bookingId, @RequestBody BookingPaymentInfoDTO paymentInfoDTO){

        InvalidPaymentMessageDTO invalidPaymentMessageDTO = new InvalidPaymentMessageDTO();

        // payment mode invalid
        if(!paymentInfoDTO.getPaymentMode().equals("UPI") && !paymentInfoDTO.getPaymentMode().equals("CARD")){
            invalidPaymentMessageDTO.setMessage("Invalid mode of payment");
            invalidPaymentMessageDTO.setStatusCode(400);
            return new ResponseEntity(invalidPaymentMessageDTO, HttpStatus.BAD_REQUEST);
        }

        // booking does not exists
        if(!bookingService.checkBookingValidity(bookingId)){
            invalidPaymentMessageDTO.setMessage("Invalid Booking Id");
            invalidPaymentMessageDTO.setStatusCode(400);
            return new ResponseEntity(invalidPaymentMessageDTO, HttpStatus.BAD_REQUEST);
        }

        // booking exists & valid payment mode
        BookingInfoEntity savedBooking = bookingService.doPayment(bookingId, paymentInfoDTO);

        return new ResponseEntity(savedBooking, HttpStatus.CREATED);
    }

}
